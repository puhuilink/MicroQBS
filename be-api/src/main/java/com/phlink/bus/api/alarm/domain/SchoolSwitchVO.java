package com.phlink.bus.api.alarm.domain;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SchoolSwitchVO {

    @ApiModelProperty("id")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long id;

    @ApiModelProperty("周末开关")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Boolean weekendSwitch;

    @ApiModelProperty("手环开关")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Boolean deviceSwitch;
}
