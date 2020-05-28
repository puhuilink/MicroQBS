/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:24:16
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:10:12
 */
package com.phlink.core.web.security.auth.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phlink.core.base.annotation.SystemLogTrace;
import com.phlink.core.base.enums.LogType;
import com.phlink.core.base.enums.ResultCode;
import com.phlink.core.web.security.model.SecurityUser;
import com.phlink.core.web.security.model.token.AccessJwtToken;
import com.phlink.core.web.utils.ResponseUtil;
import com.phlink.core.web.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    @SystemLogTrace(description = "登录系统", type = LogType.LOGIN)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        log.info("{}登录成功", securityUser.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = securityUtil.getAccessJwtToken(securityUser.getUsername(), securityUser.getSaveLogin());
        AccessJwtToken accessToken = new AccessJwtToken(token);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken.getToken());
        tokenMap.put("refreshToken", "待实现");
        tokenMap.put("expireTime", "待实现");
        ResponseUtil.out(response, ResponseUtil.resultMap(true, ResultCode.SUCCESS.getCode(), "登录成功", tokenMap));
        clearAuthenticationAttributes(request);
    }

}
