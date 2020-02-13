package com.phlink.bus.api.alarm.dao;

import com.phlink.bus.api.alarm.domain.AlarmTypeStatistics;
import com.phlink.bus.api.alarm.domain.AlarmVO;
import com.phlink.bus.api.alarm.domain.vo.AlarmLevelCount;

import java.time.LocalDate;
import java.util.List;

public interface AlarmMapper {

    List<AlarmVO> listAlarm();

    List<AlarmLevelCount> countAlarmGroupByLevel();

    List<AlarmTypeStatistics> countTodayByTypeAndLevel();

    Integer getTodayAllCount();

    Integer getTodayHandCount();

    List<AlarmLevelCount> countAlarmBusGroupByLevelToday(LocalDate day);

    List<AlarmLevelCount> countAlarmDeviceGroupByLevelToday(LocalDate day);
}
