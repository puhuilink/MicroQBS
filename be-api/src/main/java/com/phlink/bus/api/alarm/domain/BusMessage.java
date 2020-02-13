package com.phlink.bus.api.alarm.domain;

import lombok.Data;

@Data
public class BusMessage {

    private Double longitude;
    private Double latitude;
    private String busId;
    private Long timestamp;
    private Float speed;
    private Float direction;
}
