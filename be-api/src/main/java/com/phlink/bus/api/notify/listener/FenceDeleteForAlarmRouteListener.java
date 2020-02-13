package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.alarm.service.IAlarmRouteRulesService;
import com.phlink.bus.api.alarm.service.IAlarmSchoolRulesService;
import com.phlink.bus.api.notify.event.FenceDeleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FenceDeleteForAlarmRouteListener implements ApplicationListener<FenceDeleteEvent> {

    @Autowired
    private IAlarmRouteRulesService alarmRouteRulesService;

    @Async
    @Override
    public void onApplicationEvent(FenceDeleteEvent event) {
        List<Long> fenceIds = event.getFenceIds();
        if(fenceIds == null || fenceIds.isEmpty()) {
            return;
        }

        alarmRouteRulesService.deleteByFenceId(fenceIds);
    }
}
