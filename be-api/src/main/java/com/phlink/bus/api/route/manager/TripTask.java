package com.phlink.bus.api.route.manager;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.bus.domain.BindBusDetailInfo;
import com.phlink.bus.api.bus.domain.BindBusStoptimeDetailInfo;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.SpringContextUtil;
import com.phlink.bus.api.route.domain.StopTimeStudentDetail;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.route.domain.TripTime;
import com.phlink.bus.api.route.domain.enums.TripLogRunningEnum;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import com.phlink.bus.api.route.domain.enums.TripStateRunningEnum;
import com.phlink.bus.api.route.service.ITripLogService;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.api.serviceorg.domain.StudentGuardianInfo;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TripTask {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ITripService tripService;
    @Autowired
    private IBusService busService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ITripLogService tripLogService;

    /**
     * 每天凌晨1点执行，为每辆车初始化行程列表
     */
//    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void initBusTripList() {
        log.info("初始化行程列表");
        // 获取所有绑定车辆
        List<BindBusDetailInfo> busList = busService.listBindBusDetailInfo();
        if (busList == null || busList.isEmpty()) {
            return;
        }
        // 获取这些车辆的行程安排
        for (BindBusDetailInfo bus : busList) {
            String busCode = bus.getBusCode().trim();
            List<TripTime> tripList = tripService.listTripTimeByRoute(bus.getRouteId());
            List<TripState> stateList = tripList.stream().map(t -> {
                TripState s = new TripState();
                BeanUtils.copyProperties(t, s);
                tripStateChange(s, TripStateRunningEnum.WAITE);
                s.setBusDetailInfo(bus);
                s.setId(t.getTripId());
                // 站点时刻列表
                List<StopTimeStudentDetail> stopTimeDetails = tripService.listStopTimeStudentDetailByTripId(t.getTripId(), t.getDirectionId().getValue());
                // 该行程上的所有学生
                List<StudentGuardianInfo> allTripStudents = new ArrayList<>();
                for (StopTimeStudentDetail stopTimeStudentDetail : stopTimeDetails) {
                    if (tripService.checkSchoolStop(s, stopTimeDetails, stopTimeStudentDetail)) {
                        stopTimeStudentDetail.setSchoolStop(true);
                        continue;
                    }
                    stopTimeStudentDetail.setSchoolStop(false);
                    // 站点学生
                    List<StudentGuardianInfo> students = studentService.listStudentGuardianInfoByStop(stopTimeStudentDetail.getStopId(), s.getBusDetailInfo().getRouteOperationId());
                    // 初始化学生队列
                    initStopTimeQueue(busCode, stopTimeStudentDetail.getStopTimeId(), students);
                    if (!students.isEmpty()) {
                        allTripStudents.addAll(students);
                    }
                }
                s.setStopTimes(stopTimeDetails);
                if (!stopTimeDetails.isEmpty()) {
                    if (TripRedirectEnum.BACK.equals(s.getDirectionId())) {
                        StopTimeStudentDetail schoolStopTime = stopTimeDetails.get(0);
                        initStopTimeQueue(busCode, schoolStopTime.getStopTimeId(), allTripStudents);
                    }
                    if (TripRedirectEnum.GO.equals(s.getDirectionId())) {
                        StopTimeStudentDetail schoolStopTime = stopTimeDetails.get(stopTimeDetails.size() - 1);
                        initStopTimeQueue(busCode, schoolStopTime.getStopTimeId(), allTripStudents);
                    }
                }
                initAttendanceQueue(s, busCode);
                return s;
            }).collect(Collectors.toList());
            // 初始化行程队列
            initTripQueue(busCode, stateList);
        }
    }

    /**
     * 每分钟检查一次Trip
     */
    @Scheduled(cron = "0 */1 * * * ?")
    @Async
    @Transactional
    public void checkWaitTripTimeStatus() {

        if(!SpringContextUtil.isPro()) {
            return;
        }
        try {
            String keyPattern = Constants.QUEUE_BINDBUS_WAITING_TRIP_PREFIX + "*";
            Iterable<String> keys = redissonClient.getKeys().getKeysByPattern(keyPattern);
            for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
                String oneKey = iterator.next();
                String busCode = oneKey.replace(Constants.QUEUE_BINDBUS_WAITING_TRIP_PREFIX, "");
                checkWaitTripByBusCode(busCode);
            }
        } catch (Exception e) {
            log.error("checkWaitTripTimeStatus error", e);
        }
    }

    public void checkWaitTripByBusCode(String busCode) throws BusApiException {
        RList<TripState> waitingList = redissonClient.getList(Constants.QUEUE_BINDBUS_WAITING_TRIP_PREFIX + busCode);
        log.info("checkWaitTripTimeStatus get from key = \"{}\" busCode = \"{}\"", Constants.QUEUE_BINDBUS_WAITING_TRIP_PREFIX + busCode, busCode);
        for (TripState tripState : waitingList) {
            String finishedTripKey = Constants.QUEUE_BINDBUS_FINISHED_TRIP_PREFIX + busCode;
            RList<TripState> finishedList = redissonClient.getList(finishedTripKey);

            String runningTripKey = Constants.QUEUE_BINDBUS_RUNNING_TRIP_PREFIX + busCode;
            RList<TripState> runningList = redissonClient.getList(runningTripKey);

            if (isTripFinished(tripState)) {
                // 行程已结束
                waitingList.remove(tripState);
                log.info("checkWaitTripTimeStatus 行程已结束 key = \"{}\" busCode = \"{}\"", finishedTripKey, busCode);
                tripStateChange(tripState, TripStateRunningEnum.FINISH);
                finishedList.add(tripState);
                // 更新tripLog
                tripService.stopTripOnTripLog(busCode, tripState);
            } else if (isTripRunning(tripState)) {
                // 行程进行中的
                waitingList.remove(tripState);
                log.info("checkWaitTripTimeStatus 行程进行中 key = \"{}\" busCode = \"{}\"", finishedTripKey, busCode);
                tripStateChange(tripState, TripStateRunningEnum.RUNNING);
                runningList.add(tripState);
                tripService.startTripOnTripLog(busCode, tripState);
            }
        }
    }

    public void tripStateChange(TripState tripState, TripStateRunningEnum stateRunningEnum) {
        tripState.setRunningState(stateRunningEnum);
//        if(TripStateRunningEnum.RUNNING.equals(stateRunningEnum)) {
//            List<StopTimeStudentDetail> stopTimes = tripState.getStopTimes();
//            if(!stopTimes.isEmpty()) {
//                RMap<String, StopTimeStudentDetail> stopMap = redissonClient.getMap(Constants.CACHE_STOP_TIME_PREFIX + tripState.getBusDetailInfo().getBusCode());
//                // 将第一站放入缓存
//                stopMap.fastPut(Constants.CACHE_STOP_TIME_KEY_NEXT, stopTimes.get(0));
//                // 还未开始行程，下一站清空
//                stopMap.fastRemove(Constants.CACHE_STOP_TIME_KEY_CURRENT);
//            }
//        }
    }

    /**
     * 每5分钟检查一次Trip
     */
    @Scheduled(cron = "0 */1 * * * ?")
    @Async
    public void checkRunningTripTimeStatus() {
        if(!SpringContextUtil.isPro()) {
            return;
        }
        try {
            String keyPattern = Constants.QUEUE_BINDBUS_RUNNING_TRIP_PREFIX + "*";
            Iterable<String> keys = redissonClient.getKeys().getKeysByPattern(keyPattern);
            for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
                String oneKey = iterator.next();
                String busCode = oneKey.replace(Constants.QUEUE_BINDBUS_RUNNING_TRIP_PREFIX, "");
                log.info("checkRunningTripTimeStatus get from key = \"{}\" busCode = \"{}\"", oneKey, busCode);
                checkRunningTripByBusCode(busCode);
            }
        } catch (Exception e) {
            log.error("checkRunningTripTimeStatus error", e);
        }
    }

    public void checkRunningTripByBusCode(String busCode) throws BusApiException {
        RList<TripState> runningList = redissonClient.getList(Constants.QUEUE_BINDBUS_RUNNING_TRIP_PREFIX + busCode);
        String finishedTripKey = Constants.QUEUE_BINDBUS_FINISHED_TRIP_PREFIX + busCode;
        RList<TripState> finishedList = redissonClient.getList(finishedTripKey);
        for (TripState tripState : runningList) {
            if (isTripFinished(tripState)) {
                // 行程已结束
                runningList.remove(tripState);
                log.info("checkRunningTripTimeStatus 行程已结束 key = \"{}\" busCode = \"{}\"", finishedTripKey, busCode);
                tripStateChange(tripState, TripStateRunningEnum.FINISH);
                finishedList.add(tripState);
                // 更新tripLog
                tripService.stopTripOnTripLog(busCode, tripState);
            }
        }

    }


    /**
     * @Description: 每天凌晨0点1分开始构建
     * @Param: []
     * @Return: void
     * @Author wen
     * @Date 2019/11/8 18:10
     */
    @Async
    @Scheduled(cron = "0 1 0 * * ?")
    public void initTripStopTimeDetail() {
        if(!SpringContextUtil.isPro()) {
            return;
        }
        List<BindBusStoptimeDetailInfo> bindBusStoptimeDetailInfos = busService.listBusStopTimeDetailInfos();
        List<String> savedBus = new ArrayList<>();
        log.debug("bindBusStoptimeDetailInfos ---> {}", JSON.toJSONString(bindBusStoptimeDetailInfos));
        bindBusStoptimeDetailInfos.forEach(busStopTimeDetailInfo -> {
            LocalTime now = LocalTime.now();
            LocalTime end = LocalTime.of(23, 59);
            Duration d = Duration.between(now, end);

            String key = Constants.QUEUE_STOP_TIME_PREFIX + busStopTimeDetailInfo.getBusCode();
            RList<BindBusStoptimeDetailInfo> allStopTimelist = redissonClient.getList(key);

            if(!savedBus.contains(busStopTimeDetailInfo.getBusCode())) {
                allStopTimelist.clear();
                savedBus.add(busStopTimeDetailInfo.getBusCode());
            }
            log.debug("bindBusStoptimeDetailInfos key ---> {}", key);
            allStopTimelist.add(busStopTimeDetailInfo);
            allStopTimelist.expire(d.toMinutes(), TimeUnit.MINUTES);
        });

        savedBus.forEach( b -> {

            String key = Constants.QUEUE_STOP_TIME_PREFIX + b;
            RList<BindBusStoptimeDetailInfo> allStopTimelist = redissonClient.getList(key);

            log.info("bindBusStoptimeDetailInfos  allStopTimelist --> {}", JSON.toJSONString(allStopTimelist));
        });

    }


    private void initStopTimeQueue(String busCode, Long stopTimeId, List<StudentGuardianInfo> students) {
        RList<StudentGuardianInfo> all = tripService.getAllStudentList(busCode, stopTimeId);
        all.clear();
        if(!students.isEmpty()) {
            all.addAll(students);
        }
    }

    private void initAttendanceQueue(TripState trip, String busCode) {
        RList<Long> up = tripService.getUpStudentList(trip, busCode);
        RList<Long> down = tripService.getDownStudentList(trip, busCode);
        up.clear();
        down.clear();
    }

    private void initTripQueue(String busCode, List<TripState> stateList) {
        String waiting = Constants.QUEUE_BINDBUS_WAITING_TRIP_PREFIX + busCode;
        String running = Constants.QUEUE_BINDBUS_RUNNING_TRIP_PREFIX + busCode;
        String finish = Constants.QUEUE_BINDBUS_FINISHED_TRIP_PREFIX + busCode;
        RList<TripState> waitingQueue = redissonClient.getList(waiting);
        RList<TripState> runningQueue = redissonClient.getList(running);
        RList<TripState> finishedQueue = redissonClient.getList(finish);

        waitingQueue.clear();
        runningQueue.clear();
        finishedQueue.clear();

        for (TripState tripState : stateList) {
            tripState.setLogRunningState(TripLogRunningEnum.WAITE);
            if (isTripFinished(tripState)) {
                // 行程已结束
                tripStateChange(tripState, TripStateRunningEnum.FINISH);
                finishedQueue.add(tripState);
            } else if (isTripRunning(tripState)) {
                // 行程进行中的
                tripStateChange(tripState, TripStateRunningEnum.RUNNING);
                runningQueue.add(tripState);
            } else if (isTripWaiting(tripState)) {
                tripStateChange(tripState, TripStateRunningEnum.WAITE);
                waitingQueue.add(tripState);
            }
            RList<StopTimeStudentDetail> completeStopTimelist = redissonClient.getList(Constants.QUEUE_COMPLETE_STOP_TIME_PREFIX + tripState.getId() + "." + busCode);
            completeStopTimelist.clear();
        }
    }

    private boolean isTripRunning(TripState tripState) {
        LocalTime now = LocalTime.now();
        LocalTime startTime = tripState.getStartTime().plusMinutes(-30);
        return (now.isAfter(startTime) || now.equals(startTime)) &&
                (now.isBefore(tripState.getEndTime()) || now.equals(tripState.getEndTime()));
    }

    private boolean isTripWaiting(TripState tripState) {
        LocalTime startTime = tripState.getStartTime().plusMinutes(-30);
        return LocalTime.now().isBefore(startTime);
    }

    private boolean isTripFinished(TripState tripState) {
        LocalTime endTime = tripState.getEndTime();
        return LocalTime.now().isAfter(endTime);
    }
}
