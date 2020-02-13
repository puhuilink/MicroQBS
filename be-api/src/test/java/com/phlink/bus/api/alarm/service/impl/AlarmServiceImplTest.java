package com.phlink.bus.api.alarm.service.impl;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.alarm.domain.vo.AlarmStatisticsDay;
import com.phlink.bus.api.alarm.service.IAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class AlarmServiceImplTest {

    @Autowired
    private IAlarmService alarmService;

    @Test
    public void testStatisticsAlarm() {
        AlarmStatisticsDay alarmStatisticsDay = alarmService.statisticsAlarm();
        log.info("AlarmStatisticsDay ---- > {}", JSON.toJSONString(alarmStatisticsDay));
    }

}