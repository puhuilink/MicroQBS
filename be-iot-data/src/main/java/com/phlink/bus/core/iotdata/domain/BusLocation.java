package com.phlink.bus.core.iotdata.domain;

import io.rpc.core.device.BusInfo;
import lombok.Data;

@Data
public class BusLocation {
    // 设备号
    private String busId;
    // 时间戳
    private Long timestamp;
    // 经度
    private Double latitude;
    // 纬度
    private Double longitude;
    // 速度
    private Integer speed;
    // 方向
    private Integer direction;

    public BusLocation(BusInfo busInfo) {
        if(busInfo != null) {
            this.busId = busInfo.getBusId();
            this.timestamp = busInfo.getTimestamp();
            this.latitude = busInfo.getLatitude();
            this.longitude = busInfo.getLongitude();
            this.speed = busInfo.getSpeed();
            this.direction = busInfo.getDirection();
        }
    }
}
