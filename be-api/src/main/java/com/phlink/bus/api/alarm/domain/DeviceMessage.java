package com.phlink.bus.api.alarm.domain;

import lombok.Data;

@Data
public class DeviceMessage {
    private Double longitude;
    private Double latitude;
    private String deviceId;
    private Long timestamp;
    private Integer batteryLevel;
    private Integer speed;
    private Integer direction;
}
