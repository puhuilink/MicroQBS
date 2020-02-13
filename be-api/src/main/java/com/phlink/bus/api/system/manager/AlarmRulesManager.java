package com.phlink.bus.api.system.manager;

import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.domain.AlarmSchoolRules;
import com.phlink.bus.api.alarm.domain.CodeRule;
import com.phlink.bus.api.common.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmRulesManager {

    @Autowired
    private CacheService cacheService;


    public void loadAlarmRouteRules(List<AlarmRouteRules> alarmRouteRulesList) throws Exception {

        cacheService.saveAlarmRouteRules(alarmRouteRulesList);
    }

    public void loadAlarmSchoolRules(List<AlarmSchoolRules> alarmSchoolRulesList) throws Exception {

        cacheService.saveAlarmSchoolRules(alarmSchoolRulesList);
    }

    public void loadBusAndDeviceAlarmRules(List<CodeRule> list) throws Exception {

        cacheService.loadBusAndDeviceAlarmRules(list);
    }
}
