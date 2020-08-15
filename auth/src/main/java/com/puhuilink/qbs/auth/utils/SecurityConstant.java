package com.puhuilink.qbs.auth.utils;

public interface SecurityConstant {
    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = "QBS.CORE.5EauWCdt8IyDnbctAj6MZpb9vZSlhtRI6mLfDklojo9PMboYeWwEU0zZFdHMjm96wveuBTvy5S9yJ36osEkfvWliOU8XmrNovgAf";

    String JWT_ENCRYPTER_KEY = "QBS.KEY.tAj6MZpb9vZSlhtRI6mLfDklojo9PMboYeWwEU0zZFdHMjm96wveu";

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
    String CLAIMS_AUTHORITIES = "authorities";

    /**
     * 用户ID
     */
    String CLAIMS_USER_ID = "user_id";

    /**
     * 用户选择JWT保存时间参数头
     */
    String SAVE_LOGIN = "saveLogin";

    /**
     * 交互token前缀key
     */
    String TOKEN_PRE = "QBS_TOKEN_PRE:";

    /**
     * 用户token前缀key 单点登录使用
     */
    String USER_TOKEN = "QBS_USER_TOKEN:";

    // 验证码保存在Cookie中的字段
    String COOKIE_CAPTCHA_CODE = "$_captcha_code";
    // 验证码保存在Cookie中的时间（秒）
    Integer COOKIE_CAPTCHA_TIMEOUT = 300;

}
