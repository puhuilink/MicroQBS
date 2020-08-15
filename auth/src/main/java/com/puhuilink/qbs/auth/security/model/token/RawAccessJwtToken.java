/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:51:20
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 14:51:20
 */
package com.puhuilink.qbs.auth.security.model.token;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RawAccessJwtToken implements JwtToken {

    private static final long serialVersionUID = -797397445703066079L;

    private String token;

    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }
}
