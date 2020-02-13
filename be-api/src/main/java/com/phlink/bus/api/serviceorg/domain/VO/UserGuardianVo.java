package com.phlink.bus.api.serviceorg.domain.VO;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.converter.TimeConverter;
import com.phlink.bus.api.common.domain.RegexpConstant;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Excel("监护人信息表")
public class UserGuardianVo {

    @NotBlank(message = "{required}", groups = {OnUpdate.class})
    @ApiModelProperty("用户id")
    private Long userId;

    @Size(min = 4, max = 10, message = "{range}")
    @ExcelField(value = "用户名")
    @ApiModelProperty("用户名")
    private String username;

    //    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
//    @Size(min = 6, max = 24, message = "{range}")
    private String password;

    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "真实姓名")
    private String realname;

    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @Pattern(regexp = RegexpConstant.REGEX_MOBILE, message = "{mobile}")
    @ExcelField(value = "手机号")
    @ApiModelProperty("手机号")
    private String mobile;

    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ExcelField(value = "状态", writeConverterExp = "0=锁定,1=有效")
    @ApiModelProperty("状态")
    private String status;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("操作时间")
    private Date modifyTime;

    @ExcelField(value = "登录次数")
    @ApiModelProperty("登录次数")
    private transient Integer loginTime;

    @ExcelField(value = "最后登录时间", writeConverter = TimeConverter.class)
    @ApiModelProperty("最后登录时间")
    private Date lastLoginTime;

    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ExcelField(value = "性别", writeConverterExp = "0=男,1=女,2=保密")
    @ApiModelProperty("性别")
    private String ssex;

    @ExcelField(value = "身份证号")
    @ApiModelProperty("身份证号")
    private String idcard;

    @ExcelField(value = "角色")
    @ApiModelProperty("角色")
    private transient String roleName;
    @ExcelField(value = "学生姓名")
    @ApiModelProperty("学生姓名")
    private transient String studentName;
}
