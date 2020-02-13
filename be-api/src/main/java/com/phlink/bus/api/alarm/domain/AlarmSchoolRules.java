package com.phlink.bus.api.alarm.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 告警规则
 *
 * @author ZHOUY
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_alarm_school_rules")
public class AlarmSchoolRules extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 失效开始时间
     */
    @ApiModelProperty(value = "失效开始时间")
    private LocalDate invalidStartDate;

    /**
     * 失效结束时间
     */
    @ApiModelProperty(value = "失效结束时间")
    private LocalDate invalidEndDate;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Long modifyBy;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime modifyTime;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 学校id
     */
    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    /**
     * 围栏ID
     */
    @ApiModelProperty(value = "围栏ID")
    private Long fenceId;

    /**
     * 手环开关
     */
    @ApiModelProperty(value = "手环开关")
    private Boolean deviceSwitch;

    /**
     * 周末开关
     */
    @ApiModelProperty(value = "周末开关")
    private Boolean weekendSwitch;

    /**
     * 生效时间段
     */
    @ApiModelProperty(value = "生效时间段")
    private transient JSON effectiveTime;

    @ApiModelProperty(value = "学校名称")
    private transient String schoolName;
    @ApiModelProperty(value = "围栏名称")
    private transient String fenceName;
    @ApiModelProperty(value = "生效时间")
    private transient JSONArray jobs;
}
