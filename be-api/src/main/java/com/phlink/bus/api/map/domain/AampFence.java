package com.phlink.bus.api.map.domain;

import lombok.Data;

@Data
public class AampFence {

    /**
     * 电子围栏
     */

    private String name;
    private String center;
    private String radius;
    private String points;
    private String alert_condition;
    private String repeat;
}
