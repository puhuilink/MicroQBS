/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:25:11
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-06 14:50:50
 */
package com.puhuilink.qbs.auth.security.auth.rest;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Data
public class RestAuthenticationDetails implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String clientAddress;

    public RestAuthenticationDetails(HttpServletRequest request) {
        this.clientAddress = getClientIP(request);
    }

    private static String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
