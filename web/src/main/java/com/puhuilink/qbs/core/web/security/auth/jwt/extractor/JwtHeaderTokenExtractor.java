/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:43:08
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 10:43:08
 */
package com.puhuilink.qbs.core.web.security.auth.jwt.extractor;

import javax.servlet.http.HttpServletRequest;

import com.puhuilink.qbs.core.base.constant.SecurityConstant;
import com.puhuilink.qbs.core.web.config.properties.QbsTokenProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;


@Component(value="jwtHeaderTokenExtractor")
public class JwtHeaderTokenExtractor implements TokenExtractor {
    @Autowired
    private QbsTokenProperties tokenProperties;

    @Override
    public String extract(HttpServletRequest request) {
        String header = request.getHeader(SecurityConstant.HEADER_PARAM);
        if (StrUtil.isBlank(header)) {
            header = request.getParameter(SecurityConstant.HEADER_PARAM);
        }
        Boolean notValid = StrUtil.isBlank(header) || (!tokenProperties.getRedis() && !header.startsWith(SecurityConstant.TOKEN_SPLIT));
        if (notValid) {
            throw new AuthenticationServiceException("token不合法");
        }
        return header;
    }
}
