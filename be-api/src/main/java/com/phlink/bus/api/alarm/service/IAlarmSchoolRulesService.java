package com.phlink.bus.api.alarm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.alarm.domain.AlarmSchoolRules;
import com.phlink.bus.api.alarm.domain.CodeRule;
import com.phlink.bus.api.alarm.domain.InvalidDateVO;
import com.phlink.bus.api.alarm.domain.SchoolSwitchVO;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.RedisConnectException;

import java.util.List;

/**
 * @author ZHOUY
 */
public interface IAlarmSchoolRulesService extends IService<AlarmSchoolRules> {


    /**
     * 获取详情
     */
    AlarmSchoolRules findById(Long id);

    /**
     * 查询列表
     *
     * @param request
     * @param alarmSchoolRules
     * @return
     */
    IPage<AlarmSchoolRules> listAlarmSchoolRuless(QueryRequest request, AlarmSchoolRules alarmSchoolRules);

    /**
     * 新增
     *
     * @param alarmSchoolRules
     */
    void createAlarmSchoolRules(AlarmSchoolRules alarmSchoolRules) throws RedisConnectException;

    /**
     * 修改
     *
     * @param alarmSchoolRules
     */
    void modifyAlarmSchoolRules(AlarmSchoolRules alarmSchoolRules) throws RedisConnectException;

    /**
     * 批量删除
     *
     * @param alarmSchoolRulesIds
     */
    void deleteAlarmSchoolRuless(String[] alarmSchoolRulesIds) throws RedisConnectException;

    /**
     * 批量设置开关
     *
     * @param schoolswitchVO
     */
    void switchAlarmRouteRules(SchoolSwitchVO schoolswitchVO);

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
     * 获取手环code和规则id
     *
     * @return
     */
    List<CodeRule> listDeviceCode();

    /**
     * 获取学校开关
     *
     * @param deviceCode
     * @return
     * @throws RedisConnectException
     */
    SchoolSwitchVO getSchoolSwitch(String deviceCode) throws RedisConnectException;

    void deleteByFenceId(List<Long> fenceIds);

    /**
     * 根据设备ID获取该设备ID对应的告警规则
     * @param deviceId
     * @return
     */
    List<AlarmSchoolRules> getByDeviceId(String deviceId);

    boolean cancelALarmInvalidDate(Long alarmId);
}
