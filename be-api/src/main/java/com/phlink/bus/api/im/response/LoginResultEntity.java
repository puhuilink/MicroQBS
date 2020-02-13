package com.phlink.bus.api.im.response;

import lombok.Data;

import java.util.Map;

@Data
public class LoginResultEntity {
    private Integer code;
    private Map<String, Object> result;
    private String token;
}
