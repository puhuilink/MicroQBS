/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:16:46
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:16:46
 */
package com.phlink.core.web.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phlink.core.base.utils.InheritableThreadLocalUtil;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable Exception ex) throws Exception {
        InheritableThreadLocalUtil.clean();
    }
}
