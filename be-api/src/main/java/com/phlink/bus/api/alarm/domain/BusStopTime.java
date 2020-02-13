package com.phlink.bus.api.alarm.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusStopTime {

    @ApiModelProperty("站点id")
    private Long stopId;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("定位时间")
    private Long locationTime;
}
