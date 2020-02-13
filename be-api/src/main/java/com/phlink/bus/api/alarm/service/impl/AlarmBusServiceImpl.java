package com.phlink.bus.api.alarm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.alarm.dao.AlarmBusMapper;
import com.phlink.bus.api.alarm.domain.*;
import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmSubTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.AlarmTypeEnum;
import com.phlink.bus.api.alarm.domain.enums.ProcessingStatusEnum;
import com.phlink.bus.api.alarm.service.IAlarmBusService;
import com.phlink.bus.api.alarm.service.IAlarmService;
import com.phlink.bus.api.bus.domain.BindBusDetailInfo;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserService;
import com.phlink.bus.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
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
public class AlarmBusServiceImpl extends ServiceImpl<AlarmBusMapper, AlarmBus> implements IAlarmBusService {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private AlarmConfig alarmConfig;
    @Autowired
    private IMapAmapService mapAmapService;
    @Autowired
    private UserService userService;
    @Autowired
    private IBusService busService;
    @Autowired
    private IAlarmService alarmService;

    @Override
    public AlarmBus findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<AlarmBus> listAlarmBus(QueryRequest request, AlarmBusVO alarmVO) {
        Page<AlarmBus> page = new Page<>(request.getPageNum(), request.getPageSize());
//        String sort = StringUtils.isNotBlank(request.getSortOrder()) ? request.getSortOrder() : "id";
//        String order = StringUtils.isNotBlank(request.getSortField()) ? request.getSortField() : BusApiConstant.ORDER_DESC;
//        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.baseMapper.listAlarmBuss(page, alarmVO);
    }

    @Override
    public List<AlarmBus> listAlarmBus(AlarmBusVO alarmVO) {
        Page<AlarmBus> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        this.baseMapper.listAlarmBuss(page, alarmVO);
        return page.getRecords();
    }

    @Override
    @Transactional
    public void createAlarmBus(AlarmBus alarmBus) {
        alarmBus.setCreateTime(LocalDateTime.now());
        this.save(alarmBus);
    }

    @Override
    @Transactional
    public void modifyAlarmBus(AlarmBus alarmBus) {
        this.updateById(alarmBus);
    }

    @Override
    @Transactional
    public void deleteAlarmBuss(String[] alarmBusIds) {
        List<Long> list = Stream.of(alarmBusIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public List<CodeRule> listBusCode() {
        return this.baseMapper.listBusCode();
    }

    @Override
    @Transactional
    public AlarmBus saveRouteDeviationAlarm(String busCode, Integer distance, BigDecimal longitude, BigDecimal latitude) {
        AlarmBus alarm = new AlarmBus();
        alarm.setCreateTime(LocalDateTime.now());
        alarm.setAlarmType(AlarmTypeEnum.ROUTE);
        alarm.setAlarmSubType(AlarmSubTypeEnum.DIVERGE);

        if (isPauseAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), busCode)) {
            return alarm;
        }

        StringBuilder detail = new StringBuilder();
        AlarmLevelEnum alarmLevelEnum = null;
        if (distance > alarmConfig.getRouteOffsetDistance()) {
            if (distance > alarmConfig.getRouteOffsetDistanceUp()) {
                alarmLevelEnum = AlarmLevelEnum.DELAY;
                detail.append(BusApiAlarmContants.BUS_ROUTE_THRESHOLD).append(":").append(alarmConfig.getRouteOffsetDistanceUp()).append(";");
            } else {
                alarmLevelEnum = AlarmLevelEnum.LOW;
                detail.append(BusApiAlarmContants.BUS_ROUTE_THRESHOLD).append(":").append(alarmConfig.getRouteOffsetDistance()).append(";");
            }
        }

        if (alarmLevelEnum == null) {
            return alarm;
        }

        if (isSameAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), alarmLevelEnum, busCode)) {
            return alarm;
        }

        alarm.setAlarmLevel(alarmLevelEnum);

        buildAlarmStatus(alarm, alarmLevelEnum);

        // 获得高德地图位置信息
        String formattedAddress = "未知";
        try {
            formattedAddress = mapAmapService.getLocationRegeo(longitude, latitude);
        } catch (BusApiException e) {
            e.printStackTrace();
        }
        alarm.setBusCode(busCode);
        alarm.setLat(latitude.toPlainString());
        alarm.setLon(longitude.toPlainString());
        alarm.setLocation(formattedAddress);

        User driver = userService.getDriverByBusCode(busCode);
        if (driver != null) {
            alarm.setDriverName(driver.getRealname());
            alarm.setDriverId(driver.getUserId());
            alarm.setDriverMobile(driver.getMobile());
        }

        Bus bus = busService.getByBusCode(busCode);
        if (bus != null) {
            alarm.setNumberPlate(bus.getNumberPlate());
        }

        detail.append(BusApiAlarmContants.BUS_ROUTE_DISTANCE).append(":").append(distance).append(";");
        detail.append(BusApiAlarmContants.BUS_ROUTE_LOCATION).append(":").append(formattedAddress).append(";");

        alarm.setAlarmDetail(detail.toString());
        this.save(alarm);
        alarmService.pushWebMessage(alarm);
        this.sameAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), alarm.getAlarmLevel(), alarm.getBusCode());
        return alarm;
    }

    @Override
    @Transactional
    public AlarmBus saveSpeedingAlarm(String busCode, Float speed, BigDecimal longitude, BigDecimal latitude) {
        AlarmBus alarm = new AlarmBus();

        alarm.setCreateTime(LocalDateTime.now());
        alarm.setAlarmType(AlarmTypeEnum.BUS);
        alarm.setAlarmSubType(AlarmSubTypeEnum.SPEEDING);
        alarm.setBusCode(busCode);

        if (isPauseAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), busCode)) {
            return alarm;
        }

        AlarmLevelEnum alarmLevelEnum = processSpeedAlarmLevel(busCode, speed);
        if (alarmLevelEnum == null) {
            return alarm;
        }

        if (isSameAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), alarmLevelEnum, busCode)) {
            return alarm;
        }

        alarm.setAlarmLevel(alarmLevelEnum);
        alarm.setLat(latitude.toPlainString());
        alarm.setLon(longitude.toPlainString());

        buildAlarmStatus(alarm, alarmLevelEnum);

        // 获得高德地图位置信息
        String formattedAddress = "未知";
        try {
            formattedAddress = mapAmapService.getLocationRegeo(longitude, latitude);
        } catch (BusApiException e) {
            e.printStackTrace();
        }
        alarm.setLocation(formattedAddress);

        User driver = userService.getDriverByBusCode(busCode);
        if (driver != null) {
            alarm.setDriverName(driver.getRealname());
            alarm.setDriverId(driver.getUserId());
            alarm.setDriverMobile(driver.getMobile());
        }

        Bus bus = busService.getByBusCode(busCode);
        if (bus != null) {
            alarm.setNumberPlate(bus.getNumberPlate());
        }

        StringBuilder detail = new StringBuilder();
        detail.append(BusApiAlarmContants.BUS_SPEED).append(":").append(speed).append(";");
        detail.append(BusApiAlarmContants.BUS_SPEED_ALARM).append(":").append(alarmConfig.getSpeed()).append(";");
        detail.append(BusApiAlarmContants.BUS_SPEED_LOCATION).append(":").append(formattedAddress).append(";");

        alarm.setAlarmDetail(detail.toString());
        this.save(alarm);
        alarmService.pushWebMessage(alarm);
        this.sameAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), alarm.getAlarmLevel(), alarm.getBusCode());
        return alarm;
    }

    public void buildAlarmStatus(AlarmBus alarm, AlarmLevelEnum alarmLevelEnum) {
        if (AlarmLevelEnum.DELAY.equals(alarmLevelEnum) || AlarmLevelEnum.MIDDLE.equals(alarmLevelEnum)) {
            alarm.setStatus(ProcessingStatusEnum.UNPROCESSED);
        } else {
            alarm.setStatus(ProcessingStatusEnum.PROCESSED);
        }
        alarm.setAlarmLevel(alarmLevelEnum);
    }

    private AlarmLevelEnum processSpeedAlarmLevel(String busCode, Float speed) {
        RBucket<LocalDateTime> bucket = redissonClient.getBucket(BusApiConstant.ALARM_SPEED + "." + busCode);
        LocalDateTime times = bucket.get();
        LocalDateTime now = LocalDateTime.now();
        AlarmLevelEnum level = AlarmLevelEnum.SLIGHT;
        if (speed > alarmConfig.getSpeed()) {
            if (times == null) {
                // 第一次产生，并不告警
                bucket.set(now, alarmConfig.getSpeedTimeUpMinute(), TimeUnit.MINUTES);
            } else {
                // 非首次，连续产生，产生告警
                Duration duration = Duration.between(times, now);
                if (duration.toMinutes() >= alarmConfig.getSpeedTimeUpMinute()) {
                    // 超过限制时间还在超速
                    level = AlarmLevelEnum.MIDDLE;
                }
                // 延期
                bucket.expire(alarmConfig.getSpeedTimeUpMinute(), TimeUnit.MINUTES);
            }
        } else {
            bucket.delete();
        }
        return level;
    }

    @Override
    @Transactional
    public boolean saveStopDelayAlarm(String busCode, DvrLocation dvrLocation, AlarmLevelEnum alarmLevel, String stopName, long durationMin, int threshold) {

        AlarmBus alarm = new AlarmBus();
        alarm.setCreateTime(LocalDateTime.now());
        alarm.setAlarmType(AlarmTypeEnum.STOP);
        alarm.setAlarmSubType(AlarmSubTypeEnum.DELAY);
        alarm.setAlarmLevel(alarmLevel);
        alarm.setBusCode(busCode);
        if (dvrLocation != null) {
            alarm.setLat(dvrLocation.getGlat().toPlainString());
            alarm.setLon(dvrLocation.getGlon().toPlainString());
        }

        if (isPauseAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), busCode)) {
            return false;
        }

        if (isSameAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), alarm.getAlarmLevel(), busCode + "." + stopName)) {
            return false;
        }

        // 获得高德地图位置信息
        String formattedAddress = "未知";
        try {
            if (dvrLocation != null) {
                formattedAddress = mapAmapService.getLocationRegeo(dvrLocation.getGlon(), dvrLocation.getGlat());
            }
        } catch (BusApiException e) {
            e.printStackTrace();
        }
        alarm.setLocation(formattedAddress);

        User driver = userService.getDriverByBusCode(busCode);
        if (driver != null) {
            alarm.setDriverName(driver.getRealname());
            alarm.setDriverId(driver.getUserId());
            alarm.setDriverMobile(driver.getMobile());
        }

        Bus bus = busService.getByBusCode(busCode);
        if (bus != null) {
            alarm.setNumberPlate(bus.getNumberPlate());
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(BusApiAlarmContants.BUS_STOP_NAME).append(":").append(stopName).append(";");
        buffer.append(BusApiAlarmContants.BUS_STOP_THRESHOLD).append(":").append(threshold).append(";");
        buffer.append(BusApiAlarmContants.BUS_STOP_DELAY).append(":").append(durationMin).append(";");
        buffer.append(BusApiAlarmContants.BUS_STOP_LOCATION).append(":").append(formattedAddress).append(";");

        alarm.setAlarmDetail(buffer.toString());
        buildAlarmStatus(alarm, alarm.getAlarmLevel());
        this.save(alarm);
        alarmService.pushWebMessage(alarm);
        this.sameAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), alarm.getAlarmLevel(), busCode + "." + stopName);
        return true;
    }

    @Override
    public AlarmBus saveDvrOffineAlarm(BindBusDetailInfo b, DvrLocation location, Long duration, Integer threshold) {

        AlarmBus alarm = new AlarmBus();
        alarm.setCreateTime(LocalDateTime.now());
        alarm.setAlarmType(AlarmTypeEnum.BUS);
        alarm.setAlarmSubType(AlarmSubTypeEnum.MONITORED);
        alarm.setBusCode(b.getBusCode());
        alarm.setDriverName(b.getDriverName());
        alarm.setDriverId(b.getBindDriverId());
        alarm.setDriverMobile(b.getDriverMobile());
        alarm.setNumberPlate(b.getNumberPlate());

        String formattedAddress = "没有位置信息";

        if (location != null) {
            alarm.setLat(location.getGlat().toPlainString());
            alarm.setLon(location.getGlon().toPlainString());

            // 获得高德地图位置信息
            try {
                formattedAddress = mapAmapService.getLocationRegeo(location.getGlon(), location.getGlat());
            } catch (BusApiException e) {
                e.printStackTrace();
            }

            alarm.setLocation(formattedAddress);
        }

        if (isPauseAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), b.getBusCode())) {
            return alarm;
        }

        if (isSameAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), alarm.getAlarmLevel(), b.getBusCode())) {
            return alarm;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(BusApiAlarmContants.BUS_OFFINE_CODE).append(":").append(b.getBusCode()).append(";");
        buffer.append(BusApiAlarmContants.BUS_OFFINE_THRESHOLD).append(":").append(threshold).append(";");
        buffer.append(BusApiAlarmContants.BUS_OFFINE_TIME).append(":").append(duration).append(";");
        buffer.append(BusApiAlarmContants.BUS_OFFINE_LOCATION).append(":").append(formattedAddress).append(";");

        alarm.setAlarmDetail(buffer.toString());
        this.save(alarm);
        this.buildAlarmStatus(alarm, AlarmLevelEnum.MIDDLE);
        this.sameAlarm(alarm.getAlarmType(), alarm.getAlarmSubType(), alarm.getAlarmLevel(), alarm.getBusCode());
        return alarm;
    }

    @Override
    public AlarmBus getLastAlarmInfo(AlarmSubTypeEnum subType, String busCode) {
        return baseMapper.getLastAlarmInfo(subType, busCode);
    }

    @Override
    public void pauseAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, String busCode, LocalDateTime expireTime) {
        String key = getPauseAlarmKey(alarmType, alarmSubType, busCode);
        RAtomicLong bucket = redissonClient.getAtomicLong(key);
        if (!bucket.isExists()) {
            bucket.set(1);
        } else {
            bucket.incrementAndGet();
        }
        Date date = Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant());
        bucket.expireAt(date);
    }

    @Override
    public boolean isPauseAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, String busCode) {
        String key = getPauseAlarmKey(alarmType, alarmSubType, busCode);
        RAtomicLong bucket = redissonClient.getAtomicLong(key);
        return bucket.isExists();
    }

    public String getPauseAlarmKey(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, String busCode) {
        return Constants.ALARM_PAUSE_PREFIX + alarmType + "." + alarmSubType + "." + busCode;
    }

    @Override
    public void sameAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, AlarmLevelEnum alarmLevel, String busCode) {
        String key = getSameAlarmKey(alarmType, alarmSubType, alarmLevel, busCode);
        RAtomicLong bucket = redissonClient.getAtomicLong(key);
        if (!bucket.isExists()) {
            bucket.set(1);
        } else {
            bucket.incrementAndGet();
        }
        bucket.expire(20, TimeUnit.MINUTES);
    }

    @Override
    public boolean isSameAlarm(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, AlarmLevelEnum alarmLevel, String busCode) {
        String key = getSameAlarmKey(alarmType, alarmSubType, alarmLevel, busCode);
        RAtomicLong bucket = redissonClient.getAtomicLong(key);
        return bucket.isExists();
    }

    public String getSameAlarmKey(AlarmTypeEnum alarmType, AlarmSubTypeEnum alarmSubType, AlarmLevelEnum alarmLevel, String busCode) {
        return Constants.ALARM_SAME_PREFIX + alarmType + "." + alarmSubType + "." + alarmLevel + "." + busCode;
    }
}
