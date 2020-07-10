/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:24:41
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:10:23
 */
package com.puhuilink.qbs.core.web.security.auth.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.web.utils.ResponseUtil;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("用户信息校验失败，禁止访问系统");
        ResponseUtil.out(response, ResponseUtil.resultMap(false, ResultCode.FORBIDDEN));
    }

}
