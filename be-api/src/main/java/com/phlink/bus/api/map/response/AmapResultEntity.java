package com.phlink.bus.api.map.response;

import lombok.Data;

@Data
public class AmapResultEntity {
    private Integer errcode;
    private String errmsg;
    private String errdetail;
    private Object data;
}
