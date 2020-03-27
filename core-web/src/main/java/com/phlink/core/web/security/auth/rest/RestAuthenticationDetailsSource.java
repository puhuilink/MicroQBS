package com.phlink.core.web.security.auth.rest;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wen
 */
public class RestAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, RestAuthenticationDetails> {

    @Override
    public RestAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new RestAuthenticationDetails(context);
    }
}
