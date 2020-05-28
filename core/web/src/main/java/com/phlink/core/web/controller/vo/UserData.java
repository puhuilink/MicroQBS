package com.phlink.core.web.controller.vo;

import java.util.List;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.phlink.core.base.constant.CommonConstant;
import com.phlink.core.web.entity.Permission;
import com.phlink.core.web.entity.Role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ColumnWidth(30)
@HeadRowHeight(15)
@ContentRowHeight(20)
@Data
public class UserData {

    @ExcelProperty("用户名")
    @ApiModelProperty(value = "用户名")
    private String username;

    @ExcelProperty("昵称")
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ExcelProperty("名字")
    @ApiModelProperty(value = "名字")
    private String realname;

    @ExcelProperty("手机")
    @ApiModelProperty(value = "手机")
    private String mobile;

    @ExcelProperty("邮件")
    @ApiModelProperty(value = "邮件")
    private String email;

    @ExcelProperty("省市县地址")
    @ApiModelProperty(value = "省市县地址")
    private String address;

    @ExcelProperty("街道地址")
    @ApiModelProperty(value = "街道地址")
    private String street;

    @ExcelProperty("性别")
    @ApiModelProperty(value = "性别")
    private String sex;

    @ExcelProperty(value = "用户类型", converter = UserTypeConverter.class)
    @ApiModelProperty(value = "用户类型 0普通用户 1管理员")
    private Integer type = CommonConstant.USER_TYPE_NORMAL;

    @ExcelProperty(value = "用户状态", converter = UserStatusConverter.class)
    @ApiModelProperty(value = "状态 默认0正常 -1拉黑")
    private Integer status = CommonConstant.USER_STATUS_NORMAL;

    @ExcelProperty("描述/详情/备注")
    @ApiModelProperty(value = "描述/详情/备注")
    private String description;

    @ExcelIgnore
    @ApiModelProperty(value = "所属部门id")
    private String departmentId;

    @ExcelProperty("所属部门")
    @ApiModelProperty(value = "所属部门名称")
    private String departmentTitle;

    @ExcelIgnore
    @ApiModelProperty(value = "用户拥有角色")
    private List<Role> roles;

    @ExcelIgnore
    @ApiModelProperty(value = "用户拥有的权限")
    private List<Permission> permissions;

    @ExcelIgnore
    @ApiModelProperty(value = "导入数据时使用")
    private Integer defaultRole;
}
