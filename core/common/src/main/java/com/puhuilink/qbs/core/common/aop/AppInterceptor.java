/*
 * @Author: sevncz.wen
 * @Date: 2020-03-26 22:54:28
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-27 15:49:18
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/aop/AppInterceptor.java
 */
package com.puhuilink.qbs.core.common.aop;

import com.puhuilink.qbs.core.base.utils.InheritableThreadLocalUtil;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable Exception ex) throws Exception {
        InheritableThreadLocalUtil.clean();
    }
}
