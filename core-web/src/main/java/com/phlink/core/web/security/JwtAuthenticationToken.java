package com.phlink.core.web.security;


import com.phlink.core.web.security.model.SecurityUser;
import com.phlink.core.web.security.model.token.RawAccessJwtToken;

public class JwtAuthenticationToken extends AbstractJwtAuthenticationToken {

    private static final long serialVersionUID = -8487219769037942225L;

    public JwtAuthenticationToken(RawAccessJwtToken unsafeToken) {
        super(unsafeToken);
    }

    public JwtAuthenticationToken(SecurityUser securityUser) {
        super(securityUser);
    }
}
