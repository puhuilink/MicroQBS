/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:43:04
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 10:43:04
 */
package com.puhuilink.qbs.auth.security.auth.jwt.extractor;

import javax.servlet.http.HttpServletRequest;


import com.puhuilink.qbs.core.base.constant.SecurityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component(value="jwtQueryTokenExtractor")
public class JwtQueryTokenExtractor implements TokenExtractor {

    @Override
    public String extract(HttpServletRequest request) {
        String token = null;
        if (request.getParameterMap() != null && !request.getParameterMap().isEmpty()) {
            String[] tokenParamValue = request.getParameterMap().get(SecurityConstant.QUERY_PARAM);
            if (tokenParamValue != null && tokenParamValue.length == 1) {
                token = tokenParamValue[0];
            }
        }
        if (StringUtils.isBlank(token)) {
            throw new AuthenticationServiceException("Authorization query parameter cannot be blank!");
        }

        return token;
    }
}
