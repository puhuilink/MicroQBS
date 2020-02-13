package com.phlink.bus.api.map.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Points {

    @ApiModelProperty("坐标")
    private String location;

    @ApiModelProperty("时间")
    private Long locatetime;

    @ApiModelProperty("定位精度")
    private double accuracy;

    @ApiModelProperty("速度")
    private double speed;

    @ApiModelProperty("方向")
    private double direction;

    @ApiModelProperty("高度")
    private double height;

}
