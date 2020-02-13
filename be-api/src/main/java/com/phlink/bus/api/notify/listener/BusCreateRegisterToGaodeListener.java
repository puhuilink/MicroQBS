package com.phlink.bus.api.notify.listener;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.notify.event.BusCreateEvent;
import com.phlink.bus.api.notify.event.ImGroupCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BusCreateRegisterToGaodeListener implements ApplicationListener<BusCreateEvent> {

    @Autowired
    private IMapAmapService mapAmapService;
    @Autowired
    private IBusService busService;
    @Autowired
    private RedissonClient redissonClient;

    @Async
    @Override
    public void onApplicationEvent(BusCreateEvent busCreateEvent) {
        Bus bus = busCreateEvent.getBus();
        if(bus == null) {
            return;
        }
        // 注册高德终端
        long tid = this.mapAmapService.createAmapEntity(bus.getNumberPlate(), bus.getId().toString());
        // 更新到redis
        RMap<String, String> rMap = redissonClient.getMap(BusApiConstant.BUS);
        rMap.put(bus.getBusCode(), String.valueOf(tid));
        // 更新tid
        LambdaUpdateWrapper<Bus> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Bus::getId, bus.getId());
        updateWrapper.set(Bus::getTid, tid);
        this.busService.update(updateWrapper);
    }
}
