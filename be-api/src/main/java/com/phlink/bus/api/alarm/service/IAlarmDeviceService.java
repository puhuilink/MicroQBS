package com.phlink.bus.api.alarm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.alarm.domain.AlarmDevice;
import com.phlink.bus.api.alarm.domain.AlarmDeviceVO;
import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmSubTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmTypeEnum;
import com.phlink.bus.api.common.domain.QueryRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhouyi
 */
public interface IAlarmDeviceService extends IService<AlarmDevice> {


    /**
     * 获取详情
     */
    AlarmDevice findById(Long id);

    /**
     * 查询列表
     *
     * @param request
     * @param alarmDeviceVO
     * @return
     */
    IPage<AlarmDevice> listAlarmDevices(QueryRequest request, AlarmDeviceVO alarmDeviceVO);

    List<AlarmDevice> listAlarmDevices(AlarmDeviceVO alarmDeviceVO);

    /**
     * 新增
     *
     * @param alarmDevice
     */
    void createAlarmDevice(AlarmDevice alarmDevice);

    /**
     * 修改
     *
     * @param alarmDevice
     */
    void modifyAlarmDevice(AlarmDevice alarmDevice);

    /**
     * 批量删除
     *
     * @param alarmDeviceIds
     */
    void deleteAlarmDevices(String[] alarmDeviceIds);

    /**
     * 学校告警
     *
     * @param deviceId
     * @param distance
     */
    boolean saveDeviceAlarm(String deviceId, String distance, BigDecimal longitude, BigDecimal latitude);

    void pauseAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, String deviceCode, LocalDateTime expireTime);

    boolean isPauseAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, String deviceCode);

    void sameAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, AlarmLevelEnum alarmLevel, String deviceCode);

    boolean isSameAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, AlarmLevelEnum alarmLevel, String deviceCode);
}
