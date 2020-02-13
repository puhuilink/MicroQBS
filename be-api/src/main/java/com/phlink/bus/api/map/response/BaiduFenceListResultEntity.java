package com.phlink.bus.api.map.response;

import lombok.Data;

import java.util.List;

@Data
public class BaiduFenceListResultEntity {
    private Integer status;
    private String message;
    private Integer total;
    private Integer size;
    private List<BaiduFencePointsResultEntity> fences;
}
