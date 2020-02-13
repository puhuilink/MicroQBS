package com.phlink.bus.api.alarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.alarm.service.IDeviceAlarmService;
import com.phlink.bus.api.device.domain.EwatchLocation;
import com.phlink.bus.api.device.service.IEwatchLocationService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class DeviceAlarmServiceImplTest {
    private static final String deviceId = "9612481149";

    @Autowired
    private IDeviceAlarmService deviceAlarmService;
    @Autowired
    private IEwatchLocationService ewatchLocationService;

    @Test
    public void testAlarmDeviceMapping() {
        QueryWrapper<EwatchLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EwatchLocation::getDeviceId, deviceId);
        List<EwatchLocation> locations = ewatchLocationService.list(queryWrapper);
        if(locations != null && !locations.isEmpty()) {
            EwatchLocation location = locations.get(locations.size() - 1);
            location.setTimestamp(System.currentTimeMillis());
            location.setLongitude(BigDecimal.valueOf(116.526420084636));
            location.setLatitude(BigDecimal.valueOf(40.101089138455));

            deviceAlarmService.alarmDeviceMapping(location);
        }
        try {
            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}