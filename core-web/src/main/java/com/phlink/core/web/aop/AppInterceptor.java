package com.phlink.core.web.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phlink.core.web.base.utils.InheritableThreadLocalUtil;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wen
 */
@Slf4j
public class AppInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        InheritableThreadLocalUtil.clean();
    }
}
