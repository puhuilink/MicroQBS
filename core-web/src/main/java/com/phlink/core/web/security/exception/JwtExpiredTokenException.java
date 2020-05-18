/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:11:23
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:11:23
 */
package com.phlink.core.web.security.exception;

import com.phlink.core.web.security.model.token.JwtToken;

import org.springframework.security.core.AuthenticationException;

public class JwtExpiredTokenException extends AuthenticationException {
    private static final long serialVersionUID = -5959543783324224864L;

    private JwtToken token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
