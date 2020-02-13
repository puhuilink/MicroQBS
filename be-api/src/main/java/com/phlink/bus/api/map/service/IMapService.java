package com.phlink.bus.api.map.service;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.device.domain.Device;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface IMapService {

    @Async
    void batchCreateBusEntity(List<Bus> list) throws BusApiException;

    @Async
    void batchCreateDeviceEntity(List<Device> list) throws BusApiException;
}
