package com.phlink.core.web.security.auth.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private Boolean saveLogin;

    @JsonCreator
    public LoginRequest(@JsonProperty("username") String username,
                        @JsonProperty("password") String password,
                        @JsonProperty("saveLogin") Boolean saveLogin) {
        this.username = username;
        this.password = password;
        this.saveLogin = saveLogin;
    }
}
