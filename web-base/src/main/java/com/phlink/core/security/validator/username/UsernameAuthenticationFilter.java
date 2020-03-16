package com.phlink.core.security.validator.username;

import com.phlink.core.security.validator.sms.SmsAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 短信登录过滤器
 */
public class UsernameAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * request中必须含有username参数
     */
    private String usernameParameter = "username";
    private String passwordParameter = "password";
    /**
     * post请求
     */
    private boolean postOnly = true;

    public UsernameAuthenticationFilter() {
        /**
         * 处理的手机验证码登录请求处理url
         */
        super(new AntPathRequestMatcher("/login/username", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //判断是是不是post请求
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //从请求中获取手机号码
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }

        username = username.trim();
        //创建SmsCodeAuthenticationToken(未认证)
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        //设置用户信息
        setDetails(request, authRequest);
        //返回Authentication实例
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取用户名
     */
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

    /**
     * 获取密码
     */
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return usernameParameter;
    }

    public void setMobileParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Mobile parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }
}