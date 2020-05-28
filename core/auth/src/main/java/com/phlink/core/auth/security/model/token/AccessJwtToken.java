/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:51:13
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:11:41
 */
package com.phlink.core.web.security.model.token;

public final class AccessJwtToken implements JwtToken {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String rawToken;

    public AccessJwtToken(final String token) {
        this.rawToken = token;
    }

    @Override
    public String getToken() {
        return this.rawToken;
    }

}
