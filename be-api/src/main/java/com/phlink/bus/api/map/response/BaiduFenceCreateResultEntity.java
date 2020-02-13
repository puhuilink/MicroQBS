package com.phlink.bus.api.map.response;

import lombok.Data;

@Data
public class BaiduFenceCreateResultEntity {
    private Integer status;
    private String message;
    private Integer fence_id;
}
