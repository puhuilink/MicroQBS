package com.phlink.core.web.security.auth.jwt;

import com.phlink.core.web.security.JwtAuthenticationToken;
import com.phlink.core.web.security.model.SecurityUser;
import com.phlink.core.web.security.model.token.RawAccessJwtToken;
import com.phlink.core.web.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author wen
 */
@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final SecurityUtil securityUtil;

    @Autowired
    public JwtAuthenticationProvider(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
        SecurityUser securityUser = securityUtil.parseAccessJwtToken(rawAccessToken);
        return new JwtAuthenticationToken(securityUser);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
