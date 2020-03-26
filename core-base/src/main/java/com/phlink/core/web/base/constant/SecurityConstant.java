package com.phlink.core.web.base.constant;

public interface SecurityConstant {

    /**
     * token分割
     */
    String TOKEN_SPLIT = "Bearer ";

    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = "phlink";

    String TOKEN_ISSUER = "SYSTEM";

    /**
     * token参数头
     */
    String HEADER_PARAM = "accessToken";
    /**
     * 参数中的token
     */
    String QUERY_PARAM = "token";

    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";

    /**
     * 用户选择JWT保存时间参数头
     */
    String SAVE_LOGIN = "saveLogin";

    /**
     * 交互token前缀key
     */
    String TOKEN_PRE = "PHLINK_TOKEN_PRE:";

    /**
     * 用户token前缀key 单点登录使用
     */
    String USER_TOKEN = "PHLINK_USER_TOKEN:";
}
