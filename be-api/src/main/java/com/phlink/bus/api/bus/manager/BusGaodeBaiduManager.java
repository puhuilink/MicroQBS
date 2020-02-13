package com.phlink.bus.api.bus.manager;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.utils.SpringContextUtil;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.device.service.IDeviceService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusGaodeBaiduManager {

    @Autowired
    private IBusService busService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private RedissonClient redissonClient;

    @Async
    @Scheduled(cron = "0 30 */1 * * ?")
    public void initToGaode() {
        if(SpringContextUtil.isPro()) {
            List<Bus> buses = busService.list();
            busService.batchRegisterToGaode(buses);
        }
    }

    @Async
    @Scheduled(cron = "0 30 */1 * * ?")
    public void initToBaidu() {
        if(SpringContextUtil.isPro()) {
            List<Bus> buses = busService.list();
            busService.batchRegisterToBaidu(buses);
        }
    }

    @Async
    @Scheduled(cron = "0 30 */1 * * ?")
    public void initToGaodeDevice() {
        if(SpringContextUtil.isPro()) {
            List<Device> devices = deviceService.list();
            deviceService.batchRegisterToGaode(devices);
        }
    }

}
