/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:24:26
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:10:08
 */
package com.puhuilink.qbs.auth.security.auth.jwt;

import com.puhuilink.qbs.auth.config.properties.QbsTokenProperties;
import com.puhuilink.qbs.auth.security.auth.rest.LoginRequest;
import com.puhuilink.qbs.auth.utils.ResponseUtil;
import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.utils.InheritableThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private QbsTokenProperties tokenProperties;

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
            ResponseUtil.out(response, ResponseUtil.resultMap(false, ResultCode.INTERNAL_SERVER_ERROR, "用户名或密码错误"));
        } else if (e instanceof DisabledException) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, ResultCode.FORBIDDEN, "账户被禁用，请联系管理员"));
        } else {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, ResultCode.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }
}
