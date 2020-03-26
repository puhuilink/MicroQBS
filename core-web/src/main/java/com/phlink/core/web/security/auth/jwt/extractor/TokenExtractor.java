package com.phlink.core.web.security.auth.jwt.extractor;

import javax.servlet.http.HttpServletRequest;

public interface TokenExtractor {
    public String extract(HttpServletRequest request);
}
