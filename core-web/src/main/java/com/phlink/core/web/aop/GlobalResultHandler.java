/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:16:50
 * @FilePath: /phlink-common-framework/core-web/src/main/java/com/phlink/core/web/aop/GlobalResultHandler.java
 */
package com.phlink.core.web.aop;

import com.phlink.core.base.utils.ResultUtil;
import com.phlink.core.base.vo.Result;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages = { "com.phlink" })
public class GlobalResultHandler implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getMethod().getReturnType() != Result.class;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Result) {
            return body;
        }
        if (body instanceof String) {
            return ResultUtil.data(body);
        }
        if (body == null) {
            return ResultUtil.success("OK");
        }
        if (body instanceof Resource) {
            return body;
        }
        return ResultUtil.data(body);
    }

}
