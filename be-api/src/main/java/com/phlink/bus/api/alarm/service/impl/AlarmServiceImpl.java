package com.phlink.bus.api.alarm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.phlink.bus.api.alarm.dao.AlarmMapper;
import com.phlink.bus.api.alarm.domain.AlarmBus;
import com.phlink.bus.api.alarm.domain.AlarmDevice;
import com.phlink.bus.api.alarm.domain.AlarmTypeStatistics;
import com.phlink.bus.api.alarm.domain.AlarmVO;
import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.domain.vo.AlarmLevelCount;
import com.phlink.bus.api.alarm.domain.vo.AlarmStatisticsDay;
import com.phlink.bus.api.alarm.service.IAlarmService;
import org.apache.commons.collections.map.HashedMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum.*;

@Service
public class AlarmServiceImpl implements IAlarmService {

    @Autowired
    private AlarmMapper alarmMapper;
    @Autowired
    private RedissonClient redissonClient;

    private static final String BUS_ALARM_RTIME_INFO = "bus.alarm.rtime.info.";
    private static final String DEVICE_ALARM_RTIME_INFO = "device.alarm.rtime.info.";

    @Override
    public List<AlarmVO> listAlarm() {
        return this.alarmMapper.listAlarm();
    }

    @Override
    public Map<String, Integer> countAlarmGroupByLevel() {
        Map<String, Integer> result = new HashedMap();
        List<AlarmLevelCount> alarmLevelCounts = this.alarmMapper.countAlarmGroupByLevel();
        for (AlarmLevelCount alarmLevelCount : alarmLevelCounts) {
            if (DELAY.getValue().equals(alarmLevelCount.getLevel())) {
                result.put(DELAY.name(), alarmLevelCount.getCount());
            }
            if (MIDDLE.getValue().equals(alarmLevelCount.getLevel())) {
                result.put(MIDDLE.name(), alarmLevelCount.getCount());
            }
            if (LOW.getValue().equals(alarmLevelCount.getLevel())) {
                result.put(LOW.name(), alarmLevelCount.getCount());
            }
            if (SLIGHT.getValue().equals(alarmLevelCount.getLevel())) {
                result.put(SLIGHT.name(), alarmLevelCount.getCount());
            }
        }
        return result;
    }

    @Override
    public Map<String, Integer> countAlarmBusGroupByLevel() {
        Map<String, Integer> result = new HashedMap();
        RScoredSortedSet<Long> delaySortedSet = redissonClient.getScoredSortedSet(BUS_ALARM_RTIME_INFO + DELAY.name());
        RScoredSortedSet<Long> middleSortedSet = redissonClient.getScoredSortedSet(BUS_ALARM_RTIME_INFO + MIDDLE.name());

        LocalDateTime now = LocalDateTime.now();
        long nowMillis = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        // 5分钟之前
        LocalDateTime before5min = now.plusMinutes(-5);
        long beforeMillis = before5min.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        result.put(DELAY.name(), delaySortedSet.removeRangeByScore(beforeMillis, true, nowMillis, true));
        result.put(MIDDLE.name(), middleSortedSet.removeRangeByScore(beforeMillis, true, nowMillis, true));
        return result;
    }

    @Override
    public Map<String, Integer> countAlarmDeviceGroupByLevel() {
        Map<String, Integer> result = new HashedMap();
        RScoredSortedSet<Long> delaySortedSet = redissonClient.getScoredSortedSet(DEVICE_ALARM_RTIME_INFO + DELAY.name());
        RScoredSortedSet<Long> middleSortedSet = redissonClient.getScoredSortedSet(DEVICE_ALARM_RTIME_INFO + MIDDLE.name());

        LocalDateTime now = LocalDateTime.now();
        long nowMillis = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        // 5分钟之前
        LocalDateTime before5min = now.plusMinutes(-5);
        long beforeMillis = before5min.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        result.put(DELAY.name(), delaySortedSet.removeRangeByScore(beforeMillis, true, nowMillis, true));
        result.put(MIDDLE.name(), middleSortedSet.removeRangeByScore(beforeMillis, true, nowMillis, true));
        return result;
    }

    @Override
    public void pushWebMessage(AlarmBus alarm) {
        if (AlarmLevelEnum.MIDDLE.equals(alarm.getAlarmLevel()) || AlarmLevelEnum.DELAY.equals(alarm.getAlarmLevel())) {
            RScoredSortedSet<Long> sortedSet = redissonClient.getScoredSortedSet(BUS_ALARM_RTIME_INFO + alarm.getAlarmLevel().name());
            long now = System.currentTimeMillis();
            sortedSet.add(now, alarm.getId());
        }
    }

    @Override
    public void pushWebMessage(AlarmDevice alarm) {
        if (AlarmLevelEnum.MIDDLE.equals(alarm.getAlarmLevel()) || AlarmLevelEnum.DELAY.equals(alarm.getAlarmLevel())) {
            RScoredSortedSet<Long> sortedSet = redissonClient.getScoredSortedSet(DEVICE_ALARM_RTIME_INFO + alarm.getAlarmLevel().name());
            long now = System.currentTimeMillis();
            sortedSet.add(now, alarm.getId());
        }
    }

    @Override
    public void removeWebMessage(AlarmBus alarm) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(BUS_ALARM_RTIME_INFO + alarm.getAlarmLevel().name());
        sortedSet.remove(alarm.getId());
    }

    @Override
    public void removeWebMessage(AlarmDevice alarm) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(DEVICE_ALARM_RTIME_INFO + alarm.getAlarmLevel().name());
        sortedSet.remove(alarm.getId());
    }

    @Override
    public AlarmStatisticsDay statisticsAlarm() {
        // 获得今日每个告警类型的告警数量
        List<AlarmTypeStatistics> data = alarmMapper.countTodayByTypeAndLevel();
        if (data == null) {
            data = new ArrayList<>();
        }
        Map<String, JSONObject> dataResult = new HashedMap();
        for (AlarmTypeStatistics d : data) {
            String k = d.getAlarmType();
            JSONObject v = d.getData();
            dataResult.put(k, v);
        }

        // 获得今日告警总数
        Integer allCount = alarmMapper.getTodayAllCount();
        Integer handCount = alarmMapper.getTodayHandCount();

        AlarmStatisticsDay result = new AlarmStatisticsDay();
        result.setData(dataResult);
        result.setAllCount(allCount);
        result.setProcessCount(handCount);
        return result;
    }
}
