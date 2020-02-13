package com.phlink.bus.api.alarm.service;

import com.phlink.bus.api.device.domain.EwatchLocation;
import org.springframework.scheduling.annotation.Async;

public interface IDeviceAlarmService {

    void alarmDeviceMapping(EwatchLocation ewatchLocation);
}
