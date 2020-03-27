package com.phlink.core.web.security.model;

import java.io.Serializable;

public class UserPrincipal implements Serializable {

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
