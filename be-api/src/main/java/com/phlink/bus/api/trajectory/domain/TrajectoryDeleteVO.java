package com.phlink.bus.api.trajectory.domain;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "删除轨迹对象")
@Data
public class TrajectoryDeleteVO {

    @ApiModelProperty("id")
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long id;

    @ApiModelProperty("终端id(高德)")
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long tid;

    @ApiModelProperty("轨迹id(高德)")
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Long trid;
}
