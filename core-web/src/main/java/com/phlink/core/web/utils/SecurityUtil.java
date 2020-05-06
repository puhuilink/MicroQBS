/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:53:46
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 14:53:46
 */
package com.phlink.core.web.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.phlink.core.base.constant.CommonConstant;
import com.phlink.core.base.constant.SecurityConstant;
import com.phlink.core.base.enums.ResultCode;
import com.phlink.core.base.exception.BizException;
import com.phlink.core.base.vo.TokenUser;
import com.phlink.core.web.config.properties.PhlinkTokenProperties;
import com.phlink.core.web.entity.Department;
import com.phlink.core.web.entity.Permission;
import com.phlink.core.web.entity.Role;
import com.phlink.core.web.entity.User;
import com.phlink.core.web.security.model.SecurityUser;
import com.phlink.core.web.security.model.token.RawAccessJwtToken;
import com.phlink.core.web.service.DepartmentService;
import com.phlink.core.web.service.UserRoleService;
import com.phlink.core.web.service.UserService;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wen
 */
@Slf4j
@Component
public class SecurityUtil {

    @Autowired
    private PhlinkTokenProperties tokenProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService iUserRoleService;

    @Autowired
    private IpInfoUtil ipInfoUtil;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RedissonClient redissonClient;

    public String getAccessJwtToken(String username, Boolean saveLogin) {

        if (StrUtil.isBlank(username)) {
            throw new BizException("username不能为空");
        }
        boolean saved = false;
        if (saveLogin == null || saveLogin) {
            saved = true;
            if (!tokenProperties.getRedis()) {
                tokenProperties.setTokenExpireTime(tokenProperties.getSaveLoginTime() * 60 * 24);
            }
        }
        // 生成token
        User u = userService.getByUsername(username);
        if(u == null) {
            log.info("匿名登录");
        }
        List<String> list = new ArrayList<>();
        // 缓存权限
        if (tokenProperties.getStorePerms() && u != null) {
            for (Permission p : u.getPermissions()) {
                if (CommonConstant.PERMISSION_OPERATION.equals(p.getType())
                    && StrUtil.isNotBlank(p.getTitle())
                    && StrUtil.isNotBlank(p.getPath())) {
                    list.add(p.getTitle());
                }
            }
            for (Role r : u.getRoles()) {
                list.add(r.getName());
            }
        }
        // 登陆成功生成token
        String token;
        if (tokenProperties.getRedis()) {
            // redis
            token = UUID.randomUUID().toString().replace("-", "");
            TokenUser user = new TokenUser(username, list, saved);
            // 单设备登录 之前的token失效
            if (tokenProperties.getSdl()) {
                RBucket<String> bucket = redissonClient.getBucket(SecurityConstant.USER_TOKEN + username, new StringCodec());
                if (bucket != null) {
                    String oldToken = bucket.get();
                    if (StrUtil.isNotBlank(oldToken)) {
                        redissonClient.getKeys().delete(SecurityConstant.TOKEN_PRE + oldToken);
                    }
                }
            }
            if (saved) {
                redissonClient.getBucket(SecurityConstant.USER_TOKEN + username, new StringCodec()).set(token, tokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
                redissonClient.getBucket(SecurityConstant.TOKEN_PRE + token, new StringCodec()).set(new Gson().toJson(user), tokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
            } else {
                redissonClient.getBucket(SecurityConstant.USER_TOKEN + username, new StringCodec()).set(token, tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
                redissonClient.getBucket(SecurityConstant.TOKEN_PRE + token, new StringCodec()).set(new Gson().toJson(user), tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
            }
        } else {
            // jwt
            token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                //主题 放入用户名
                .setSubject(username)
                //自定义属性 放入用户拥有请求权限
                .claim(SecurityConstant.AUTHORITIES, new Gson().toJson(list))
                //失效时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getTokenExpireTime() * 60 * 1000))
                //签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                .compact();
        }
        return token;
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
            throw new BizException(ResultCode.AUTHENTICATION, "You aren't authorized to perform this operation!");
        }
    }

    /**
     * 获取当前用户数据权限 null代表具有所有权限 包含值为-1的数据代表无任何权限
     */
    public List<String> getDeparmentIds() {

        List<String> deparmentIds = new ArrayList<>();
        User u = getCurrUser();
        // 读取缓存
        String key = "userRole::depIds:" + u.getId();
        RBucket<String> bucket = redissonClient.getBucket(key, new StringCodec());
        String v = bucket.get();
        if (StrUtil.isNotBlank(v)) {
            deparmentIds = new Gson().fromJson(v, new TypeToken<List<String>>() {
            }.getType());
            return deparmentIds;
        }
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
                if (StrUtil.isBlank(u.getDepartmentId())) {
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
                if (StrUtil.isBlank(u.getDepartmentId())) {
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
        // 缓存
        redissonClient.getBucket(key, new StringCodec()).set(new Gson().toJson(deparmentIds));
        return deparmentIds;
    }

    private void getRecursion(String departmentId, List<String> ids) {

        Department department = departmentService.getById(departmentId);
        ids.add(department.getId());
        if (department.getIsParent() != null && department.getIsParent()) {
            // 获取其下级
            List<Department> departments = departmentService.listByParentIdAndStatusOrderBySortOrder(departmentId, CommonConstant.STATUS_NORMAL);
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

        // 用户名
        String username = null;
        // 权限
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (tokenProperties.getRedis()) {
            // redis
            RBucket<String> bucket = redissonClient.getBucket(SecurityConstant.TOKEN_PRE + rawAccessToken.getToken(), new StringCodec());
            if (bucket == null) {
                throw new BadCredentialsException("登录已失效，请重新登录");
            }
            String v = bucket.get();
            if (StrUtil.isBlank(v)) {
                throw new BadCredentialsException("登录已失效，请重新登录");
            }
            TokenUser user = new Gson().fromJson(v, TokenUser.class);
            username = user.getUsername();
            if (tokenProperties.getStorePerms()) {
                // 缓存了权限
                for (String ga : user.getPermissions()) {
                    authorities.add(new SimpleGrantedAuthority(ga));
                }
            } else {
                // 未缓存 读取权限数据
                authorities = getCurrUserPerms(username);
            }
            if (!user.getSaveLogin()) {
                // 若未保存登录状态重新设置失效时间
                RBucket<String> userTokenBucket = redissonClient.getBucket(SecurityConstant.USER_TOKEN + username, new StringCodec());
                userTokenBucket.set(rawAccessToken.getToken());
                userTokenBucket.expire(tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);

                RBucket<String> tokenBucket = redissonClient.getBucket(SecurityConstant.TOKEN_PRE + rawAccessToken.getToken(), new StringCodec());
                tokenBucket.set(v);
                tokenBucket.expire(tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
            }
        } else {
            // JWT
            try {
                // 解析token
                Claims claims = Jwts.parser()
                    .setSigningKey(SecurityConstant.JWT_SIGN_KEY)
                    .parseClaimsJws(rawAccessToken.getToken().replace(SecurityConstant.TOKEN_SPLIT, ""))
                    .getBody();

                // 获取用户名
                username = claims.getSubject();
                // 获取权限
                if (tokenProperties.getStorePerms()) {
                    // 缓存了权限
                    String authority = claims.get(SecurityConstant.AUTHORITIES).toString();
                    if (StrUtil.isNotBlank(authority)) {
                        List<String> list = new Gson().fromJson(authority, new com.google.common.reflect.TypeToken<List<String>>() {
                        }.getType());
                        for (String ga : list) {
                            authorities.add(new SimpleGrantedAuthority(ga));
                        }
                    }
                } else {
                    // 未缓存 读取权限数据
                    authorities = getCurrUserPerms(username);
                }
            } catch (ExpiredJwtException e) {
                throw new BadCredentialsException("登录已失效，请重新登录");
            } catch (Exception e) {
                log.error(e.toString());
                throw new BadCredentialsException("解析token错误");
            }
        }

        if (StrUtil.isNotBlank(username)) {
            // 踩坑提醒 此处password不能为null
            User user = userService.getByUsername(username);
            SecurityUser securityUser = new SecurityUser(user);
            securityUser.setAuthorities(authorities);
            return securityUser;
        }
        throw new BadCredentialsException("token无效");
    }
}
