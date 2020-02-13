package com.phlink.bus.api.serviceorg.domain.VO;

import com.phlink.bus.api.serviceorg.domain.enums.ServiceInvalidReasonEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ServiceInvalidateVO {

    @ApiModelProperty("学生id")
    @NotNull(message = "{required}")
    private Long studentId;

    @ApiModelProperty("失效原因")
    @NotNull(message = "{required}")
    private ServiceInvalidReasonEnum invalidReason;
}
