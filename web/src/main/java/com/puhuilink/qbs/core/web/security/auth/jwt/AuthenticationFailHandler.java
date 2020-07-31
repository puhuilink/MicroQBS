/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:24:26
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:10:08
 */
package com.puhuilink.qbs.core.web.security.auth.jwt;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.utils.InheritableThreadLocalUtil;
import com.puhuilink.qbs.core.web.config.properties.QbsTokenProperties;
import com.puhuilink.qbs.core.web.security.auth.rest.LoginRequest;
import com.puhuilink.qbs.core.web.utils.ResponseUtil;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private QbsTokenProperties tokenProperties;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException e) throws IOException, ServletException {
        LoginRequest loginRequest = InheritableThreadLocalUtil.get(LoginRequest.class);
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException
                || e instanceof InternalAuthenticationServiceException) {
            if (loginRequest == null) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, ResultCode.JWT_TOKEN_EXPIRED, e.getMessage()));
                return;
            }
            recordLoginTime(loginRequest.getUsername());
            String username = loginRequest.getUsername();
            String key = "loginTimeLimit:" + username;
            RBucket<String> bucket = redissonClient.getBucket(key, new StringCodec());
            String value = bucket.get();
            if (StrUtil.isBlank(value)) {
                value = "0";
            }
            // 获取已登录错误次数
            int loginFailTime = Integer.parseInt(value);
            int restLoginTime = tokenProperties.getLoginTimeLimit() - loginFailTime;
            log.info("用户" + username + "登录失败，还有" + restLoginTime + "次机会");
            if (restLoginTime <= tokenProperties.getLoginTimeNotify() && restLoginTime > 0) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, ResultCode.INTERNAL_SERVER_ERROR,
                        "用户名或密码错误，还有" + restLoginTime + "次尝试机会"));
            } else if (restLoginTime <= 0) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, ResultCode.LOGIN_FAIL_MANY_TIMES,
                        "登录错误次数超过限制，请" + tokenProperties.getLoginAfterTime() + "分钟后再试"));
            } else {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, ResultCode.INTERNAL_SERVER_ERROR, "用户名或密码错误"));
            }
        } else if (e instanceof DisabledException) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, ResultCode.FORBIDDEN, "账户被禁用，请联系管理员"));
        } else {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, ResultCode.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    /**
     * 判断用户登陆错误次数
     */
    public boolean recordLoginTime(String username) {

        String key = "loginTimeLimit:" + username;
        String flagKey = "loginFailFlag:" + username;
        RBucket<String> bucket = redissonClient.getBucket(key, new StringCodec());
        String value = bucket.get();
        if (StrUtil.isBlank(value)) {
            value = "0";
        }
        // 获取已登录错误次数
        int loginFailTime = Integer.parseInt(value) + 1;
        redissonClient.getBucket(key, new StringCodec()).set(String.valueOf(loginFailTime),
                tokenProperties.getLoginAfterTime(), TimeUnit.MINUTES);
        if (loginFailTime >= tokenProperties.getLoginTimeLimit()) {
            redissonClient.getBucket(flagKey, new StringCodec()).set("fail", tokenProperties.getLoginAfterTime(),
                    TimeUnit.MINUTES);
            return false;
        }
        return true;
    }
}
