package com.phlink.core.web.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.core.base.constant.CommonConstant;
import com.phlink.core.web.base.PhlinkBaseEntity;
import com.phlink.core.web.security.model.Authority;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wen
 */
@Data
@TableName("t_user")
@ApiModel(value = "用户")
public class User extends PhlinkBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "名字")
    private String realname;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "邮件")
    private String email;

    @ApiModelProperty(value = "省市县地址")
    private String address;

    @ApiModelProperty(value = "街道地址")
    private String street;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "密码强度")
    private String passStrength;

    @ApiModelProperty(value = "用户头像")
    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;

    @ApiModelProperty(value = "用户类型 0普通用户 1管理员")
    private Integer type = CommonConstant.USER_TYPE_NORMAL;

    @ApiModelProperty(value = "状态 默认0正常 -1拉黑")
    private Integer status = CommonConstant.USER_STATUS_NORMAL;

    @ApiModelProperty(value = "描述/详情/备注")
    private String description;

    @ApiModelProperty(value = "所属部门id")
    private String departmentId;

    @TableField(exist = false)
    @ApiModelProperty(value = "所属部门名称")
    private String departmentTitle;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户拥有角色")
    private List<Role> roles;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户拥有的权限")
    private List<Permission> permissions;

    @TableField(exist = false)
    @ApiModelProperty(value = "导入数据时使用")
    private Integer defaultRole;

    private Authority authority;

    public User(User user) {
        if (user != null) {
            this.setId(user.getId());
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.mobile = user.getMobile();
            this.departmentId = user.getDepartmentId();
        }
    }

    public User() {

    }

}
