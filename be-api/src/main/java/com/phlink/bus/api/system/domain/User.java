package com.phlink.bus.api.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.annotation.DistributedLockParam;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.converter.TimeConverter;
import com.phlink.bus.api.common.domain.RegexpConstant;
import com.phlink.bus.api.system.domain.enums.UserTypeEnum;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_user")
@Excel("用户信息表")
public class User implements Serializable {

    private static final long serialVersionUID = -4852732617765810959L;
    /**
     * 账户状态
     */
    public static final String STATUS_VALID = "1";

    public static final String STATUS_LOCK = "0";

    public static final String DEFAULT_AVATAR = "default.jpg";

    /**
     * 性别
     */
    public static final String SEX_MALE = "0";

    public static final String SEX_FEMALE = "1";

    public static final String SEX_UNKNOW = "2";


    // 默认密码
    public static final String DEFAULT_PASSWORD = "1234qwer";
    public static final String IM_DEFAULT_PASSWORD = "12345678";


    @NotNull(message = "{required}", groups = {OnUpdate.class})
    @TableId(value = "USER_ID", type = IdType.ID_WORKER)
    @ApiModelProperty("用户id")
    private Long userId;

    @DistributedLockParam(name = "username")
//    @Size(min = 1, max = 10, message = "{range}")
    @ExcelField(value = "用户名")
    @ApiModelProperty("用户名")
    private String username;

    //    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
//    @Size(min = 6, max = 24, message = "{range}")
    private String password;

    @Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z0-9]+$", message = "{invalid}", groups = {OnAdd.class, OnUpdate.class})
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "真实姓名")
    @ApiModelProperty("真实姓名")
    private String realname;

    @ApiModelProperty("部门id")
    private Long deptId;

    @ExcelField(value = "部门")
    @ApiModelProperty("部门")
    private transient String deptName;

    @Size(max = 50, message = "{noMoreThan}")
    @Email(message = "{email}")
    @ExcelField(value = "邮箱")
    @ApiModelProperty("邮箱")
    private String email;

    @DistributedLockParam(name = "mobile")
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
    @ExcelField(value = "性别", readConverterExp = "男=0,女=1,保密=2", writeConverterExp = "0=男,1=女,2=保密", options = SexOptions.class)
    @ApiModelProperty("性别")
    private String ssex;

    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "个人描述")
    @ApiModelProperty("个人描述")
    private String description;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("用户类型")
    private UserTypeEnum userType;

    @ApiModelProperty("是否在即时通注册过")
    private Boolean im;

//    @ExcelField(value = "身份证号")
    @ApiModelProperty("身份证号")
    private String idcard;

    @ApiModelProperty("极光推送ID")
    private String registrationId;

    @ApiModelProperty("是否检查了身份")
    private Boolean checkIdentify;

    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ApiModelProperty("角色id")
    private transient String roleId;

    @ExcelField(value = "角色")
    @ApiModelProperty("角色")
    private transient String roleName;
    private transient String studentName;

    // 排序字段
    private transient String sortField;

    // 排序规则 ascend 升序 descend 降序
    private transient String sortOrder;

    private transient String id;

    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * shiro-redis v3.1.0 必须要有 getAuthCacheKey()或者 getId()方法
     * # Principal id field name. The field which you can get unique id to identify this principal.
     * # For example, if you use UserInfo as Principal class, the id field maybe userId, userName, email, etc.
     * # Remember to add getter to this id field. For example, getUserId(), getUserName(), getEmail(), etc.
     * # Default value is authCacheKey or id, that means your principal object has a method called "getAuthCacheKey()" or "getId()"
     *
     * @return userId as Principal id field name
     */
    public Long getAuthCacheKey() {
        return userId;
    }
}
