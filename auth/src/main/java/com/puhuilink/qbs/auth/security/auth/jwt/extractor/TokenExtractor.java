/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:42:57
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 10:42:57
 */
package com.puhuilink.qbs.auth.security.auth.jwt.extractor;

import javax.servlet.http.HttpServletRequest;

public interface TokenExtractor {
    String extract(HttpServletRequest request);
}
