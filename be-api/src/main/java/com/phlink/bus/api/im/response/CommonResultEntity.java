package com.phlink.bus.api.im.response;

import lombok.Data;

@Data
public class CommonResultEntity {
    private String code;
    private String msg;
    private Object result;
}
