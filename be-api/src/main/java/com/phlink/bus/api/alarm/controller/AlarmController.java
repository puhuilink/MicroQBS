package com.phlink.bus.api.alarm.controller;

import com.phlink.bus.api.alarm.domain.AlarmVO;
import com.phlink.bus.api.alarm.domain.vo.AlarmStatisticsDay;
import com.phlink.bus.api.alarm.service.IAlarmService;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/alarmmsg")
@Api(tags = ApiTagsConstant.TAG_ALARM)
public class AlarmController extends BaseController {

    @Autowired
    public IAlarmService alarmService;

    @GetMapping
    //@RequiresPermissions("alarm:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_ALARM, httpMethod = "GET")
    public List<AlarmVO> listAlarm() {
        return this.alarmService.listAlarm();
    }

    @GetMapping("/count")
    //@RequiresPermissions("alarm:view")
    @ApiOperation(value = "告警数量", notes = "告警数量", tags = ApiTagsConstant.TAG_ALARM, httpMethod = "GET")
    public Map<String, Integer> countAlarm() {
        return this.alarmService.countAlarmGroupByLevel();
    }

    @GetMapping("/bus-count/today")
    //@RequiresPermissions("alarm:view")
    @ApiOperation(value = "今日车辆告警数量", notes = "今日车辆告警数量", tags = ApiTagsConstant.TAG_ALARM, httpMethod = "GET")
    public Map<String, Integer> countAlarmBus() {
        return this.alarmService.countAlarmBusGroupByLevel();
    }

    @GetMapping("/device-count/today")
    //@RequiresPermissions("alarm:view")
    @ApiOperation(value = "今日手环告警数量", notes = "今日手环告警数量", tags = ApiTagsConstant.TAG_ALARM, httpMethod = "GET")
    public Map<String, Integer> countAlarmDevice() {
        return this.alarmService.countAlarmDeviceGroupByLevel();
    }

    @GetMapping("/statistics/today")
    //@RequiresPermissions("alarm:view")
    @ApiOperation(value = "今日告警统计", notes = "今日告警统计", tags = ApiTagsConstant.TAG_ALARM, httpMethod = "GET")
    public AlarmStatisticsDay statisticsAlarm() {
        return this.alarmService.statisticsAlarm();
    }
}
