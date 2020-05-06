/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:53:12
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 14:53:12
 */
package com.phlink.core.web.utils;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpContextUtil {

    private HttpContextUtil() {

    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
