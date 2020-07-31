/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 13:42:51
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:12:44
 */
package com.puhuilink.qbs.core.web.security;

import com.puhuilink.qbs.core.web.security.model.SecurityUser;
import com.puhuilink.qbs.core.web.security.model.token.RawAccessJwtToken;

public class JwtAuthenticationToken extends AbstractJwtAuthenticationToken {

    private static final long serialVersionUID = -8487219769037942225L;

    public JwtAuthenticationToken(RawAccessJwtToken unsafeToken) {
        super(unsafeToken);
    }

    public JwtAuthenticationToken(SecurityUser securityUser) {
        super(securityUser);
    }
}
