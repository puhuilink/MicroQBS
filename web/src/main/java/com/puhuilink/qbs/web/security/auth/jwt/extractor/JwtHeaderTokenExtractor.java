/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:43:08
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 10:43:08
 */
package com.puhuilink.qbs.web.security.auth.jwt.extractor;

import javax.servlet.http.HttpServletRequest;

import com.puhuilink.qbs.core.base.constant.SecurityConstant;
import com.puhuilink.qbs.web.config.properties.QbsTokenProperties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;




@Component(value="jwtHeaderTokenExtractor")
public class JwtHeaderTokenExtractor implements TokenExtractor {
    @Autowired
    private QbsTokenProperties tokenProperties;

    @Override
    public String extract(HttpServletRequest request) {
        String header = request.getHeader(SecurityConstant.HEADER_PARAM);
        if (StringUtils.isBlank(header)) {
            header = request.getParameter(SecurityConstant.HEADER_PARAM);
        }
        Boolean notValid = StringUtils.isBlank(header) || (!tokenProperties.getRedis() && !header.startsWith(SecurityConstant.TOKEN_SPLIT));
        if (notValid) {
            throw new AuthenticationServiceException("token不合法");
        }
        return header;
    }
}
