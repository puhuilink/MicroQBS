package com.phlink.bus.api.alarm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.alarm.dao.AlarmDeviceMapper;
import com.phlink.bus.api.alarm.domain.AlarmDevice;
import com.phlink.bus.api.alarm.domain.AlarmDeviceVO;
import com.phlink.bus.api.alarm.domain.BusApiAlarmContants;
import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmSubTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.ProcessingStatusEnum;
import com.phlink.bus.api.alarm.service.IAlarmDeviceService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouyi
 */
@Slf4j
@Service
public class AlarmDeviceServiceImpl extends ServiceImpl<AlarmDeviceMapper, AlarmDevice> implements IAlarmDeviceService {

    @Autowired
    private IStudentService studentService;
    @Autowired
    private IMapAmapService mapAmapService;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public AlarmDevice findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<AlarmDevice> listAlarmDevices(QueryRequest request, AlarmDeviceVO alarmDeviceVO) {
        Page<AlarmDevice> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort = StringUtils.isNotBlank(request.getSortOrder()) ? request.getSortOrder() : "id";
        String order = StringUtils.isNotBlank(request.getSortField()) ? request.getSortField() : BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.baseMapper.listAlarmDevices(page, alarmDeviceVO);
    }

    @Override
    public List<AlarmDevice> listAlarmDevices(AlarmDeviceVO alarmDeviceVO) {
        Page<AlarmDevice> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        this.baseMapper.listAlarmDevices(page, alarmDeviceVO);
        return page.getRecords();
    }

    @Override
    @Transactional
    public void createAlarmDevice(AlarmDevice alarmDevice) {
        alarmDevice.setCreateTime(LocalDateTime.now());
        this.save(alarmDevice);
    }

    @Override
    @Transactional
    public void modifyAlarmDevice(AlarmDevice alarmDevice) {
        this.updateById(alarmDevice);
    }

    @Override
    @Transactional
    public void deleteAlarmDevices(String[] alarmDeviceIds) {
        List<Long> list = Stream.of(alarmDeviceIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    @Transactional
    public boolean saveDeviceAlarm(String deviceId, String distance, BigDecimal longitude, BigDecimal latitude) {
        Student student = this.studentService.getStudentByDeviceCode(deviceId);
        if (student == null) {
            log.error("[alarm] 该设备{}未绑定学生，无法找到学生信息", deviceId);
            return false;
        }

        AlarmDevice alarmDevice = new AlarmDevice();
        alarmDevice.setDeviceCode(deviceId);
        alarmDevice.setCreateTime(LocalDateTime.now());
        alarmDevice.setAlarmType(AlarmTypeEnum.SCHOOL);
        alarmDevice.setAlarmSubType(AlarmSubTypeEnum.LEAVE_SCHOOL);
        alarmDevice.setAlarmLevel(AlarmLevelEnum.DELAY);
        alarmDevice.setStatus(ProcessingStatusEnum.UNPROCESSED);

        if(isPauseAlarm(alarmDevice.getAlarmType(), alarmDevice.getAlarmSubType(), deviceId)) {
            return false;
        }

        if(isSameAlarm(alarmDevice.getAlarmType(), alarmDevice.getAlarmSubType(), alarmDevice.getAlarmLevel(), deviceId)) {
            return false;
        }

        // 获得高德地图位置信息
        String formattedAddress = "未知";
        try {
            formattedAddress = mapAmapService.getLocationRegeo(longitude, latitude);
        } catch (BusApiException e) {
            e.printStackTrace();
        }
        alarmDevice.setLocation(formattedAddress);
        alarmDevice.setLon(longitude.toPlainString());
        alarmDevice.setLat(latitude.toPlainString());
        alarmDevice.setStudentId(student.getId());
        StringBuffer buffer = new StringBuffer();
        buffer.append(BusApiAlarmContants.LEAVE_FENCE).append(":").append(distance).append(";");
        buffer.append(BusApiAlarmContants.LEAVE_LOCATION).append(":").append(formattedAddress);
        alarmDevice.setAlarmDetail(buffer.toString());
        this.save(alarmDevice);
        this.sameAlarm(alarmDevice.getAlarmType(), alarmDevice.getAlarmSubType(), alarmDevice.getAlarmLevel(), alarmDevice.getDeviceCode());
        return true;
    }

    @Override
    public void pauseAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, String deviceCode, LocalDateTime expireTime) {
        String key = getPauseAlarmKey(alarmType, alarmSubType, deviceCode);
        RAtomicLong bucket = redissonClient.getAtomicLong(key);
        if(!bucket.isExists()) {
            bucket.set(1);
        }else{
            bucket.incrementAndGet();
        }
        Date date = Date.from( expireTime.atZone( ZoneId.systemDefault()).toInstant());
        bucket.expireAt(date);
    }

    @Override
    public boolean isPauseAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, String deviceCode) {
        String key = getPauseAlarmKey(alarmType, alarmSubType, deviceCode);
        RAtomicLong bucket = redissonClient.getAtomicLong(key);
        return bucket.isExists();
    }

    public String getPauseAlarmKey(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, String deviceCode) {
        return Constants.ALARM_PAUSE_DEVICE_PREFIX + alarmType + "." + alarmSubType + "." + deviceCode;
    }

    @Override
    public void sameAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, AlarmLevelEnum alarmLevel, String deviceCode) {
        String key = getSameAlarmKey(alarmType, alarmSubType, alarmLevel, deviceCode);
        RAtomicLong bucket = redissonClient.getAtomicLong(key);
        if(!bucket.isExists()) {
            bucket.set(1);
        }else{
            bucket.incrementAndGet();
        }
        bucket.expire(20, TimeUnit.MINUTES);
    }

    @Override
    public boolean isSameAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, AlarmLevelEnum alarmLevel, String deviceCode) {
        String key = getSameAlarmKey(alarmType, alarmSubType, alarmLevel, deviceCode);
        RAtomicLong bucket = redissonClient.getAtomicLong(key);
        return bucket.isExists();
    }

    public String getSameAlarmKey(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, AlarmLevelEnum alarmLevel, String busCode) {
        return Constants.ALARM_SAME_DEVICE_PREFIX + alarmType + "." + alarmSubType + "." + alarmLevel + "." + busCode;
    }
}
