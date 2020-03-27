package com.phlink.core.web.security.auth.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImageLoginRequest {
    private String username;
    private String password;
    private String code;
    private String captchaId;
    private Boolean saveLogin;

    @JsonCreator
    public ImageLoginRequest(@JsonProperty("username") String username,
                             @JsonProperty("password") String password,
                             @JsonProperty("code") String code,
                             @JsonProperty("captchaId") String captchaId,
                             @JsonProperty("saveLogin") Boolean saveLogin
                             ) {
        this.username = username;
        this.password = password;
        this.code = code;
        this.captchaId = captchaId;
        this.saveLogin = saveLogin;
    }

}
