package com.phlink.bus.api.alarm.service.impl;

import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmSubTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmTypeEnum;
import com.phlink.bus.api.alarm.service.IAlarmBusService;
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
public class AlarmBusServiceImplTest {

    @Autowired
    private IAlarmBusService alarmBusService;

    @Test
    public void testSameAlarm(){
        alarmBusService.sameAlarm(AlarmTypeEnum.STOP, AlarmSubTypeEnum.DELAY, AlarmLevelEnum.MIDDLE, "testbuscode.stopname");
        boolean isSame = alarmBusService.isSameAlarm(AlarmTypeEnum.STOP, AlarmSubTypeEnum.DELAY, AlarmLevelEnum.MIDDLE, "testbuscode");
        log.info("isSame ==== {}", isSame);
    }

    @Test
    public void testIsSame(){
        boolean isSame = alarmBusService.isSameAlarm(AlarmTypeEnum.STOP, AlarmSubTypeEnum.DELAY, AlarmLevelEnum.MIDDLE, "testbuscode.stopname");
        log.info("isSame ==== {}", isSame);
    }



}