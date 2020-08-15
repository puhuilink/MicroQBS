/*
 * @Author: sevncz.wen
 * @Date: 2020-08-14 11:41
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-14 11:41
 */
package com.puhuilink.qbs.auth.security.model;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @program: qbs-web
 * @description: Token状态
 * @author: sevncz.wen
 * @create: 2020-08-14 11:41
 **/
public enum TokenStatus {
    LOGIN(0), LOGOUT(1), DISABLE(2), RESET(3);

    @EnumValue
    private int code;

    TokenStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TokenStatus parse(String value) {
        TokenStatus status = null;
        if (value != null && value.length() != 0) {
            for (TokenStatus current : TokenStatus.values()) {
                if (current.name().equalsIgnoreCase(value)) {
                    status = current;
                    break;
                }
            }
        }
        return status;
    }
}
