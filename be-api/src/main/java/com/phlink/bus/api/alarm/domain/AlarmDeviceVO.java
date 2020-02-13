package com.phlink.bus.api.alarm.domain;

import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmSubTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.ProcessingStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AlarmDeviceVO {
    @ApiModelProperty(value = "设备id")
    private String deviceCode;
    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    /**
     * 处理状态
     */
    @ApiModelProperty(value = "处理状态")
    private ProcessingStatusEnum status;

    /**
     * 告警时间
     */
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 告警时间
     */
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
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

}
