package com.puhuilink.qbs.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.puhuilink.qbs.core.base.constant.CommonConstant;
import com.puhuilink.qbs.core.common.base.QbsBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_department_master")
@ApiModel(value = "部门负责人")
public class DepartmentMaster extends QbsBaseEntity {

    @ApiModelProperty(value = "关联部门id")
    private String departmentId;

    @ApiModelProperty(value = "关联部门负责人")
    private String userId;

    @ApiModelProperty(value = "负责人类型 默认0主要 1副职")
    private Integer type = CommonConstant.MASTER_TYPE_MAIN;
}
