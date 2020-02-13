package com.phlink.bus.api.common.handler;

import com.phlink.bus.api.common.domain.BusApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.phlink.bus.api")
public class GlobalResponseHandler implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getMethod().getReturnType() != BusApiResponse.class;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        if (body instanceof BusApiResponse || body instanceof String) {
//            return body;
//        }
//        if (body == null) {
//            if(returnType.getParameterType().equals(BusApiResponse.class)) {
//                return new BusApiResponse().message("OK");
//            }
//            try {
//                return returnType.getParameterType().newInstance();
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
        return body;
    }

}
