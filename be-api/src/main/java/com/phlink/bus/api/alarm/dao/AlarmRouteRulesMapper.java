package com.phlink.bus.api.alarm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.domain.CodeRule;
import com.phlink.bus.api.alarm.domain.InvalidDateVO;
import com.phlink.bus.api.alarm.domain.RouteSwitchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZHOUY
 */
public interface AlarmRouteRulesMapper extends BaseMapper<AlarmRouteRules> {

    void switchAlarmRouteRules(@Param("routeSwitchVO") RouteSwitchVO routeSwitchVO);

    void batchWeekendSwitch(@Param("weekendSwitch") Boolean weekendSwitch);

    void batchInvalidDate(@Param("invalidDateVO") InvalidDateVO invalidDateVO);

    List<CodeRule> listBusCode();


    IPage<AlarmRouteRules> listAlarmRouteRules(Page page, @Param("alarmRouteRules") AlarmRouteRules alarmRouteRules);

    AlarmRouteRules getByBusCode(@Param("busCode") String busCode);
}