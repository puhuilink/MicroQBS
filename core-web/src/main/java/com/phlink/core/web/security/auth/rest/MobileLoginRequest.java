package com.phlink.core.web.security.auth.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MobileLoginRequest {
    private String mobile;
    private String code;

    @JsonCreator
    public MobileLoginRequest(@JsonProperty("mobile") String mobile, @JsonProperty("code") String code) {
        this.mobile = mobile;
        this.code = code;
    }

}
