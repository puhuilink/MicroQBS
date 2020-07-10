/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:11:32
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:11:32
 */
package com.phlink.qbs.core.web.security.exception;

import org.springframework.security.authentication.CredentialsExpiredException;

public class UserPasswordExpiredException extends CredentialsExpiredException {

    private final String resetToken;

    public UserPasswordExpiredException(String msg, String resetToken) {
        super(msg);
        this.resetToken = resetToken;
    }

    public String getResetToken() {
        return resetToken;
    }

}
