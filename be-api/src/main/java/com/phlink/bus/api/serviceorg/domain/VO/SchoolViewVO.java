package com.phlink.bus.api.serviceorg.domain.VO;

import com.phlink.bus.api.serviceorg.domain.enums.SchoolFenceStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SchoolViewVO {

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区/县")
    private String country;

    @ApiModelProperty("围栏配置状态")
    private SchoolFenceStatus fenceStatus;

    @ApiModelProperty("所属组织")
    private Long deptId;

    @ApiModelProperty("学校名称")
    private String schoolName;

}
