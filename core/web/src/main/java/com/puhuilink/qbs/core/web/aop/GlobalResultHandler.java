/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:16:50
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/aop/GlobalResultHandler.java
 */
package com.puhuilink.qbs.core.web.aop;

import com.puhuilink.qbs.core.base.utils.ResultUtil;
import com.puhuilink.qbs.core.base.vo.Result;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalResultHandler implements ResponseBodyAdvice {

    private static String[] ignores = new String[]{
        // swagger ui
        "/swagger-resources/**",
        "/swagger-ui.html",
        "/v2/api-docs",
        "/webjars/**"
    };

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getMethod().getReturnType() != Result.class;
    }

    private boolean ignoring(String uri) {
        for (String string : ignores) {
            if (uri.contains(string)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //判断url是否需要拦截
        if (this.ignoring(request.getURI().toString())) {
            return body;
        }
        if (body == null) {
            return ResultUtil.success("OK");
        }
        if (body instanceof Result) {
            return body;
        }
        if (body instanceof String) {
            return ResultUtil.data(body);
        }
        if (body instanceof Resource) {
            return body;
        }
        if (body instanceof Byte) {
            return body;
        }
        return ResultUtil.data(body);
    }

}
