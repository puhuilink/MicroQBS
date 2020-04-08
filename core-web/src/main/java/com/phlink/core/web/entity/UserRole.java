package com.phlink.core.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.core.web.base.PhlinkBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wen
 */
@Data
@TableName("t_user_role")
@ApiModel(value = "用户角色")
public class UserRole extends PhlinkBaseEntity {

    @ApiModelProperty(value = "用户唯一id")
    private String userId;

    @ApiModelProperty(value = "角色唯一id")
    private String roleId;

    @TableField(exist=false)
    @ApiModelProperty(value = "角色名")
    private String roleName;
}
