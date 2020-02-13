package com.phlink.bus.core.iotdata.domain;

import io.rpc.core.device.EWatchInfo;
import lombok.Data;

@Data
public class EWatchLocation {
    // 设备号
    private String deviceId;
    // 时间戳
    private Long timestamp;
    // 电池电量
    private Integer batteryLevel;
    // 经度
    private Double latitude;
    // 纬度
    private Double longitude;
    // 速度
    private Integer speed;
    // 方向
    private Integer direction;

    public EWatchLocation(EWatchInfo eWatchInfo) {
        if(eWatchInfo != null) {
            this.deviceId = eWatchInfo.getDeviceId();
            this.timestamp = eWatchInfo.getTimestamp();
            this.batteryLevel = eWatchInfo.getBatteryLevel();
            this.latitude = eWatchInfo.getLatitude();
            this.longitude = eWatchInfo.getLongitude();
            this.speed = eWatchInfo.getSpeed();
            this.direction = eWatchInfo.getDirection();
        }
    }


}
