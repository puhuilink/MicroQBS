package com.phlink.core.web.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 限流异常
 */
public class LoginFailLimitException extends AuthenticationException {

    private static final long serialVersionUID = -3608667856397125671L;

    public LoginFailLimitException(String msg) {
        super(msg);
    }
}