package com.phlink.bus.api.alarm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.alarm.domain.AlarmSchoolRules;
import com.phlink.bus.api.alarm.domain.CodeRule;
import com.phlink.bus.api.alarm.domain.InvalidDateVO;
import com.phlink.bus.api.alarm.domain.SchoolSwitchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZHOUY
 */
public interface AlarmSchoolRulesMapper extends BaseMapper<AlarmSchoolRules> {

    void switchAlarmSchoolRules(@Param("schoolSwitchVO") SchoolSwitchVO schoolSwitchVO);

    void batchWeekendSwitch(@Param("weekendSwitch") Boolean weekendSwitch);

    void batchInvalidDate(@Param("invalidDateVO") InvalidDateVO invalidDateVO);

    List<CodeRule> listDeviceCode();

    IPage<AlarmSchoolRules> listAlarmSchoolRuless(Page page, AlarmSchoolRules alarmSchoolRules);

    List<AlarmSchoolRules> listRulesByDeviceId(@Param("deviceId") String deviceId);
}
