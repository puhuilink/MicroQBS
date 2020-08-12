/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:12:08
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:12:08
 */
package com.puhuilink.qbs.web.security.model;

import java.io.Serializable;

public class UserPrincipal implements Serializable {

    private static final long serialVersionUID = 6797321032949823369L;
    private final Type type;
    private final String value;
    private final Boolean saveLogin;

    public UserPrincipal(Type type, String value, Boolean saveLogin) {
        this.type = type;
        this.value = value;
        this.saveLogin = saveLogin;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Boolean getSaveLogin() {
        return saveLogin;
    }

    public enum Type {
        USER_NAME,
        MOBILE,
        EMAIL
    }

}
