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
@Excel("游客信息表")
public class UserVisitorVo {

    @NotBlank(message = "{required}", groups = {OnUpdate.class})
    @ApiModelProperty("用户id")
    private Long userId;

    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @Pattern(regexp = RegexpConstant.REGEX_MOBILE, message = "{mobile}")
    @ExcelField(value = "手机号")
    @ApiModelProperty("手机号")
    private String mobile;

    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ExcelField(value = "状态", writeConverterExp = "0=锁定,1=有效")
    @ApiModelProperty("状态")
    private String status;

    @ExcelField(value = "注册时间", writeConverter = TimeConverter.class)
    @ApiModelProperty("注册时间")
    private Date createTime;

    @ExcelField(value = "登录次数")
    @ApiModelProperty("登录次数")
    private transient Integer loginTime;

    @ExcelField(value = "最后登录时间", writeConverter = TimeConverter.class)
    @ApiModelProperty("最后登录时间")
    private Date lastLoginTime;

    @ExcelField(value = "角色")
    @ApiModelProperty("角色")
    private transient String roleName;
}
