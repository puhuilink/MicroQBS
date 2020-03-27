package com.phlink.core.web.security.exception;

import org.springframework.security.authentication.CredentialsExpiredException;

/**
 * @author wen
 */
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
