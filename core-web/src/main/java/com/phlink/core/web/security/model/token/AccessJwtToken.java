package com.phlink.core.web.security.model.token;


public final class AccessJwtToken implements JwtToken {
    private final String rawToken;

    public AccessJwtToken(final String token) {
        this.rawToken = token;
    }

    @Override
    public String getToken() {
        return this.rawToken;
    }

}
