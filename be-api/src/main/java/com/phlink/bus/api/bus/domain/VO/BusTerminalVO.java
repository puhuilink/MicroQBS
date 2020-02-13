package com.phlink.bus.api.bus.domain.VO;

import lombok.Data;

@Data
public class BusTerminalVO {

    /**
     * 终端标识（百度）
     */
    private String entityName;

    /**
     * 终端标识(高德)
     */
    private long tid;
}
