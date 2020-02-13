package com.phlink.bus.api.leave.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LeaveVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生id")
    private Long studentId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("请假人手机号")
    private String mobile;

    @ApiModelProperty("学校名称")
    private String schoolName;
}
