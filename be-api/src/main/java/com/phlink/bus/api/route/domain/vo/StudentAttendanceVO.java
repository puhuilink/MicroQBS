package com.phlink.bus.api.route.domain.vo;

import lombok.Data;

@Data
public class StudentAttendanceVO {

    private String deviceId;
    private String machineId;
    private String longitude;
    private String latitude;
    private String status;
    private Long timestamp;
    private String busCode;

}
