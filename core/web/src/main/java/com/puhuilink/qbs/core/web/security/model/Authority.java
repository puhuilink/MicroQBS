/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:12:00
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:12:00
 */
package com.puhuilink.qbs.core.web.security.model;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Authority {

    SYS_ADMIN(0), TENANT_ADMIN(1), CUSTOMER_USER(2), REFRESH_TOKEN(10);

    @EnumValue
    private int code;

    Authority(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Authority parse(String value) {
        Authority authority = null;
        if (value != null && value.length() != 0) {
            for (Authority current : Authority.values()) {
                if (current.name().equalsIgnoreCase(value)) {
                    authority = current;
                    break;
                }
            }
        }
        return authority;
    }
}
