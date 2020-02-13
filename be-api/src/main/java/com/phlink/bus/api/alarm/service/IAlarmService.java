package com.phlink.bus.api.alarm.service;

import com.phlink.bus.api.alarm.domain.AlarmBus;
import com.phlink.bus.api.alarm.domain.AlarmDevice;
import com.phlink.bus.api.alarm.domain.AlarmVO;
import com.phlink.bus.api.alarm.domain.vo.AlarmStatisticsDay;

import java.util.List;
import java.util.Map;

public interface IAlarmService {

    List<AlarmVO> listAlarm();

    Map<String, Integer> countAlarmGroupByLevel();

    void pushWebMessage(AlarmDevice alarm);

    void removeWebMessage(AlarmBus alarm);

    void removeWebMessage(AlarmDevice alarm);

    AlarmStatisticsDay statisticsAlarm();

    Map<String, Integer> countAlarmBusGroupByLevel();

    Map<String, Integer> countAlarmDeviceGroupByLevel();

    void pushWebMessage(AlarmBus alarm);
}
