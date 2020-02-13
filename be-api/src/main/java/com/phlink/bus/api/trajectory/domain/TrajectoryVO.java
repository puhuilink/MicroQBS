package com.phlink.bus.api.trajectory.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrajectoryVO {

    @ApiModelProperty("车牌号码")
    private String numberPlate;

    @ApiModelProperty("车架号")
    private String busCode;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("车辆ID")
    private Long busId;
}
