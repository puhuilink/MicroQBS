package com.phlink.core.web.security;

import cn.hutool.core.util.StrUtil;
import com.phlink.core.web.security.exception.LoginFailLimitException;
import com.phlink.core.web.entity.User;
import com.phlink.core.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String flagKey = "loginFailFlag:" + username;
        RBucket<String> bucket = redissonClient.getBucket(flagKey, new StringCodec());
        String value = bucket.get();
        Long timeRest = bucket.remainTimeToLive();
        if(StrUtil.isNotBlank(value)){
            Duration duration = Duration.ofMillis(timeRest);
            //超过限制次数
            throw new LoginFailLimitException("登录错误次数超过限制，请"+duration.toMinutes()+"分钟后再试");
        }
        User user = userService.getByUsername(username);
        return new SecurityUserDetails(user);
    }

    public UserDetails loadUserByMobile(String mobile) {

        String flagKey = "loginFailFlag:" + mobile;
        RBucket<String> bucket = redissonClient.getBucket(flagKey, new StringCodec());
        String value = bucket.get();
        Long timeRest = bucket.remainTimeToLive();
        if(StrUtil.isNotBlank(value)){
            Duration duration = Duration.ofMillis(timeRest);
            //超过限制次数
            throw new LoginFailLimitException("登录错误次数超过限制，请"+duration.toMinutes()+"分钟后再试");
        }
        User user = userService.getByMobile(mobile);
        return new SecurityUserDetails(user);
    }
}