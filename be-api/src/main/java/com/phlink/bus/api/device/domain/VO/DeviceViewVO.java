package com.phlink.bus.api.device.domain.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceViewVO {

    @ApiModelProperty("学生id")
    private Long studentId;

    @ApiModelProperty("设备标识码")
    private String deviceCode;

    @ApiModelProperty("绑定状态")
    private Boolean bindingStatus;
}
