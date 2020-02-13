package com.phlink.bus.api.map.response;

import lombok.Data;

import java.util.List;

@Data
public class BaiduLocationStatusResultEntity {
    private Integer status;
    private String message;
    private Integer total;
    private Integer size;
    private List<BaiduLocationStatus> monitored_statuses;

    public boolean isSuccess() {
        return 0 == status;
    }
}
