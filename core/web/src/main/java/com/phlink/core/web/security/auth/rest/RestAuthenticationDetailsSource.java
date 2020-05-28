/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:25:18
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:10:57
 */
package com.phlink.core.web.security.auth.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;

public class RestAuthenticationDetailsSource
        implements AuthenticationDetailsSource<HttpServletRequest, RestAuthenticationDetails> {

    @Override
    public RestAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new RestAuthenticationDetails(context);
    }
}
