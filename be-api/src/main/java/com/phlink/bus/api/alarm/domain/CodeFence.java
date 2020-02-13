package com.phlink.bus.api.alarm.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CodeFence {

    @ApiModelProperty("车架号、手环编号")
    private String code;
    @ApiModelProperty("围栏id")
    private String fenceId;
}
