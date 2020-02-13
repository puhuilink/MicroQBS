package com.phlink.bus.api.alarm;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.alarm.domain.AlarmBus;
import com.phlink.bus.api.alarm.domain.AlarmConfig;
import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.domain.BusTripTime;
import com.phlink.bus.api.alarm.domain.enums.AlarmLevelEnum;
import com.phlink.bus.api.alarm.service.IAlarmBusService;
import com.phlink.bus.api.alarm.service.IAlarmRouteRulesService;
import com.phlink.bus.api.alarm.service.IBusAlarmService;
import com.phlink.bus.api.bus.domain.BindBusDetailInfo;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.lisenter.service.CacheDeviceInfoService;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.common.utils.SpringContextUtil;
import com.phlink.bus.api.notify.event.AlarmBusDisconnectEvent;
import com.phlink.bus.api.notify.event.AlarmStopLateEvent;
import com.phlink.bus.api.route.domain.StopTimeStudentDetail;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BusAlarmTask {
    private static final int high_threshold = 10;
    private static final int low_threshold = 5;

    @Autowired
    private CacheDeviceInfoService cacheDeviceInfoService;
    @Autowired
    private IAlarmBusService alarmBusService;
    @Autowired
    private IBusAlarmService busAlarmService;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private IAlarmRouteRulesService alarmRouteRulesService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private AlarmConfig alarmConfig;
    @Autowired
    private IBusService busService;
    @Autowired
    private ITripService tripService;

    /**
     * @Description: 每一分钟执行一次
     * @Param: []
     * @Return: void
     * @Author wen
     * @Date 2019/11/22 17:47
     */
    @Async
    @Scheduled(cron = "0 */1 * * * ?")
    public void initNeedCheckBindBusDetailAsync() {
        if (!SpringContextUtil.isPro()) {
            return;
        }
        try {
            initNeedCheckBindBusDetail();
        } catch (Exception e) {
            log.error("initNeedCheckBindBusDetailAsync ", e);
        }
    }

    public void initNeedCheckBindBusDetail() {
        // 获取所有绑定车辆
        List<BindBusDetailInfo> busList = busService.listBindBusDetailInfo();
        if (busList == null || busList.isEmpty()) {
            return;
        }
        String key = Constants.QUEUE_BINDBUS_NEEDCHECK;
        RBlockingQueue<BindBusDetailInfo> rlist = redissonClient.getBlockingDeque(key);

        List<BindBusDetailInfo> needCheck = busList.stream().filter(b -> StringUtils.isNotBlank(b.getDvrCode())).collect(Collectors.toList());
        if (needCheck.isEmpty()) {
            return;
        }
        if (!rlist.isExists() || rlist.isEmpty()) {
            rlist.addAll(needCheck);
        } else {
            log.info("[BUS-offlineAlarm] 队列{}已存在", key);
        }
    }

    @Async
    @Scheduled(cron = "20 */1 * * * ?")
    public void offlineAlarmAsync() {
        if (!SpringContextUtil.isPro()) {
            return;
        }
        try {
            offlineAlarm();
        } catch (Exception e) {
            log.error("offlineAlarmAsync ", e);
        }
    }

    @Async
    @Scheduled(cron = "30 */1 * * * ?")
    public void stopDelayAlarmAsync() {
        if (!SpringContextUtil.isPro()) {
            return;
        }
        log.info("站点迟到检查定时任务开启");
        try {
            stopDelayAlarm();
        } catch (Exception e) {
            log.error("stopDelayAlarmAsync ", e);
        }
        log.info("站点迟到检查定时任务完成");
    }

    public void offlineAlarm() {
        log.info("车辆失联定时任务开启");
        // 获取所有绑定车辆
        String key = Constants.QUEUE_BINDBUS_NEEDCHECK;
        RBlockingQueue<BindBusDetailInfo> rqueue = redissonClient.getBlockingDeque(key);
        if (!rqueue.isExists() || rqueue.isEmpty()) {
            return;
        }
        while (!rqueue.isEmpty()) {
            BindBusDetailInfo b = rqueue.poll();
            if (b == null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            // 获取规则
            AlarmRouteRules rules = alarmRouteRulesService.getByRoute(b.getRouteId());
            if (rules != null) {
                if (busAlarmService.invalidDateCheck(rules, b.getBusCode(), b.getId())) {
                    log.info("[offlineAlarm] {} 不在有效运营时间范围", b.getBusCode());
                    continue;
                }
                String offlineKey = Constants.BUS_OFFLINE_ALARM_PREFIX + b.getBusCode();
                RBucket<Long> offlineBusAlarm = redissonClient.getBucket(offlineKey);
                // 是否之前存在告警
                if (offlineBusAlarm.isExists()) {
                    log.info("[offlineAlarm] {} redis的{}已经存在", b.getBusCode(), offlineKey);
                    continue;
                }
                log.info("[offlineAlarm] {} redis的{}不存在", b.getBusCode(), offlineKey);
                // 判断是否离线超时
                DvrLocation dvrLocation = cacheDeviceInfoService.getLastDvr(b.getDvrCode());
                // 运营时间段
                List<BusTripTime> busTripTimes = busService.listBusTripTime(b.getId());
                LocalTime nowTime = LocalTime.now();
                LocalTime startTime = null;
                for (BusTripTime tripTime : busTripTimes) {
                    if (tripTime.getStartTime().isBefore(nowTime) && nowTime.isBefore(tripTime.getEndTime())) {
                        startTime = tripTime.getStartTime();
                        break;
                    }
                }
                if (startTime == null) {
                    continue;
                }
                // GPS时间
                LocalTime gpsDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(dvrLocation.getGpstime()), ZoneId.systemDefault()).toLocalTime();
                // 和行程开始时间的比较，本身就在开始时间之前
                Duration offlineDuartion = null;
                if (gpsDateTime.isBefore(startTime)) {
                    offlineDuartion = Duration.between(startTime, nowTime);
                } else {
                    offlineDuartion = Duration.between(gpsDateTime, nowTime);
                }
                if (offlineDuartion.toMinutes() < alarmConfig.getOfflineTimeMinute()) {
                    log.info("[offlineAlarm] {} 未超时，duration={}分钟", b.getBusCode(), offlineDuartion.toMinutes());
                    return;
                }
                log.info("[offlineAlarm] {} 产生告警拉，duration={}分钟", b.getBusCode(), offlineDuartion.toMinutes());
                AlarmBus alarm = this.alarmBusService.saveDvrOffineAlarm(b, dvrLocation, offlineDuartion.toMinutes(), alarmConfig.getOfflineTimeMinute());
                if (alarm.getId() != null) {
                    if (AlarmLevelEnum.MIDDLE.equals(alarm.getAlarmLevel()) || AlarmLevelEnum.DELAY.equals(alarm.getAlarmLevel())) {
                        //离线通知车队队长
                        context.publishEvent(new AlarmBusDisconnectEvent(this, b.getBusCode()));
                    }
                    offlineBusAlarm.set(startTime.toNanoOfDay(), alarmConfig.getOfflineMinute(), TimeUnit.MINUTES);
                }
            }
        }
        log.info("车辆失联定时任务完成");
    }

    public void stopDelayAlarm() {
        Iterable<String> keyIterable = redissonClient.getKeys().getKeysByPattern(Constants.QUEUE_STOP_TIME_PREFIX + "*");

        for (Iterator<String> iterator = keyIterable.iterator(); iterator.hasNext(); ) {
            String oneKey = iterator.next();
            String busCode = oneKey.replace(Constants.QUEUE_STOP_TIME_PREFIX, "");

            // 所有的站点时刻列表
            // 当前tripstate
            stopDelayProcessAlarm(busCode);
        }
    }

    public void stopDelayProcessAlarm(String busCode) {
        TripState state = tripService.getCurrentRunningTrip(busCode);
        if (state == null) {
            return;
        }
        // 所有的站点时刻列表
//        RList<BindBusStoptimeDetailInfo> allStopTimelist = redissonClient.getList(Constants.QUEUE_STOP_TIME_PREFIX + busCode);
        List<StopTimeStudentDetail> allStopTimelist = state.getStopTimes();
//            RList<BindBusStoptimeDetailInfo> allStopTimelist = redissonClient.getList(Constants.QUEUE_STOP_TIME_PREFIX + busCode);
        // 已经完成的站点时刻
//            RList<StopTimeStudentDetail> completeStopTimelist = redissonClient.getList(Constants.QUEUE_COMPLETE_STOP_TIME_PREFIX + busCode);
        RList<StopTimeStudentDetail> completeStopTimelist = redissonClient.getList(Constants.QUEUE_COMPLETE_STOP_TIME_PREFIX + state.getId() + "." + busCode);
        // 还没有完成的站点时刻
        List<StopTimeStudentDetail> noCompleteStopTimeList = allStopTimelist.stream().filter(t -> {
            for (StopTimeStudentDetail completeStopTime : completeStopTimelist) {
                if (completeStopTime.getStopId().equals(t.getStopId())) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        for (StopTimeStudentDetail stopTime : noCompleteStopTimeList) {
            if (LocalTime.now().isAfter(stopTime.getArrivalTime())) {
                Duration duration = Duration.between(stopTime.getArrivalTime(), LocalTime.now());
                long durationMin = duration.toMinutes();
                if (durationMin >= alarmConfig.getStopTimeMinute()) {
                    log.info("[stopDelayAlarm] 车辆[{}]在站点[{}]超过10分钟，到达时间[{}], 延期[{}分钟]", busCode, stopTime.getStopName(), stopTime.getArrivalTime(), duration.toMinutes());
                    // 获取规则
                    AlarmRouteRules rules = alarmRouteRulesService.getByRoute(state.getRouteId());
                    if (rules != null && rules.getStopSwitch() != null && rules.getStopSwitch()) {
                        if (invalidDateCheck(rules, busCode, state.getBusDetailInfo().getId())) {
                            log.info("[stopDelayAlarm] 车辆[{}] 不在有效运营时间范围", busCode);
                            continue;
                        }
                        log.info("[stopDelayAlarm] 车辆[{}] AlarmRouteRules = {}", busCode, JSON.toJSONString(rules));
                        AlarmLevelEnum alarmLevel;
                        int threshold;
                        if (durationMin > low_threshold && durationMin < high_threshold) {
                            alarmLevel = AlarmLevelEnum.SLIGHT;
                            threshold = low_threshold;
                        } else if (durationMin >= high_threshold) {
                            alarmLevel = AlarmLevelEnum.MIDDLE;
                            threshold = high_threshold;
                        } else {
                            continue;
                        }
                        try {
                            DvrLocation dvrLocation = cacheDeviceInfoService.getLastDvr(state.getBusDetailInfo().getDvrCode());
                            boolean isAlarm = this.alarmBusService.saveStopDelayAlarm(busCode, dvrLocation, alarmLevel, stopTime.getStopName(), durationMin, threshold);
                            if (isAlarm) {
                                log.info("[stopDelayAlarm] 车辆[{}]在站点[{}]迟到{}分钟，并且产生告警", busCode, stopTime.getStopName(), duration.toMinutes());
                                context.publishEvent(new AlarmStopLateEvent(this, dvrLocation));
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    } else {
                        log.info("[stopDelayAlarm] 车辆[{}] 未设置站点告警", busCode);
                    }
                }
            }
        }
    }


    public Boolean invalidDateCheck(AlarmRouteRules rules, String busCode, Long busId) {
        if (rules != null) {
            //周末开关
            if (DateUtil.defineWeekEndToday()) {
                if (rules.getWeekendSwitch()) {
                    // 可以告警
                    return false;
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
                if (tripTime.getStartTime().isBefore(nowTime) && nowTime.isBefore(tripTime.getEndTime().plusMinutes(30))) {
                    // 告警
                    return false;
                }
            }
        }
        return true;
    }
}