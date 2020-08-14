package com.puhuilink.qbs.auth.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.puhuilink.qbs.auth.config.properties.QbsTokenProperties;
import com.puhuilink.qbs.auth.entity.*;
import com.puhuilink.qbs.auth.security.model.SecurityUser;
import com.puhuilink.qbs.auth.security.model.token.RawAccessJwtToken;
import com.puhuilink.qbs.auth.service.DepartmentService;
import com.puhuilink.qbs.auth.service.UserRoleService;
import com.puhuilink.qbs.auth.service.UserService;
import com.puhuilink.qbs.auth.service.UserTokenService;
import com.puhuilink.qbs.core.common.utils.CommonConstant;
import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.WarnException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
public class SecurityUtil {

    @Autowired
    private QbsTokenProperties tokenProperties;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService iUserRoleService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private DepartmentService departmentService;

    public String getAccessJwtToken(String username, Boolean saveLogin) {
        if (StringUtils.isBlank(username)) {
            throw new WarnException(ResultCode.AUTHENTICATION.getCode(), "用户名不能为空");
        }
        long expireTime = tokenProperties.getTokenExpireTime();
        if (saveLogin == null || saveLogin) {
            expireTime = tokenProperties.getSaveLoginTime() * 60 * 24;
        }
        // 生成token
        User u = userService.getByUsername(username);
        if (u == null) {
            log.warn("用户{}不存在", username);
            throw new WarnException(ResultCode.AUTHENTICATION.getCode(), "用户不存在");
        }
        List<String> list = new ArrayList<>();
        // 缓存权限
        if (tokenProperties.getStorePerms()) {
            for (Permission p : u.getPermissions()) {
                if (CommonConstant.PERMISSION_OPERATION.equals(p.getType()) && StringUtils.isNotBlank(p.getTitle())
                        && StringUtils.isNotBlank(p.getPath())) {
                    list.add(p.getTitle());
                }
            }
            for (Role r : u.getRoles()) {
                list.add(r.getName());
            }
        }
        SecretKey key = getSecretKey();
        // 登陆成功生成token
        String token = Jwts.builder()
                // 主题 放入用户名
                .setSubject(username)
                .setIssuer(SecurityConstant.TOKEN_ISSUER)
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                // 自定义属性 放入用户拥有请求权限
                .claim(SecurityConstant.CLAIMS_AUTHORITIES, new Gson().toJson(list))
                // 自定义属性 放入用户ID
                .claim(SecurityConstant.CLAIMS_USER_ID, u.getId())
                // 失效时间
                .setExpiration(
                        new Date(System.currentTimeMillis() + expireTime * 60 * 1000))
                // 签名算法和密钥
                .signWith(key)
                .compact();

        String tokenEncode = EncrypterHelper.encrypt(token);
        if(tokenProperties.getSdl()) {
            // 单设备登录，只允许一个存在
            userTokenService.logoutAndCreate(u, tokenEncode);
        }else{
            userTokenService.create(u, tokenEncode);
        }
        return tokenEncode;
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SecurityConstant.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public User getCurrUser() {
        SecurityUser securityUser = getSecurityUser();
        return userService.getByUsername(securityUser.getUsername());
    }

    public SecurityUser getSecurityUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            return (SecurityUser) authentication.getPrincipal();
        } else {
            throw new WarnException(ResultCode.AUTHENTICATION.getCode(), "无效的认证签名，请重新登录");
        }
    }

    /**
     * 获取当前用户数据权限 null代表具有所有权限 包含值为-1的数据代表无任何权限
     */
    public List<String> getDeparmentIds() {

        List<String> deparmentIds = new ArrayList<>();
        User u = getCurrUser();
        // 当前用户拥有角色
        List<Role> roles = iUserRoleService.listByUserId(u.getId());
        // 判断有无全部数据的角色
        Boolean flagAll = false;
        for (Role r : roles) {
            if (r.getDataType() == null || r.getDataType().equals(CommonConstant.DATA_TYPE_ALL)) {
                flagAll = true;
                break;
            }
        }
        // 包含全部权限返回null
        if (flagAll) {
            return null;
        }
        // 每个角色判断 求并集
        for (Role r : roles) {
            if (r.getDataType().equals(CommonConstant.DATA_TYPE_UNDER)) {
                // 本部门及以下
                if (StringUtils.isBlank(u.getDepartmentId())) {
                    // 用户无部门
                    deparmentIds.add("-1");
                } else {
                    // 递归获取自己与子级
                    List<String> ids = new ArrayList<>();
                    getRecursion(u.getDepartmentId(), ids);
                    deparmentIds.addAll(ids);
                }
            } else if (r.getDataType().equals(CommonConstant.DATA_TYPE_SAME)) {
                // 本部门
                if (StringUtils.isBlank(u.getDepartmentId())) {
                    // 用户无部门
                    deparmentIds.add("-1");
                } else {
                    deparmentIds.add(u.getDepartmentId());
                }
            } else if (r.getDataType().equals(CommonConstant.DATA_TYPE_CUSTOM)) {
                // 自定义
                List<String> depIds = iUserRoleService.listDepIdsByUserId(u.getId());
                if (depIds == null || depIds.size() == 0) {
                    deparmentIds.add("-1");
                } else {
                    deparmentIds.addAll(depIds);
                }
            }
        }
        // 去重
        LinkedHashSet<String> set = new LinkedHashSet<>(deparmentIds.size());
        set.addAll(deparmentIds);
        deparmentIds.clear();
        deparmentIds.addAll(set);
        return deparmentIds;
    }

    private void getRecursion(String departmentId, List<String> ids) {

        Department department = departmentService.getById(departmentId);
        ids.add(department.getId());
        if (department.getIsParent() != null && department.getIsParent()) {
            // 获取其下级
            List<Department> departments = departmentService.listByParentIdAndStatusOrderBySortOrder(departmentId,
                    CommonConstant.STATUS_NORMAL);
            departments.forEach(d -> {
                getRecursion(d.getId(), ids);
            });
        }
    }

    /**
     * 通过用户名获取用户拥有权限
     *
     * @param username
     */
    public List<GrantedAuthority> getCurrUserPerms(String username) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        User user = userService.getByUsername(username);
        for (Permission p : user.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(p.getTitle()));
        }
        return authorities;
    }

    public SecurityUser parseAccessJwtToken(RawAccessJwtToken rawAccessToken) throws BadCredentialsException {
        String tokenEncode = rawAccessToken.getToken();
        if(StringUtils.isBlank(tokenEncode)) {
            log.warn("Token 为空");
            throw new BadCredentialsException("认证签名不存在，请登录后再操作");
        }
        String token = EncrypterHelper.decrypt(tokenEncode);
        // 用户名
        String username = null;
        // 权限
        List<GrantedAuthority> authorities = new ArrayList<>();

        // JWT
        try {
            SecretKey key = getSecretKey();
            // 解析token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token).getBody();
            String userId = claims.getOrDefault(SecurityConstant.CLAIMS_USER_ID, "").toString();

            UserToken userToken = userTokenService.getLoginStatusTokenByUserId(userId, tokenEncode);

            if(userToken == null) {
                log.warn("User Token 不存在 userId:{} tokenEncode: {}", userId, tokenEncode);
                throw new BadCredentialsException("认证签名已失效，请重新登录");
            }
            username = claims.getSubject();

            // 获取权限
            // 缓存了权限
            String authority = claims.getOrDefault(SecurityConstant.CLAIMS_AUTHORITIES, "").toString();
            if (StringUtils.isNotBlank(authority)) {
                List<String> list = new Gson().fromJson(authority,
                        new TypeToken<List<String>>() {
                        }.getType());
                for (String ga : list) {
                    authorities.add(new SimpleGrantedAuthority(ga));
                }
            } else {
                // 未缓存 读取权限数据
                authorities = getCurrUserPerms(username);
            }
        } catch (ExpiredJwtException e) {
            log.warn("Token 过期 tokenEncode: {} Exception: {}", tokenEncode, e.getMessage());
            throw new BadCredentialsException("认证签名已失效，请重新登录");
        }

        if (StringUtils.isNotBlank(username)) {
            // 踩坑提醒 此处password不能为null
            User user = userService.getByUsername(username);
            SecurityUser securityUser = new SecurityUser(user);
            securityUser.setAuthorities(authorities);
            return securityUser;
        }
        throw new BadCredentialsException("认证签名无效");
    }
}
