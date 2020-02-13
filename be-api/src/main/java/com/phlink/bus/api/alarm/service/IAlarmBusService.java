package com.phlink.bus.api.alarm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.alarm.domain.AlarmBus;
import com.phlink.bus.api.alarm.domain.AlarmBusVO;
import com.phlink.bus.api.alarm.domain.CodeRule;
import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmSubTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmTypeEnum;
import com.phlink.bus.api.bus.domain.BindBusDetailInfo;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.common.domain.QueryRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhouyi
 */
public interface IAlarmBusService extends IService<AlarmBus> {


    /**
     * 获取详情
     */
    AlarmBus findById(Long id);

    /**
     * 查询列表
     *
     * @param request
     * @param alarmBusVO
     * @return
     */
    IPage<AlarmBus> listAlarmBus(QueryRequest request, AlarmBusVO alarmBusVO);

    List<AlarmBus> listAlarmBus(AlarmBusVO alarmVO);

    /**
     * 新增
     *
     * @param alarmBus
     */
    void createAlarmBus(AlarmBus alarmBus);

    /**
     * 修改
     *
     * @param alarmBus
     */
    void modifyAlarmBus(AlarmBus alarmBus);

    /**
     * 批量删除
     *
     * @param alarmBusIds
     */
    void deleteAlarmBuss(String[] alarmBusIds);

    /**
     * 获取所有车辆busCode
     *
     * @return
     */
    List<CodeRule> listBusCode();

    /**
     * 生成偏离路线告警
     *
     * @param entityName
     * @param distance
     */
    AlarmBus saveRouteDeviationAlarm(String entityName, Integer distance, BigDecimal longitude, BigDecimal latitude);

    /**
     * 生成超速告警
     *
     * @param entityName
     * @param glon
     * @param glat
     */
    AlarmBus saveSpeedingAlarm(String entityName, Float speed, BigDecimal glon, BigDecimal glat);

    /**
     * 生成站点迟到告警
     * @param busCode
     * @param dvrLocation
     * @param alarmLevel
     * @param stopName
     * @param durationMin
     * @param threshold
     * @return
     */
    boolean saveStopDelayAlarm(String busCode, DvrLocation dvrLocation, AlarmLevelEnum alarmLevel, String stopName, long durationMin, int threshold);
    /**
     * 生成失联告警
     * @param b
     * @param location
     * @param duration
     * @return
     */
    AlarmBus saveDvrOffineAlarm(BindBusDetailInfo b, DvrLocation location, Long duration, Integer threshold);

    AlarmBus getLastAlarmInfo(AlarmSubTypeEnum subType, String busCode);

    void pauseAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, String busCode, LocalDateTime expireTime);

    boolean isPauseAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, String busCode);

    void sameAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, AlarmLevelEnum alarmLevel, String busCode);

    boolean isSameAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, AlarmLevelEnum alarmLevel, String busCode);
}
