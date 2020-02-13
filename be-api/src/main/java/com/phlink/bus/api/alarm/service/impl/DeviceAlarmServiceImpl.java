package com.phlink.bus.api.alarm.service.impl;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.alarm.domain.AlarmSchoolRules;
import com.phlink.bus.api.alarm.service.IAlarmDeviceService;
import com.phlink.bus.api.alarm.service.IAlarmSchoolRulesService;
import com.phlink.bus.api.alarm.service.IDeviceAlarmService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.device.domain.EwatchLocation;
import com.phlink.bus.api.fence.domain.Fence;
import com.phlink.bus.api.fence.domain.FenceTime;
import com.phlink.bus.api.fence.domain.enums.FenceTypeEnum;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.fence.service.IFenceTimeService;
import com.phlink.bus.api.map.domain.FenceEvent;
import com.phlink.bus.api.map.domain.FenceStatus;
import com.phlink.bus.api.map.response.AmapDistanceResultEntity;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.notify.event.AlarmStudentLeaveSchoolEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceAlarmServiceImpl implements IDeviceAlarmService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private IAlarmSchoolRulesService alarmSchoolRulesService;
    @Autowired
    private IAlarmDeviceService alarmDeviceService;
    @Autowired
    private IMapAmapService amapService;
    @Autowired
    private IFenceService fenceService;
    @Autowired
    private IFenceTimeService fenceTimeService;
    @Autowired
    private ApplicationContext context;

    @Async
    @Override
    public void alarmDeviceMapping(EwatchLocation ewatchLocation) {
        String code = ewatchLocation.getDeviceId();
        try {
            //无论如何先保存最新位置
            this.redisService.hset(BusApiConstant.LOCATION, code, ewatchLocation.getLongitude() + "," + ewatchLocation.getLatitude() + "," + ewatchLocation.getTimestamp() / 1000);
            // 获取规则
            List<AlarmSchoolRules> rules = alarmSchoolRulesService.getByDeviceId(code);
            log.info("[DeviceAlarm] 获取设备对应的规则{} 设备ID:{}", JSON.toJSONString(rules), code);

            List<AlarmSchoolRules> availableRules = new ArrayList<>();
            for(AlarmSchoolRules rule : rules) {
                //检查当前时间是否在失效时间内
                if (!isAvailbale(rule, code)) {
                    log.info("[DeviceAlarm] 设备ID:{}在失效范围内不处理", code);
                    continue;
                }
                availableRules.add(rule);
            }

            String tid = redisService.hget(BusApiConstant.DEVICE, code);
            log.info("[DeviceAlarm] 获取设备ID:{}高德注册终端ID:{}", code, tid);
            if (StringUtils.isNotBlank(tid) && !availableRules.isEmpty()) {
                deviceAlarm(availableRules, ewatchLocation);
            } else {
                log.error("[DeviceAlarm] 该设备没有注册或注册失败！设备ID:{}", code);
            }
        } catch (RedisConnectException | BusApiException e) {
            log.error("[DeviceAlarm] 告警异常", e);
        }
    }

    private boolean isAvailbale(AlarmSchoolRules rules, @NotNull String code) {
        if (rules == null) {
            log.info("[DeviceAlarm] 设备{}没有对应的告警规则", code);
            return false;
        }
        //周末开关
        if (DateUtil.defineWeekEndToday()) {
            if (!rules.getWeekendSwitch()) {
                // 不告警
                return false;
            }
        }
        LocalDate today = LocalDate.now();
        if (rules.getInvalidEndDate() != null && rules.getInvalidStartDate() != null) {
            // 在失效时间
            if ((rules.getInvalidStartDate().isBefore(today) || rules.getInvalidStartDate().isEqual(today)) &&
                    (today.isBefore(rules.getInvalidEndDate()) || rules.getInvalidEndDate().isEqual(today))) {
                return false;
            }
        }
        //手环开关
        if (rules.getDeviceSwitch()) {
            return true;
        }
        // 是否在生效时间范围内
        List<FenceTime> fenceTimes = fenceTimeService.listByFenceId(rules.getFenceId());
        LocalTime nowTime = LocalTime.now();
        for(FenceTime time : fenceTimes) {
            if (time.getStartTime().isBefore(nowTime) && nowTime.isBefore(time.getEndTime())) {
                // 只要有一个在生效时间内就返回
                return true;
            }
        }

        return false;
    }

    /**
     * 设备告警
     *
     * @param ewatchLocation
     */
    private void deviceAlarm(List<AlarmSchoolRules> rules, EwatchLocation ewatchLocation) throws BusApiException {

        // 查看围栏监控
        FenceStatus fenceStatus = amapService.getAlarmFence(ewatchLocation.getDeviceId(), ewatchLocation.getLongitude().doubleValue(), ewatchLocation.getLatitude().doubleValue(), ewatchLocation.getTimestamp() / 1000);
        if (fenceStatus == null) {
            return;
        }
        List<FenceEvent> fenceEventList = fenceStatus.getFencing_event_list();

        List<Long> fenceIds = rules.stream().map(AlarmSchoolRules::getFenceId).filter(Objects::nonNull).collect(Collectors.toList());
        if(fenceIds.isEmpty()) {
            return;
        }
        Collection<Fence> fences = fenceService.listByIds(fenceIds);
        Map<String, Fence> fencesIdMap = new HashMap<>();
        for(Fence f : fences) {
            if(StringUtils.isNotBlank(f.getFenceId())) {
                fencesIdMap.put(f.getFenceId(), f);
            }
        }

        boolean isOutFlag = false;
        Fence schoolFence = null;
        for (FenceEvent fenceEvent : fenceEventList) {
            if(fencesIdMap.containsKey(fenceEvent.getFence_info().getFence_gid())) {
                schoolFence = fencesIdMap.get(fenceEvent.getFence_info().getFence_gid());
                if ("in".equals(fenceEvent.getClient_status())) {
                    log.info("[DeviceAlarm] 设备{}正常，在围栏{}中", ewatchLocation.getDeviceId(), fenceEvent.getFence_info().getFence_gid());
                    // 只要在一个围栏里就行
                    return;
                }
                isOutFlag = true;
                break;
            }
        }
        if(!isOutFlag) {
            if(fencesIdMap.containsKey(fenceStatus.getNearest_fence_gid())) {
                schoolFence = fencesIdMap.get(fenceStatus.getNearest_fence_gid());
            }
        }
        Integer dis = null;
        if(schoolFence != null) {
            log.info("[DeviceAlarm] 设备不在围栏里 {}", schoolFence.getFenceName());
            dis = getLeaveDistance(ewatchLocation, schoolFence);
        }else{
            for(Fence f : fences) {
                log.info("[DeviceAlarm] 设备不在围栏里 {}", f.getFenceName());
                Integer disInt = getLeaveDistance(ewatchLocation, f);
                if(dis == null || disInt < dis) {
                    dis = disInt;
                }
            }
        }
        if(dis != null && dis > 0) {
            boolean isAlarm = this.alarmDeviceService.saveDeviceAlarm(ewatchLocation.getDeviceId(),  dis + "米", ewatchLocation.getLongitude(), ewatchLocation.getLatitude());
            if(isAlarm) {
                // 发送消息给监护人
                context.publishEvent(new AlarmStudentLeaveSchoolEvent(this, ewatchLocation));
            }
        }else{
            log.error("[DeviceAlarm] 距离围栏不合法 dis = {} schoolFence = {} fence = {}", dis, JSON.toJSONString(schoolFence), JSON.toJSONString(fences));
        }
    }

    private Integer getLeaveDistance(EwatchLocation ewatchLocation, Fence schoolFence) throws BusApiException {
        Integer dis = null;
        // 离开的距离
        if(FenceTypeEnum.CIRCLE.equals(schoolFence.getFenceType())) {
            List<String> origins = Collections.singletonList(ewatchLocation.getLongitude().toPlainString() + "," + ewatchLocation.getLatitude().toPlainString());
            String destination = schoolFence.getCenter();
            AmapDistanceResultEntity distanceResultEntity = amapService.getDistance(origins, destination, "0");
            if(distanceResultEntity.requestSuccess()) {
                List<AmapDistanceResultEntity.AmapDistanceResult> results = distanceResultEntity.getResults();
                if(results != null && !results.isEmpty()) {
                    AmapDistanceResultEntity.AmapDistanceResult distanceResult = results.get(0);
                    dis = distanceResult.getDistance() - schoolFence.getRadius();
                }
            }
        }else if(FenceTypeEnum.POLYGON.equals(schoolFence.getFenceType())) {
            String vertexes = schoolFence.getVertexes();
            List<double[]> points = JSON.parseArray(vertexes, double[].class);
            List<String> origins = points.stream().map(p -> p[0] + "," + p[1]).collect(Collectors.toList());
            String destination = ewatchLocation.getLongitude().toPlainString() + "," + ewatchLocation.getLatitude().toPlainString();
            AmapDistanceResultEntity distanceResultEntity = amapService.getDistance(origins, destination,"0");
            if(distanceResultEntity.requestSuccess()) {
                dis = getMinDistance(distanceResultEntity);
            }
        }
        return dis;
    }

    private int getMinDistance(AmapDistanceResultEntity entity) {
        List<AmapDistanceResultEntity.AmapDistanceResult> results = entity.getResults();
        int distance = 0;
        for (AmapDistanceResultEntity.AmapDistanceResult r : results) {
            if (distance == 0) {
                distance = r.getDistance();
            }
            if (distance > r.getDistance()) {
                distance = r.getDistance();
            }
        }
        return distance;
    }

}
