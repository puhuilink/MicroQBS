package com.phlink.bus.api.alarm.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmSubTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.ProcessingStatusEnum;
import com.phlink.bus.api.common.converter.LocalDateTimeConverter;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 设备(手环)记录表
 *
 * @author zhouyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_alarm_device")
@Excel("设备告警")
public class AlarmDevice extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    @ExcelField(value = "设备编号")
    @ApiModelProperty(value = "设备id")
    private String deviceCode;
    @ExcelField(value = "学生姓名")
    @ApiModelProperty(value = "学生姓名")
    private transient String studentName;

    @ExcelField(value = "学校名称")
    @ApiModelProperty(value = "学校名称")
    private transient String schoolName;
    /**
     * 告警类型
     */
    @ExcelField(value = "告警类型", writeConverterExp = "ROUTE=路线告警,STOP=站点告警,SCHOOL=学校告警,BUS=车辆告警")
    @ApiModelProperty(value = "告警类型")
    private AlarmTypeEnum alarmType;

    /**
     * 告警子类
     */
    @ExcelField(value = "告警子类", writeConverterExp = "DIVERGE=偏离路线,SPEEDING=车辆超速,DELAY=站点迟到,MONITORED=监控设备,LEAVE_SCHOOL=异常出校")
    @ApiModelProperty(value = "告警子类")
    private AlarmSubTypeEnum alarmSubType;

    /**
     * 告警级别
     */
    @ExcelField(value = "告警级别", writeConverterExp = "SLIGHT=轻微,LOW=低,MIDDLE=中,DELAY=高")
    @ApiModelProperty(value = "告警级别")
    private AlarmLevelEnum alarmLevel;

    /**
     * 处理状态
     */
    @ExcelField(value = "处理状态", writeConverterExp = "UNPROCESSED=未处理,PROCESSED=系统已处理,MANUAL=人工已处理")
    @ApiModelProperty(value = "处理状态")
    private ProcessingStatusEnum status;

    /**
     * 告警时间
     */
    @ExcelField(value = "告警时间", writeConverter = LocalDateTimeConverter.class)
    @ApiModelProperty(value = "告警时间")
    private LocalDateTime createTime;

    /**
     * 告警处理时间
     */
    @ExcelField(value = "告警处理时间", writeConverter = LocalDateTimeConverter.class)
    @ApiModelProperty(value = "告警处理时间")
    private LocalDateTime processTime;

    /**
     * 告警详情
     */
    @ExcelField(value = "告警详情")
    @ApiModelProperty(value = "告警详情")
    private String alarmDetail;
    /**
     * 学生ID
     */
    @ApiModelProperty(value = "学生id")
    private Long studentId;
    /**
     * 位置
     */
    @ApiModelProperty(value = "位置描述")
    private String location;
    @ApiModelProperty(value = "经度")
    private String lon;
    @ApiModelProperty(value = "纬度")
    private String lat;
}
