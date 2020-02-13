package com.phlink.bus.api.alarm.domain;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class InvalidDateVO {

    /**
     * 失效开始时间
     */
    @ApiModelProperty(value = "失效开始时间")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private LocalDate invalidStartDate;

    /**
     * 失效结束时间
     */
    @ApiModelProperty(value = "失效结束时间")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private LocalDate invalidEndDate;
}
