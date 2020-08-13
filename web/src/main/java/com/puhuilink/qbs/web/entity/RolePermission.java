package com.puhuilink.qbs.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.puhuilink.qbs.core.common.base.QbsBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("t_role_permission")
@ApiModel(value = "角色权限")
public class RolePermission extends QbsBaseEntity {

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "权限id")
    private String permissionId;
}
