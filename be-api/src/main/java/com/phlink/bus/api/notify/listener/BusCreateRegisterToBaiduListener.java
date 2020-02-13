package com.phlink.bus.api.notify.listener;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.map.service.IMapBaiduService;
import com.phlink.bus.api.notify.event.BusCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BusCreateRegisterToBaiduListener implements ApplicationListener<BusCreateEvent> {

    @Autowired
    private IMapBaiduService mapBaiduService;
    @Autowired
    private IBusService busService;

    @Async
    @Override
    public void onApplicationEvent(BusCreateEvent busCreateEvent) {
        Bus bus = busCreateEvent.getBus();
        if(bus == null) {
            return;
        }
        // 注册百度终端
        try {
            boolean bl = this.mapBaiduService.createBaiduEntity(bus.getNumberPlate(), bus.getBusCode());
            if (bl) {
                log.error("百度地图终端新增失败");
                return;
            }
            // 更新entityName
            LambdaUpdateWrapper<Bus> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Bus::getId, bus.getId());
            updateWrapper.set(Bus::getEntityName, bus.getBusCode());
            this.busService.update(updateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
