package com.phlink.qbs.core.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.qbs.core.web.base.PhlinkBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_role_department")
@ApiModel(value = "角色部门")
public class RoleDepartment extends PhlinkBaseEntity {

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "部门id")
    private String departmentId;
}
