package com.phlink.bus.api.route.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RouteBusVO {
    @ApiModelProperty("路线id")
    private String routeId;
    @ApiModelProperty("车架号")
    private String busCode;
}
