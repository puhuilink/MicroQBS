package com.phlink.bus.api.fence.domain;

import com.phlink.bus.api.fence.domain.enums.RelationTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FenceViewVO {

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("关联类型(1:学校，2:路线)")
    private RelationTypeEnum relationType;

    /**
     * 车牌号
     */
    @ApiModelProperty("车牌号")
    private String numberPlate;

    /**
     * 车架号
     */
    @ApiModelProperty("车架号")
    private String busCode;

    /**
     * 路线名称
     */
    @ApiModelProperty("路线名称")
    private String routeName;

}
