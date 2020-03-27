package com.phlink.core.web.security.auth.rest;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.phlink.core.web.base.enums.ResultCode;
import com.phlink.core.web.base.utils.ResponseUtil;
import com.phlink.core.web.security.exception.AuthMethodNotSupportedException;
import com.phlink.core.web.security.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestImageLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new RestAuthenticationDetailsSource();

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final RedissonClient redissonClient;

    public RestImageLoginProcessingFilter(String defaultFilterProcessesUrl, AuthenticationSuccessHandler successHandler,
                                          AuthenticationFailureHandler failureHandler, RedissonClient redissonClient) {
        super(defaultFilterProcessesUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.redissonClient = redissonClient;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            if(log.isDebugEnabled()) {
                log.debug("请求方法不支持. Request method: " + request.getMethod());
            }
            throw new AuthMethodNotSupportedException("请求方法不支持");
        }

        ImageLoginRequest loginRequest;
        try {
            loginRequest = new Gson().fromJson(request.getReader(), ImageLoginRequest.class);
        } catch (Exception e) {
            throw new AuthenticationServiceException("错误的登录请求");
        }

        if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("请提供用户名和密码");
        }
        if (StringUtils.isBlank(loginRequest.getCaptchaId()) || StringUtils.isBlank(loginRequest.getCode())) {
            throw new BadCredentialsException("请提供验证码");
        }

        RBucket<String> bucket = redissonClient.getBucket(loginRequest.getCaptchaId(), new StringCodec());
        String redisCode = bucket.get();
        if (StrUtil.isBlank(redisCode)) {
            throw new BadCredentialsException("验证码过期");
        }

        if (!redisCode.toLowerCase().equals(loginRequest.getCode().toLowerCase())) {
            log.info("验证码错误：code:" + loginRequest.getCode() + "，redisCode:" + redisCode);
            throw new BadCredentialsException("验证码错误");
        }
        // 已验证清除key
        redissonClient.getKeys().delete(loginRequest.getCaptchaId());

        UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.USER_NAME, loginRequest.getUsername());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, loginRequest.getPassword());
        token.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
