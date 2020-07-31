package com.puhuilink.qbs.core.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.puhuilink.qbs.core.web.base.QbsBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_user_role")
@ApiModel(value = "用户角色")
public class UserRole extends QbsBaseEntity {

    @ApiModelProperty(value = "用户唯一id")
    private String userId;

    @ApiModelProperty(value = "角色唯一id")
    private String roleId;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色名")
    private String roleName;
}
