/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:51:17
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 14:51:17
 */
package com.phlink.qbs.core.web.security.model.token;

import java.io.Serializable;

public interface JwtToken extends Serializable {
    String getToken();
}
