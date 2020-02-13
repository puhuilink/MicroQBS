package com.phlink.bus.api.fence.domain;

import com.phlink.bus.api.fence.domain.enums.FenceTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FenceSimpleVO {

    @ApiModelProperty("电子围栏id")
    private String fenceId;

    @ApiModelProperty("电子围栏类型")
    private FenceTypeEnum fenceType;
}
