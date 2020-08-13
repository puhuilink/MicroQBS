/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:25:28
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:11:05
 */
package com.puhuilink.qbs.auth.security.auth.rest;

import com.google.gson.Gson;
import com.puhuilink.qbs.auth.security.exception.AuthMethodNotSupportedException;
import com.puhuilink.qbs.auth.security.model.UserPrincipal;
import com.puhuilink.qbs.auth.utils.AuthConstants;
import com.puhuilink.qbs.auth.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class RestImageLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new RestAuthenticationDetailsSource();

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    public RestImageLoginProcessingFilter(String defaultFilterProcessesUrl, AuthenticationSuccessHandler successHandler,
                                          AuthenticationFailureHandler failureHandler) {
        super(defaultFilterProcessesUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
     }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            if (log.isDebugEnabled()) {
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

        String code = CookieUtil.getCookieByName(request, AuthConstants.COOKIE_CAPTCHA_CODE);
        if (StringUtils.isBlank(code)) {
            throw new BadCredentialsException("验证码过期");
        }

        if (!code.toLowerCase().equals(loginRequest.getCode().toLowerCase())) {
            log.info("验证码错误：request code:" + loginRequest.getCode() + "，code:" + code);
            throw new BadCredentialsException("验证码错误");
        }
        // 已验证清除key
        CookieUtil.deleteCookie(request, response, AuthConstants.COOKIE_CAPTCHA_CODE);

        UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.USER_NAME, loginRequest.getUsername(),
                loginRequest.getSaveLogin());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal,
                loginRequest.getPassword());
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
