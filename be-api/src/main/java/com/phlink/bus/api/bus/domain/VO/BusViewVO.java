package com.phlink.bus.api.bus.domain.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusViewVO {

    @ApiModelProperty("车牌号")
    private String numberPlate;

    @ApiModelProperty("车架号")
    private String busCode;

    @ApiModelProperty("所属部门")
    private Long deptId;

    @ApiModelProperty("dvr编号")
    private String dvrCode;
}
