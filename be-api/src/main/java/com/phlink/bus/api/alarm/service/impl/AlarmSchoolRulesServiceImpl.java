package com.phlink.bus.api.alarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.alarm.dao.AlarmSchoolRulesMapper;
import com.phlink.bus.api.alarm.domain.AlarmSchoolRules;
import com.phlink.bus.api.alarm.domain.CodeRule;
import com.phlink.bus.api.alarm.domain.InvalidDateVO;
import com.phlink.bus.api.alarm.domain.SchoolSwitchVO;
import com.phlink.bus.api.alarm.service.IAlarmSchoolRulesService;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ZHOUY
 */
@Service
public class AlarmSchoolRulesServiceImpl extends ServiceImpl<AlarmSchoolRulesMapper, AlarmSchoolRules> implements IAlarmSchoolRulesService {

    @Autowired
    private RedisService redisService;

    @Override
    public AlarmSchoolRules findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<AlarmSchoolRules> listAlarmSchoolRuless(QueryRequest request, AlarmSchoolRules alarmSchoolRules) {
        Page<AlarmSchoolRules> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort = StringUtils.isNotBlank(request.getSortOrder()) ? request.getSortOrder() : "id";
        String order = StringUtils.isNotBlank(request.getSortField()) ? request.getSortField() : BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.baseMapper.listAlarmSchoolRuless(page, alarmSchoolRules);
    }

    @Override
    @Transactional
    public void createAlarmSchoolRules(AlarmSchoolRules alarmSchoolRules) throws RedisConnectException {
        alarmSchoolRules.setCreateTime(LocalDateTime.now());
//        alarmSchoolRules.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(alarmSchoolRules);
        this.redisService.hset(BusApiConstant.DEVICE_SWITCH, String.valueOf(alarmSchoolRules.getId()), String.valueOf(alarmSchoolRules.getDeviceSwitch()));
        this.redisService.hset(BusApiConstant.SCHOOL_WEEKEND_SWITCH, String.valueOf(alarmSchoolRules.getId()), String.valueOf(alarmSchoolRules.getWeekendSwitch()));
    }

    @Override
    @Transactional
    public void modifyAlarmSchoolRules(AlarmSchoolRules alarmSchoolRules) throws RedisConnectException {
        alarmSchoolRules.setModifyTime(LocalDateTime.now());
        alarmSchoolRules.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(alarmSchoolRules);
        this.redisService.hset(BusApiConstant.DEVICE_SWITCH, String.valueOf(alarmSchoolRules.getId()), String.valueOf(alarmSchoolRules.getDeviceSwitch()));
        this.redisService.hset(BusApiConstant.SCHOOL_WEEKEND_SWITCH, String.valueOf(alarmSchoolRules.getId()), String.valueOf(alarmSchoolRules.getWeekendSwitch()));
    }

    @Override
    @Transactional
    public void deleteAlarmSchoolRuless(String[] alarmSchoolRulesIds) throws RedisConnectException {
        List<Long> list = Stream.of(alarmSchoolRulesIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
        List<AlarmSchoolRules> schoolRulesList = this.baseMapper.selectBatchIds(list);
        HashMap<String, String> deviceMap = new HashMap<>();
        HashMap<String, String> weekendMap = new HashMap<>();
        for (AlarmSchoolRules alarmSchoolRules : schoolRulesList) {
            deviceMap.put(String.valueOf(alarmSchoolRules.getId()), String.valueOf(alarmSchoolRules.getDeviceSwitch()));
            weekendMap.put(String.valueOf(alarmSchoolRules.getId()), String.valueOf(alarmSchoolRules.getWeekendSwitch()));
        }
        this.redisService.batchHdel(BusApiConstant.DEVICE_SWITCH, deviceMap);
        this.redisService.batchHdel(BusApiConstant.SCHOOL_WEEKEND_SWITCH, weekendMap);
    }

    @Override
    @Transactional
    public void switchAlarmRouteRules(SchoolSwitchVO schoolswitchVO) {
        this.baseMapper.switchAlarmSchoolRules(schoolswitchVO);
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
    public List<CodeRule> listDeviceCode() {
        return this.baseMapper.listDeviceCode();
    }

    @Override
    public SchoolSwitchVO getSchoolSwitch(String deviceCode) throws RedisConnectException {
        SchoolSwitchVO schoolSwitchVO = new SchoolSwitchVO();
        String ruleId = this.redisService.hget(BusApiConstant.CODE_RULE, deviceCode);
        schoolSwitchVO.setId(Long.parseLong(ruleId));
        String deviceSwitch = redisService.hget(BusApiConstant.DEVICE_SWITCH, ruleId);
        String weekendSwitch = redisService.hget(BusApiConstant.SCHOOL_WEEKEND_SWITCH, ruleId);
        if (StringUtils.isNotBlank(deviceSwitch)) {
            schoolSwitchVO.setDeviceSwitch(Boolean.parseBoolean(deviceSwitch));
        }
        if (StringUtils.isNotBlank(weekendSwitch)) {
            schoolSwitchVO.setWeekendSwitch(Boolean.parseBoolean(weekendSwitch));
        }
        return schoolSwitchVO;
    }

    @Override
    public void deleteByFenceId(List<Long> fenceIds) {
        if (fenceIds == null || fenceIds.isEmpty()) {
            return;
        }
        UpdateWrapper<AlarmSchoolRules> wrapper = new UpdateWrapper<>();
        wrapper.lambda().in(AlarmSchoolRules::getFenceId, fenceIds);
        wrapper.lambda().set(AlarmSchoolRules::getDeleted, true);
        this.update(wrapper);
    }

    @Override
    public List<AlarmSchoolRules> getByDeviceId(@NotBlank String deviceId) {
        return this.baseMapper.listRulesByDeviceId(deviceId);
    }

    @Override
    public boolean cancelALarmInvalidDate(Long alarmId) {
        UpdateWrapper<AlarmSchoolRules> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(AlarmSchoolRules::getInvalidEndDate, null);
        wrapper.lambda().set(AlarmSchoolRules::getInvalidStartDate, null);
        wrapper.lambda().eq(AlarmSchoolRules::getId, alarmId);
        return this.update(wrapper);
    }
}
