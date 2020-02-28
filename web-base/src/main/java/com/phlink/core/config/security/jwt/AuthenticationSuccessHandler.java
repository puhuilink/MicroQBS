package com.phlink.core.config.security.jwt;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.google.gson.Gson;
import com.phlink.core.common.annotation.SystemLogTrace;
import com.phlink.core.common.constant.SecurityConstant;
import com.phlink.core.common.enums.CommonResultInfo;
import com.phlink.core.common.enums.LogType;
import com.phlink.core.common.utils.IpInfoUtil;
import com.phlink.core.common.utils.ResponseUtil;
import com.phlink.core.common.vo.TokenUser;
import com.phlink.core.config.properties.PhlinkTokenProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private PhlinkTokenProperties tokenProperties;

    @Autowired
    private IpInfoUtil ipInfoUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @SystemLogTrace(description = "登录系统", type = LogType.LOGIN)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //用户选择保存登录状态几天
        String saveLogin = request.getParameter(SecurityConstant.SAVE_LOGIN);
        Boolean saved = false;
        if (StrUtil.isNotBlank(saveLogin) && Boolean.valueOf(saveLogin)) {
            saved = true;
            if (!tokenProperties.getRedis()) {
                tokenProperties.setTokenExpireTime(tokenProperties.getSaveLoginTime() * 60 * 24);
            }
        }
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) ((UserDetails) authentication.getPrincipal()).getAuthorities();
        List<String> list = new ArrayList<>();
        for (GrantedAuthority g : authorities) {
            list.add(g.getAuthority());
        }
        // 登陆成功生成token
        String token;
        if (tokenProperties.getRedis()) {
            // redis
            token = UUID.randomUUID().toString().replace("-", "");
            TokenUser user = new TokenUser(username, list, saved);
            // 不缓存权限
            if (!tokenProperties.getStorePerms()) {
                user.setPermissions(null);
            }
            // 单设备登录 之前的token失效
            if (tokenProperties.getSdl()) {
                RBucket<String> bucket = redissonClient.getBucket(SecurityConstant.USER_TOKEN + username, new StringCodec());
                String oldToken = bucket.get();
                if (StrUtil.isNotBlank(oldToken)) {
                    redissonClient.getKeys().delete(SecurityConstant.TOKEN_PRE + oldToken);
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
            // 不缓存权限
            if (!tokenProperties.getStorePerms()) {
                list = null;
            }
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

        ResponseUtil.out(response, ResponseUtil.resultMap(true, CommonResultInfo.SUCCESS.getResultCode(), "登录成功", token));
    }
}