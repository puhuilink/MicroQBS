package com.phlink.bus.api.alarm.domain;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RouteSwitchVO {

    @ApiModelProperty("id")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long id;

    @ApiModelProperty("路线开关")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Boolean routeSwitch;

    @ApiModelProperty("站点开关")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Boolean stopSwitch;

    @ApiModelProperty(value = "车辆开关")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Boolean busSwitch;

    @ApiModelProperty(value = "周末开关")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Boolean weekendSwitch;

}
