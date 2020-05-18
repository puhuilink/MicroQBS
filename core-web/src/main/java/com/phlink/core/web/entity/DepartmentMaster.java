package com.phlink.core.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.core.base.constant.CommonConstant;
import com.phlink.core.web.base.PhlinkBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_department_master")
@ApiModel(value = "部门负责人")
public class DepartmentMaster extends PhlinkBaseEntity {

    @ApiModelProperty(value = "关联部门id")
    private String departmentId;

    @ApiModelProperty(value = "关联部门负责人")
    private String userId;

    @ApiModelProperty(value = "负责人类型 默认0主要 1副职")
    private Integer type = CommonConstant.MASTER_TYPE_MAIN;
}
