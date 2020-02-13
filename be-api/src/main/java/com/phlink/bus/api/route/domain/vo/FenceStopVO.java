package com.phlink.bus.api.route.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FenceStopVO {

    @ApiModelProperty("站点围栏id")
    private Long id;

    @ApiModelProperty("站点围栏名称")
    private String fenceName;

    @ApiModelProperty("站点坐标")
    private String center;
}
