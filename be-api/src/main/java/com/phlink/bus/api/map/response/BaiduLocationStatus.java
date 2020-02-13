package com.phlink.bus.api.map.response;

import lombok.Data;

@Data
public class BaiduLocationStatus {
    private Integer fence_id;
    /**
     * unknown：未知状态
     * in：在围栏内
     * out：在围栏外
     */
    private String monitored_status;
}
