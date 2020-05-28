/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:11:18
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:11:18
 */
package com.phlink.core.web.security.exception;

import org.springframework.security.authentication.AuthenticationServiceException;


public class AuthMethodNotSupportedException extends AuthenticationServiceException {
    private static final long serialVersionUID = 3705043083010304496L;

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
