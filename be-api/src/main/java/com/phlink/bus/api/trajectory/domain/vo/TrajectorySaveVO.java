package com.phlink.bus.api.trajectory.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TrajectorySaveVO {

    @ApiModelProperty(value = "车辆ID", required = true)
    @NotNull(message = "{required}")
    private Long busId;
    @ApiModelProperty(value = "轨迹名称", required = true)
    @NotBlank(message = "{required}")
    private String trname;
    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull(message = "{required}")
    private Long startTimestamp;
    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull(message = "{required}")
    private Long endTimestamp;
}
