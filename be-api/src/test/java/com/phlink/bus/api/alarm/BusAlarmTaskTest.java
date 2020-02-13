package com.phlink.bus.api.alarm;

import com.phlink.bus.api.ApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class BusAlarmTaskTest {
    @Autowired
    private BusAlarmTask busAlarmTask;

    @Test
    public void testOfflineAlarm() {
        busAlarmTask.initNeedCheckBindBusDetail();
        busAlarmTask.offlineAlarm();
    }

    @Test
    public void testStopDelayAlarm() {
        busAlarmTask.stopDelayAlarm();
    }

    @Test
    public void testTripstateProcessAlarm() {
        String busCode = "FT9310";
        busAlarmTask.stopDelayProcessAlarm(busCode);
    }
}