package com.phlink.core.config.security.jwt;

import cn.hutool.core.util.StrUtil;
import com.phlink.core.common.enums.CommonResultInfo;
import com.phlink.core.common.exception.LoginFailLimitException;
import com.phlink.core.common.utils.ResponseUtil;
import com.phlink.core.config.properties.PhlinkTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private PhlinkTokenProperties tokenProperties;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            String username = request.getParameter("username");
            recordLoginTime(username);
            String key = "loginTimeLimit:" + username;
            RBucket<String> bucket = redissonClient.getBucket(key, new StringCodec());
            String value = bucket.get();
            if (StrUtil.isBlank(value)) {
                value = "0";
            }
            //获取已登录错误次数
            int loginFailTime = Integer.parseInt(value);
            int restLoginTime = tokenProperties.getLoginTimeLimit() - loginFailTime;
            log.info("用户" + username + "登录失败，还有" + restLoginTime + "次机会");
            if (restLoginTime <= 3 && restLoginTime > 0) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, CommonResultInfo.INTERNAL_SERVER_ERROR, "用户名或密码错误，还有" + restLoginTime + "次尝试机会"));
            } else if (restLoginTime <= 0) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, CommonResultInfo.LOGIN_FAIL_MANY_TIMES, "登录错误次数超过限制，请" + tokenProperties.getLoginAfterTime() + "分钟后再试"));
            } else {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, CommonResultInfo.INTERNAL_SERVER_ERROR, "用户名或密码错误"));
            }
        } else if (e instanceof DisabledException) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, CommonResultInfo.FORBIDDEN, "账户被禁用，请联系管理员"));
        } else {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, CommonResultInfo.INTERNAL_SERVER_ERROR, "登录失败，其他内部错误"));
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
        //获取已登录错误次数
        int loginFailTime = Integer.parseInt(value) + 1;
        redissonClient.getBucket(key).set(String.valueOf(loginFailTime), tokenProperties.getLoginAfterTime(), TimeUnit.MINUTES);
        if (loginFailTime >= tokenProperties.getLoginTimeLimit()) {
            redissonClient.getBucket(flagKey).set("fail", tokenProperties.getLoginAfterTime(), TimeUnit.MINUTES);
            return false;
        }
        return true;
    }
}