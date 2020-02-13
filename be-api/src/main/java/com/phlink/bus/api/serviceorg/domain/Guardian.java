package com.phlink.bus.api.serviceorg.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.serviceorg.domain.enums.GuardianStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_guardian")
public class  Guardian extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 家长ID，来自user表
     */
    @ApiModelProperty(value = "家长ID",example = "1L")
    private Long userId;

    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证号码")
    private String idcard;

    /**
     * 可查看学生的ID
     */
    @ApiModelProperty(value = "可查看学生的ID")
    private Long[] studentId;

    /**
     * 和孩子的关系
     */
    @ApiModelProperty(value = "和孩子的关系")
    private JSONObject relation;

    /**
     * 是否可以请假 0 否 1是
     */
    @Deprecated
    @ApiModelProperty(value = "是否可以请假")
    private transient Boolean leavePermissions;

    /**
     * 是否是主监护人0 否 1是  
     */
    @Deprecated
    @ApiModelProperty(value = "是否是主监护人")
    private transient Boolean mainGuardian;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private transient String mobile;

    @ApiModelProperty(value = "责任人姓名")
    private transient String guardianName;

    @ApiModelProperty(value = "责任人性别")
    private transient String guardianSex;

    @ApiModelProperty(value = "责任人状态")
    private transient GuardianStatusEnum status;

    @ApiModelProperty(value = "创建人",example = "1L",hidden = true)
    private Long createBy;

    @ApiModelProperty(value = "创建时间",hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作人",example = "1L",hidden = true)
    private Long modifyBy;

    @ApiModelProperty(value = "操作时间",hidden = true)
    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "登录次数",example = "1",hidden = true)
    private transient Integer loginTimes;

    @ApiModelProperty(value = "最后登录时间",hidden = true)
    private transient LocalDateTime lastLoginTime;

    public void addStudent(Long studentId) {
        if(studentId == null) {
            return;
        }
        if(this.studentId == null) {
            this.studentId = new Long[]{studentId};
        }else{
            List<Long> studentIdList = new ArrayList<>(Arrays.asList(this.studentId));
            studentIdList.add(studentId);
            this.studentId = studentIdList.stream().distinct().filter(Objects::nonNull).collect(Collectors.toList()).toArray(new Long[]{});
        }
    }
}
