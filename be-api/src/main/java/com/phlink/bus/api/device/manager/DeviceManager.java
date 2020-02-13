package com.phlink.bus.api.device.manager;

import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.bus.service.IDvrService;
import com.phlink.bus.api.device.domain.VO.DeviceCountAll;
import com.phlink.bus.api.device.service.IDeviceRelationService;
import com.phlink.bus.api.device.service.IDeviceService;
import com.phlink.bus.api.route.service.ITripLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceManager {
    @Autowired
    private IBusService busService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IDvrService dvrService;


    public DeviceCountAll countAll() {
        DeviceCountAll result = new DeviceCountAll();
        int busCount = busService.count();
        int ewatchCount = deviceService.count();
        // 在运营的车辆
        int busRunningCount = dvrService.countRunning();
        // 绑定手环
        int ewatchBindCount = deviceService.countBindDevice();

        result.setBus(busCount);
        result.setBusRunning(busRunningCount);
        result.setEwatch(ewatchCount);
        result.setEwatchBind(ewatchBindCount);
        return result;
    }
}
