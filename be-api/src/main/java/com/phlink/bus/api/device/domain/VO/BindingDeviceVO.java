package com.phlink.bus.api.device.domain.VO;

import com.phlink.bus.api.common.annotation.DistributedLockParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BindingDeviceVO {

    @DistributedLockParam(name = "deviceCode")
    @ApiModelProperty("设备识别码")
    private String deviceCode;

    @DistributedLockParam(name = "studentId")
    @ApiModelProperty("学生id")
    private Long studentId;
}
