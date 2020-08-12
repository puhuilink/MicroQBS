/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:11:28
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:11:28
 */
package com.puhuilink.qbs.web.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 限流异常
 *
 * @author wen
 */
public class LoginFailLimitException extends AuthenticationException {

    private static final long serialVersionUID = -3608667856397125671L;

    public LoginFailLimitException(String msg) {
        super(msg);
    }
}
