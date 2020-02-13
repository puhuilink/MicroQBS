package com.phlink.bus.api.notify.listener;

import com.phlink.bus.api.alarm.domain.AlarmSchoolRules;
import com.phlink.bus.api.alarm.service.IAlarmSchoolRulesService;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.fence.domain.enums.FenceTypeEnum;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.notify.event.FenceCreateEvent;
import com.phlink.bus.api.route.domain.vo.FenceStopVO;
import com.phlink.bus.api.route.service.IStopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FenceCreateForAlarmSchoolListener implements ApplicationListener<FenceCreateEvent> {

    @Autowired
    private IAlarmSchoolRulesService alarmSchoolRulesService;

    @Async
    @Override
    public void onApplicationEvent(FenceCreateEvent event) {
        FenceVO vo = event.getVo();
        if(vo == null) {
            return;
        }
        if(FenceTypeEnum.POLYLINE.equals(vo.getFenceType())) {
            // 这里只创建学校围栏告警配置
            return;
        }

        AlarmSchoolRules rules = new AlarmSchoolRules();
        rules.setRuleName(vo.getFenceName());
        rules.setSchoolId(vo.getRelationId());
        rules.setDeviceSwitch(true);
        rules.setWeekendSwitch(true);
        rules.setCreateBy(vo.getCreateBy());
        rules.setFenceId(vo.getId());

        try {
            alarmSchoolRulesService.createAlarmSchoolRules(rules);
        } catch (RedisConnectException e) {
            log.error("保存AlarmSchoolRule失败，{}", rules, e);
        }
    }
}
