package com.phlink.bus.api.route.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RouteViewVO {

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("站点名称")
    private String stopName;

    @ApiModelProperty("路线名称")
    private String routeName;

    @ApiModelProperty("站点ID")
    private Long routeId;
}
