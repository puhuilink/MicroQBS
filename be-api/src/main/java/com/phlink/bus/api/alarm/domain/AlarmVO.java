package com.phlink.bus.api.alarm.domain;

import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmSubTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.ProcessingStatusEnum;
import com.phlink.bus.api.map.domain.enums.EntityTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmVO {

    @ApiModelProperty(value = "名称（车牌号或学生姓名）")
    private String entityName;

    @ApiModelProperty(value = "设备类型")
    private EntityTypeEnum entityType;

    /**
     * 告警类型
     */
    @ApiModelProperty(value = "告警类型")
    private AlarmTypeEnum alarmType;

    /**
     * 告警子类
     */
    @ApiModelProperty(value = "告警子类")
    private AlarmSubTypeEnum alarmSubType;

    /**
     * 告警级别
     */
    @ApiModelProperty(value = "告警级别")
    private AlarmLevelEnum alarmLevel;

    /**
     * 处理状态
     */
    @ApiModelProperty(value = "处理状态")
    private ProcessingStatusEnum status;

    /**
     * 告警时间
     */
    @ApiModelProperty(value = "告警时间")
    private LocalDateTime createTime;
}
