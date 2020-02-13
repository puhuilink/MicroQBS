package com.phlink.bus.api.alarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.alarm.dao.AlarmRouteRulesMapper;
import com.phlink.bus.api.alarm.domain.*;
import com.phlink.bus.api.alarm.service.IAlarmRouteRulesService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ZHOUY
 */
@Service
public class AlarmRouteRulesServiceImpl extends ServiceImpl<AlarmRouteRulesMapper, AlarmRouteRules> implements IAlarmRouteRulesService {

    @Autowired
    private RedisService redisService;

    @Override
    public AlarmRouteRules findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<AlarmRouteRules> listAlarmRouteRuless(QueryRequest request, AlarmRouteRules alarmRouteRules) {
        Page<AlarmRouteRules> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort = StringUtils.isNotBlank(request.getSortOrder()) ? request.getSortOrder() : "id";
        String order = StringUtils.isNotBlank(request.getSortField()) ? request.getSortField() : BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.baseMapper.listAlarmRouteRules(page, alarmRouteRules);
    }

    @Override
    @Transactional
    public void createAlarmRouteRules(AlarmRouteRules alarmRouteRules) throws RedisConnectException {
        alarmRouteRules.setCreateTime(LocalDateTime.now());
//        alarmRouteRules.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(alarmRouteRules);
        setAlarmRouteRules(alarmRouteRules);
    }

    @Override
    @Transactional
    public void modifyAlarmRouteRules(AlarmRouteRules alarmRouteRules) throws RedisConnectException {
        alarmRouteRules.setModifyTime(LocalDateTime.now());
        alarmRouteRules.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(alarmRouteRules);
        setAlarmRouteRules(alarmRouteRules);
    }

    private void setAlarmRouteRules(AlarmRouteRules alarmRouteRules) throws RedisConnectException {
        this.redisService.hset(BusApiConstant.ROUTE_SWITCH, String.valueOf(alarmRouteRules.getId()), String.valueOf(alarmRouteRules.getRouteSwitch()));
        this.redisService.hset(BusApiConstant.BUS_SWITCH, String.valueOf(alarmRouteRules.getId()), String.valueOf(alarmRouteRules.getBusSwitch()));
        this.redisService.hset(BusApiConstant.STOP_SWITCH, String.valueOf(alarmRouteRules.getId()), String.valueOf(alarmRouteRules.getStopSwitch()));
        this.redisService.hset(BusApiConstant.ROUTE_WEEKEND_SWITCH, String.valueOf(alarmRouteRules.getId()), String.valueOf(alarmRouteRules.getWeekendSwitch()));
    }

    @Override
    public void deleteAlarmRouteRuless(String[] alarmRouteRulesIds) throws RedisConnectException {
        List<Long> list = Stream.of(alarmRouteRulesIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
        List<AlarmRouteRules> rulesList = this.baseMapper.selectBatchIds(list);
        HashMap<String, String> routeSwitchMap = new HashMap<>();
        HashMap<String, String> busSwitchMap = new HashMap<>();
        HashMap<String, String> stopSwitchMap = new HashMap<>();
        HashMap<String, String> weekendSwitchMap = new HashMap<>();
        for (AlarmRouteRules alarmRouteRules : rulesList) {
            routeSwitchMap.put(String.valueOf(alarmRouteRules.getId()), String.valueOf(alarmRouteRules.getRouteSwitch()));
            busSwitchMap.put(String.valueOf(alarmRouteRules.getId()), String.valueOf(alarmRouteRules.getBusSwitch()));
            stopSwitchMap.put(String.valueOf(alarmRouteRules.getId()), String.valueOf(alarmRouteRules.getStopSwitch()));
            weekendSwitchMap.put(String.valueOf(alarmRouteRules.getId()), String.valueOf(alarmRouteRules.getWeekendSwitch()));
        }
        this.redisService.batchHdel(BusApiConstant.ROUTE_SWITCH, routeSwitchMap);
        this.redisService.batchHdel(BusApiConstant.BUS_SWITCH, busSwitchMap);
        this.redisService.batchHdel(BusApiConstant.STOP_SWITCH, stopSwitchMap);
        this.redisService.batchHdel(BusApiConstant.ROUTE_WEEKEND_SWITCH, weekendSwitchMap);
    }

    @Override
    @Transactional
    public void switchAlarmRouteRules(RouteSwitchVO routeSwitchVO) {
        this.baseMapper.switchAlarmRouteRules(routeSwitchVO);
    }

    @Override
    @Transactional
    public void batchWeekendSwitch(Boolean weekendSwitch) {
        this.baseMapper.batchWeekendSwitch(weekendSwitch);
    }

    @Override
    @Transactional
    public void batchInvalidDate(InvalidDateVO invalidDateVO) {
        this.baseMapper.batchInvalidDate(invalidDateVO);
    }

    @Override
    public List<CodeRule> listBusCode() {
        return this.baseMapper.listBusCode();
    }

    @Override
    public RouteSwitchVO getRouteSwitch(String busCode) throws RedisConnectException {
        RouteSwitchVO routeSwitchVO = new RouteSwitchVO();
        String ruleId = this.redisService.hget(BusApiConstant.CODE_RULE, busCode);
        routeSwitchVO.setId(Long.parseLong(ruleId));
        String busSwitch = redisService.hget(BusApiConstant.BUS_SWITCH, ruleId);
        String routeSwitch = redisService.hget(BusApiConstant.ROUTE_SWITCH, ruleId);
        String weekendSwitch = redisService.hget(BusApiConstant.ROUTE_WEEKEND_SWITCH, ruleId);
        String stopSwitch = redisService.hget(BusApiConstant.STOP_SWITCH, ruleId);
        if (StringUtils.isNotBlank(busSwitch)) {
            routeSwitchVO.setBusSwitch(Boolean.parseBoolean(busSwitch));
        }
        if (StringUtils.isNotBlank(routeSwitch)) {
            routeSwitchVO.setRouteSwitch(Boolean.parseBoolean(routeSwitch));
        }
        if (StringUtils.isNotBlank(weekendSwitch)) {
            routeSwitchVO.setWeekendSwitch(Boolean.parseBoolean(weekendSwitch));
        }
        if (StringUtils.isNotBlank(stopSwitch)) {
            routeSwitchVO.setStopSwitch(Boolean.parseBoolean(stopSwitch));
        }
        return routeSwitchVO;
    }

    @Override
    public void deleteByFenceId(List<Long> fenceIds) {
        if(fenceIds == null || fenceIds.isEmpty()) {
            return;
        }
        UpdateWrapper<AlarmRouteRules> wrapper = new UpdateWrapper<>();
        wrapper.lambda().in(AlarmRouteRules::getFenceId, fenceIds);
        wrapper.lambda().set(AlarmRouteRules::getDeleted, true);
        this.update(wrapper);
    }

    @Override
    public AlarmRouteRules getByBusCode(@NotBlank String code) {
        return this.baseMapper.getByBusCode(code);
    }

    @Override
    public AlarmRouteRules getByRoute(@NotNull Long routeId) {
        QueryWrapper<AlarmRouteRules> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AlarmRouteRules::getRouteId, routeId);
        try {
            return this.getOne(queryWrapper);
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean cancelALarmInvalidDate(Long alarmId) {
        UpdateWrapper<AlarmRouteRules> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(AlarmRouteRules::getInvalidEndDate, null);
        wrapper.lambda().set(AlarmRouteRules::getInvalidStartDate, null);
        wrapper.lambda().eq(AlarmRouteRules::getId, alarmId);
        return this.update(wrapper);
    }
}