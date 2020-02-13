package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.service.IBusAlarmService;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.notify.event.AlarmBusMappingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlarmBusMappingListener implements ApplicationListener<AlarmBusMappingEvent> {

    @Autowired
    private IBusAlarmService busAlarmService;

    @Async
    @Override
    public void onApplicationEvent(AlarmBusMappingEvent event) {
        DvrLocation dvrLocation = event.getDvrLocation();
        AlarmRouteRules rules = event.getRules();
        if(dvrLocation == null || rules == null) {
            return;
        }
        //路线开关
        if (rules.getRouteSwitch()) {
            busAlarmService.asyncBuildRouteAlarm(rules, dvrLocation);
        }
        //站点是否告警，需要等分析完站点距离之后再决定
        busAlarmService.asyncBuildStopAlarm(rules, dvrLocation);

        //车辆开关
        if (rules.getBusSwitch()) {
            busAlarmService.asyncBuildBusAlarm(dvrLocation);
        }
    }
}
