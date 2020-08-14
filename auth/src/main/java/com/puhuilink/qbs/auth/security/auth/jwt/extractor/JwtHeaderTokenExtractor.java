/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:43:08
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 10:43:08
 */
package com.puhuilink.qbs.auth.security.auth.jwt.extractor;

import com.puhuilink.qbs.auth.utils.SecurityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component(value = "jwtHeaderTokenExtractor")
public class JwtHeaderTokenExtractor implements TokenExtractor {

    @Override
    public String extract(HttpServletRequest request) {
        String header = request.getHeader(SecurityConstant.HEADER_PARAM);
        if (StringUtils.isBlank(header)) {
            header = request.getParameter(SecurityConstant.HEADER_PARAM);
        }
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("认证签名不合法");
        }
        return header;
    }
}
