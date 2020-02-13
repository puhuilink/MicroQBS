package com.phlink.bus.api.alarm.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CodeRule {

    @ApiModelProperty("车架号、手环编号")
    private String code;
    @ApiModelProperty("规则id")
    private String id;
}
