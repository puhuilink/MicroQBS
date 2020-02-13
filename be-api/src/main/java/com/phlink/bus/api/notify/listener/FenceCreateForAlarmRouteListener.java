package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.service.IAlarmRouteRulesService;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.fence.domain.enums.FenceTypeEnum;
import com.phlink.bus.api.notify.event.FenceCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FenceCreateForAlarmRouteListener implements ApplicationListener<FenceCreateEvent> {

    @Autowired
    private IAlarmRouteRulesService alarmRouteRulesService;

    @Async
    @Override
    public void onApplicationEvent(FenceCreateEvent event) {
        FenceVO vo = event.getVo();
        if(vo == null) {
            return;
        }
        if(!FenceTypeEnum.POLYLINE.equals(vo.getFenceType())) {
            // 这里只创建路线围栏告警配置
            return;
        }

        AlarmRouteRules rules = new AlarmRouteRules();
        rules.setRuleName(vo.getFenceName());
        rules.setCreateBy(vo.getCreateBy());
        rules.setFenceId(vo.getId());
        rules.setRouteId(vo.getRelationId());
//        rules.setBusCode(bus.getNumberPlate());
        rules.setRouteSwitch(true);
        rules.setBusSwitch(true);
        rules.setWeekendSwitch(true);
        if(vo.getStopFence() != null && vo.getStopFence()) {
            rules.setStopSwitch(true);
        }else{
            rules.setStopSwitch(false);
        }

        try {
            alarmRouteRulesService.createAlarmRouteRules(rules);
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }
    }
}
