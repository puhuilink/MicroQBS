package com.phlink.bus.api.system.manager;

import com.phlink.bus.api.alarm.domain.CodeFence;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.common.service.CacheService;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.route.domain.vo.RouteBusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityManager {

    @Autowired
    private CacheService cacheService;

    public void loadBus(List<Bus> busList) throws Exception {

        cacheService.saveBus(busList);
    }

    public void loadBusRouteFence(List<CodeFence> busFenceList) throws Exception {

        cacheService.saveBusRouteFence(busFenceList);
    }

    public void loadBusStopFence(List<CodeFence> busFenceList) throws Exception {

        cacheService.saveBusStopFence(busFenceList);
    }

    public void loadDevice(List<Device> deviceList) throws Exception {

        cacheService.saveDevice(deviceList);
    }

    public void loadSchoolFence(List<CodeFence> schoolFenceList) throws Exception {
        cacheService.saveSchoolFence(schoolFenceList);
    }

    public void loadRouteBus(List<RouteBusVO> routeBusVOList) throws Exception {
        cacheService.saveRouteBus(routeBusVOList);
    }
}
