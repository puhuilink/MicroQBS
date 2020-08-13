/*
 * @Author: sevncz.wen
 * @Date: 2020-08-13 18:22
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-13 18:22
 */
package com.puhuilink.qbs.auth.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * @program: qbs-web
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-13 18:22
 **/
public class CookieUtil {

    public static void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(AuthConstants.COOKIE_CAPTCHA_TIMEOUT);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }


    public static String getCookieByName(HttpServletRequest request, String name) {
        String code = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> optional = Arrays.stream(cookies)
                    .filter(c -> name.equals(c.getName())).findAny();
            if(optional.isPresent()) {
                code = optional.get().getValue();
            }
        }
        return code;
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> optional = Arrays.stream(cookies)
                    .filter(c -> name.equals(c.getName())).findAny();
            if(optional.isPresent()) {
                Cookie cookie = optional.get();
                cookie.setMaxAge(0);
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            }
        }
    }
}
