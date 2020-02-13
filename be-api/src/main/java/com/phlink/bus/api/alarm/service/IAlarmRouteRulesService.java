package com.phlink.bus.api.alarm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.domain.CodeRule;
import com.phlink.bus.api.alarm.domain.InvalidDateVO;
import com.phlink.bus.api.alarm.domain.RouteSwitchVO;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.RedisConnectException;

import java.util.List;

/**
 * @author ZHOUY
 */
public interface IAlarmRouteRulesService extends IService<AlarmRouteRules> {


    /**
     * 获取详情
     */
    AlarmRouteRules findById(Long id);

    /**
     * 查询列表
     *
     * @param request
     * @param alarmRouteRules
     * @return
     */
    IPage<AlarmRouteRules> listAlarmRouteRuless(QueryRequest request, AlarmRouteRules alarmRouteRules);

    /**
     * 新增
     *
     * @param alarmRouteRules
     */
    void createAlarmRouteRules(AlarmRouteRules alarmRouteRules) throws RedisConnectException;

    /**
     * 修改
     *
     * @param alarmRouteRules
     */
    void modifyAlarmRouteRules(AlarmRouteRules alarmRouteRules) throws RedisConnectException;

    /**
     * 批量删除
     *
     * @param alarmRouteRulesIds
     */
    void deleteAlarmRouteRuless(String[] alarmRouteRulesIds) throws RedisConnectException;

    /**
     * 批量变更开关
     *
     * @param routeSwitchVO
     */
    void switchAlarmRouteRules(RouteSwitchVO routeSwitchVO);

    /**
     * 一键设置周末
     *
     * @param weekendSwitch
     */
    void batchWeekendSwitch(Boolean weekendSwitch);

    /**
     * 一键设置失效时间
     *
     * @param invalidDateVO
     */
    void batchInvalidDate(InvalidDateVO invalidDateVO);

    /**
     * 获取车辆code和规则id
     *
     * @return
     */
    List<CodeRule> listBusCode();

    /**
     * 根据车辆code获取路线类开关
     *
     * @param busCode
     * @return
     */
    RouteSwitchVO getRouteSwitch(String busCode) throws RedisConnectException;

    void deleteByFenceId(List<Long> fenceIds);

    /**
     * 根据车辆code获取规则信息
     * @param code
     * @return
     */
    AlarmRouteRules getByBusCode(String code);

    AlarmRouteRules getByRoute(Long routeId);

    boolean cancelALarmInvalidDate(Long alarmId);
}
