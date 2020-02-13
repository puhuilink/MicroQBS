package com.phlink.bus.api.device.service.impl;

import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.device.service.IDeviceService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class DeviceServiceImplTest {
    @Autowired
    private IDeviceService deviceService;

    @Test
    public void testDeviceHeartBeat() {
        deviceService.deviceHeartBeat();
    }
}