package com.phlink.bus.api.alarm.service.impl;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.alarm.domain.AlarmBus;
import com.phlink.bus.api.alarm.domain.AlarmConfig;
import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.domain.BusTripTime;
import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.service.IAlarmBusService;
import com.phlink.bus.api.alarm.service.IAlarmRouteRulesService;
import com.phlink.bus.api.alarm.service.IBusAlarmService;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.fence.domain.Fence;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.map.response.AmapDistanceResultEntity;
import com.phlink.bus.api.map.response.BaiduLocationStatus;
import com.phlink.bus.api.map.response.BaiduLocationStatusResultEntity;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.map.service.IMapBaiduService;
import com.phlink.bus.api.notify.event.AlarmBusMappingEvent;
import com.phlink.bus.api.notify.event.AlarmRouteDeviationEvent;
import com.phlink.bus.api.notify.event.AlarmSpeedEvent;
import com.phlink.bus.api.notify.event.AlarmStopEnterEvent;
import com.phlink.bus.api.route.domain.StopTimeStudentDetail;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BusAlarmServiceImpl implements IBusAlarmService {

    private static final int high_threshold = 10;
    private static final int low_threshold = 5;
    @Autowired
    private RedisService redisService;
    @Autowired
    private IAlarmRouteRulesService alarmRouteRulesService;
    @Autowired
    private IMapAmapService amapService;
    @Autowired
    private IMapBaiduService baiduService;
    @Autowired
    private IAlarmBusService alarmBusService;
    @Autowired
    private ITripService tripService;
    @Autowired
    private AlarmConfig alarmConfig;
    @Autowired
    private IFenceService fenceService;
    @Autowired
    private IBusService busService;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private RedissonClient redissonClient;

    @Async
    @Override
    public void alarmBusMapping(DvrLocation dvrLocation) {
        String code = dvrLocation.getBusCode();
        Bus bus = busService.getById(dvrLocation.getBusId());
        if (bus == null) {
            log.error("[BusAlarm-{}] 错误的车辆编号，无法找到车辆", code);
            return;
        }
        try {
            //先保存最新位置（GPS坐标）
            this.redisService.hset(BusApiConstant.LOCATION, code, dvrLocation.getLon() + "," + dvrLocation.getLat() + "," + dvrLocation.getGpstime() / 1000);
            // 获取规则
            AlarmRouteRules rules = alarmRouteRulesService.getByBusCode(code);
            if (invalidDateCheck(rules, bus.getBusCode(), bus.getId())) {
                return;
            }
            if (bus.getTid() != 0) {
                context.publishEvent(new AlarmBusMappingEvent(this, rules, dvrLocation));
            } else {
                log.error("[BusAlarm-{}] 该车辆没有注册或注册失败！", code);
            }
        } catch (RedisConnectException e) {
            log.error("[BusAlarm-{}] Redis连接异常", code);
        }
    }


    /**
     * 查询路线告警
     */
    @Async
    @Override
    public void asyncBuildRouteAlarm(AlarmRouteRules rules, DvrLocation dvrLocation) {
        Long fenceId = rules.getFenceId();
        if (fenceId == null) {
            log.error("[BusAlarm-{}] 规则{}-{}没有配置围栏ID", rules.getFenceName(), rules.getId(), rules.getRuleName());
            return;
        }
        Fence fence = fenceService.getById(fenceId);
        if (fence == null) {
            log.error("[BusAlarm-{}] 规则{}-{}没有配置围栏ID", rules.getFenceName(), rules.getId(), rules.getRuleName());
            return;
        }
        Bus bus = busService.findById(dvrLocation.getBusId());
        //使用百度坐标
        BaiduLocationStatusResultEntity resultEntity = baiduService.queryLocationStatus(bus.getEntityName(), Collections.singletonList(fence.getFenceId()), dvrLocation.getBlon().toPlainString(), dvrLocation.getBlat().toPlainString());
        if (resultEntity.isSuccess()) {
            List<BaiduLocationStatus> status = resultEntity.getMonitored_statuses();
            for (BaiduLocationStatus s : status) {
                if (StringUtils.isNotBlank(s.getMonitored_status()) && !"in".equals(s.getMonitored_status())) {
                    // 计算距离
                    String vertexes = fence.getVertexes();
                    List<double[]> points = JSON.parseArray(vertexes, double[].class);
                    List<String> origins = points.stream().map(p -> p[0] + "," + p[1]).collect(Collectors.toList());
                    int distance = 0;
                    try {
                        AmapDistanceResultEntity entity = amapService.getDistance(origins, dvrLocation.getGlon() + "," + dvrLocation.getGlat(), "0");
                        if (entity.requestSuccess()) {
                            distance = getMinDistance(entity);
                        }
                    } catch (BusApiException e) {
                        e.printStackTrace();
                    }
                    // 需要用高德的坐标计算位置
                    AlarmBus alarm = alarmBusService.saveRouteDeviationAlarm(dvrLocation.getBusCode(), distance, dvrLocation.getGlon(), dvrLocation.getGlat());
                    if (alarm.getId() != null) {
                        if (AlarmLevelEnum.MIDDLE.equals(alarm.getAlarmLevel()) || AlarmLevelEnum.DELAY.equals(alarm.getAlarmLevel())) {
                            //超速通知车队队长
                            context.publishEvent(new AlarmRouteDeviationEvent(this, dvrLocation));
                        }
                    }
                } else {
                    // 在围栏里
                    // 暂时不用做处理
                }
            }
        } else {
            log.error("[BusAlarm-{}] 百度围栏获取失败, {}", rules.getFenceName(), JSON.toJSONString(resultEntity));
        }
    }

    /**
     * @Description: 通过计算两点之间的距离判断是否产生站点告警
     * @Param: [rules, dvrLocation]
     * @Return: void
     * @Author wen
     * @Date 2019/11/8 15:42
     */
    @Async
    @Override
    public void asyncBuildStopAlarm(AlarmRouteRules rules, DvrLocation dvrLocation) {
        String busCode = dvrLocation.getBusCode();
        // 当前tripstate
        TripState state = tripService.getCurrentRunningTrip(busCode);
        if (state == null) {
            log.info("[BusStopAlarm-{}] 当前没有运行的行程", busCode);
            return;
        }
        // 所有的站点时刻列表
        List<StopTimeStudentDetail> allStopTimelist = state.getStopTimes();
        // 已经完成的站点时刻
        RList<StopTimeStudentDetail> completeStopTimelist = redissonClient.getList(Constants.QUEUE_COMPLETE_STOP_TIME_PREFIX + state.getId() + "." + busCode);
        if (allStopTimelist.isEmpty()) {
            // 已经完成行程
            log.info("[BusStopAlarm-{}] 没有相关站点时刻列表", busCode);
            return;
        }
        // 计算距离
        List<String> stopOrigins = allStopTimelist.stream().map(s -> s.getStopLon() + "," + s.getStopLat()).collect(Collectors.toList());
        try {
            AmapDistanceResultEntity resultEntity = amapService.getDistance(stopOrigins, dvrLocation.getGlon() + "," + dvrLocation.getGlat(), "0");
            if (resultEntity.requestSuccess()) {
                List<AmapDistanceResultEntity.AmapDistanceResult> results = resultEntity.getResults();
                if (results == null || results.isEmpty()) {
                    return;
                }
                for (AmapDistanceResultEntity.AmapDistanceResult distanceResult : results) {
                    StopTimeStudentDetail stopTime = allStopTimelist.get(distanceResult.getOrigin_id() - 1);
                    boolean isEnter = false;
                    for (StopTimeStudentDetail complete : completeStopTimelist) {
                        if (complete.getStopId().equals(stopTime.getStopId())) {
                            // 已经进过站
                            log.info("[BusStopAlarm-{}] 已经经过[{}]站点", busCode, stopTime.getStopName());
                            isEnter = true;
                            break;
                        }
                    }
                    if (isEnter) {
                        continue;
                    }
                    Integer distance = distanceResult.getDistance();
                    if (distance <= alarmConfig.getStopDistance()) {
                        log.info("[BusStopAlarm-{}] 进入站点，时间：{}，站点名称：{}", busCode, LocalTime.now(), stopTime.getStopName());
                        // 保存进站车辆相关信息
                        stopTime.setArrivalTime(LocalTime.now());
                        completeStopTimelist.add(stopTime);
                        // 进站，保存到已完成站点队列
                        int index = allStopTimelist.indexOf(stopTime);
                        // 通知下一站到站站点
                        int nextIndex = index + 1;
                        if (nextIndex < allStopTimelist.size()) {
                            StopTimeStudentDetail nextStopTime = allStopTimelist.get(nextIndex);
                            context.publishEvent(new AlarmStopEnterEvent(this, stopTime, nextStopTime, state));
                        }
                    }else{
                        log.info("[BusStopAlarm-{}] 距离附近[{}]站点{}米                     站点[{},{}], 车辆[{},{}]", busCode, stopTime.getStopName(), distance, stopTime.getStopLon(), stopTime.getStopLat(), dvrLocation.getGlon(), dvrLocation.getGlat());
                    }
                }

            }
        } catch (BusApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 失效时间判断，只有在告警时间范围内才告警
     * @Param: [rules, bus]
     * @Return: java.lang.Boolean
     * @Author wen
     * @Date 2019/11/8 15:22
     */
    @Override
    public Boolean invalidDateCheck(AlarmRouteRules rules, String busCode, Long busId) {
        if (rules != null) {
            //周末开关
            if (DateUtil.defineWeekEndToday()) {
                if (!rules.getWeekendSwitch()) {
                    // 不告警
                    return true;
                }
            }

            // 在失效时间段
            if (rules.getInvalidEndDate() != null && rules.getInvalidStartDate() != null) {
                LocalDate today = LocalDate.now();
                if ((rules.getInvalidStartDate().isBefore(today) || rules.getInvalidStartDate().isEqual(today)) &&
                        (today.isBefore(rules.getInvalidEndDate()) || rules.getInvalidEndDate().isEqual(today))) {
                    // 不告警
                    return true;
                }
            }
            // 在运营时间段
            List<BusTripTime> busTripTimes = busService.listBusTripTime(busId);
            LocalTime nowTime = LocalTime.now();
            for (BusTripTime tripTime : busTripTimes) {
                if (tripTime.getStartTime().isBefore(nowTime) && nowTime.isBefore(tripTime.getEndTime())) {
                    // 告警
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 车辆告警处理
     *
     * @param dvrLocation
     */
    @Async
    @Override
    public void asyncBuildBusAlarm(DvrLocation dvrLocation) {
        AlarmBus alarm = this.alarmBusService.saveSpeedingAlarm(dvrLocation.getBusCode(), dvrLocation.getSpeed().floatValue(), dvrLocation.getGlon(), dvrLocation.getGlat());
        if (AlarmLevelEnum.MIDDLE.equals(alarm.getAlarmLevel()) || AlarmLevelEnum.DELAY.equals(alarm.getAlarmLevel())) {
            //超速通知车队队长
            context.publishEvent(new AlarmSpeedEvent(this, dvrLocation));
        }
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