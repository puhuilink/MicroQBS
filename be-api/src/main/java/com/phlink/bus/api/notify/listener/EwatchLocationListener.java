package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.alarm.service.IDeviceAlarmService;
import com.phlink.bus.api.device.domain.EwatchLocation;
import com.phlink.bus.api.notify.event.EwatchLocationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EwatchLocationListener implements ApplicationListener<EwatchLocationEvent> {

    @Autowired
    private IDeviceAlarmService deviceAlarmService;

    @Async
    @Override
    public void onApplicationEvent(EwatchLocationEvent event) {
        EwatchLocation ewatchLocation = event.getEwatchLocation();
        if(ewatchLocation == null) {
            return;
        }
        deviceAlarmService.alarmDeviceMapping(ewatchLocation);
    }
}
