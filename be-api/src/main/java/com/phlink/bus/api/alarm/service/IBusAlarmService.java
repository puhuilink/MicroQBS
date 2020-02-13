package com.phlink.bus.api.alarm.service;

import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.bus.domain.DvrLocation;

public interface IBusAlarmService {

    void alarmBusMapping(DvrLocation dvrLocation);

    void asyncBuildRouteAlarm(AlarmRouteRules rules, DvrLocation dvrLocation);

    void asyncBuildStopAlarm(AlarmRouteRules rules, DvrLocation dvrLocation);

    /**
     * 失效时间检查
     *
     *
     * @param rules
     * @param busCode
     * @param busId
     * @return
     */
    Boolean invalidDateCheck(AlarmRouteRules rules, String busCode, Long busId);

    void asyncBuildBusAlarm(DvrLocation dvrLocation);

}
