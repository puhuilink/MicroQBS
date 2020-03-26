package com.phlink.core.web.security.auth.jwt.extractor;

import cn.hutool.core.util.StrUtil;
import com.phlink.core.web.base.constant.SecurityConstant;
import com.phlink.core.web.config.properties.PhlinkTokenProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wen
 */
@Component(value="jwtHeaderTokenExtractor")
public class JwtHeaderTokenExtractor implements TokenExtractor {
    @Autowired
    private PhlinkTokenProperties tokenProperties;

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
