/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:24:09
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 10:24:09
 */
package com.puhuilink.qbs.web.security.auth.jwt;

import com.puhuilink.qbs.web.security.JwtAuthenticationToken;
import com.puhuilink.qbs.web.security.model.SecurityUser;
import com.puhuilink.qbs.web.security.model.token.RawAccessJwtToken;
import com.puhuilink.qbs.web.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
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
