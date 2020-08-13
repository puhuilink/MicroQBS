/*
 * @Author: sevncz.wen
 * @Date: 2020-08-13 18:15
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-13 18:15
 */
package com.puhuilink.qbs.auth.utils;

/**
 * @program: qbs-auth
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-13 18:15
 **/
public class AuthConstants {
    // 验证码保存在Cookie中的字段
    public static final String COOKIE_CAPTCHA_CODE = "$_captcha_code";
    // 验证码保存在Cookie中的时间（秒）
    public static final Integer COOKIE_CAPTCHA_TIMEOUT = 300;
}
