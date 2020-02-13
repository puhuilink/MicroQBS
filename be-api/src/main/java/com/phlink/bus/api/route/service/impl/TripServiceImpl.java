package com.phlink.bus.api.route.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.Dvr;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.domain.DvrServer;
import com.phlink.bus.api.bus.manager.ZaDvrManager;
import com.phlink.bus.api.bus.response.DvrStatusResponse;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.bus.service.IDvrServerService;
import com.phlink.bus.api.bus.service.IDvrService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.lisenter.service.CacheDeviceInfoService;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.map.response.AmapDistanceResultEntity;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.notify.event.StudentAttendanceEvent;
import com.phlink.bus.api.notify.event.TripEndEvent;
import com.phlink.bus.api.notify.event.TripStartEvent;
import com.phlink.bus.api.route.dao.TripMapper;
import com.phlink.bus.api.route.domain.*;
import com.phlink.bus.api.route.domain.enums.StopAttendanceStateEnum;
import com.phlink.bus.api.route.domain.enums.TripLogRunningEnum;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import com.phlink.bus.api.route.domain.enums.TripStateRunningEnum;
import com.phlink.bus.api.route.domain.vo.StopStudentVo;
import com.phlink.bus.api.route.domain.vo.TripStateVO;
import com.phlink.bus.api.route.domain.vo.TripStopStudentVO;
import com.phlink.bus.api.route.service.IStopAttendanceService;
import com.phlink.bus.api.route.service.ITripLogService;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.domain.StudentGuardianInfo;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wen
 */
@Slf4j
@Service
public class TripServiceImpl extends ServiceImpl<TripMapper, Trip> implements ITripService {

    @Autowired
    private IStudentService studentService;
    @Autowired
    private IBusService busService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IMapAmapService mapAmapService;
    @Autowired
    private CacheDeviceInfoService cacheDeviceInfoService;
    @Autowired
    private IDvrService dvrService;
    @Autowired
    private IStopAttendanceService stopAttendanceService;
    @Autowired
    private ITripLogService tripLogService;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private IDvrServerService dvrServerService;
    @Lazy
    @Autowired
    private ZaDvrManager zaDvrManager;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Trip findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<Trip> listTrips(QueryRequest request, Trip trip) {
        QueryWrapper<Trip> queryWrapper = new QueryWrapper<>();
        Page<Trip> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createTrip(Trip trip) {
        this.save(trip);
    }

    @Override
    public List<Trip> listTripByStudentId(Long studentId) {
        return this.baseMapper.findTripListByStudentId(studentId);
    }

    @Override
    public List<TripStopStudentVO> listTripStopTimeStudentDetail(String tripTime, Long schoolId) {
//		Long busTeacherId = 1156518284485586945L;
//		return this.tripMapper.findTripStopStudent(tripTime,busTeacherId);
        String inType = null;
        String outType = null;
        if ("1".equals(tripTime) || "3".equals(tripTime)) {
            inType = "1";
            outType = "2";
        } else {
            inType = "2";
            outType = "1";
        }
        // 获取当前行程
        Long tripId = 1L;
        // 获取当前行程请假的学生
        List<Student> leaveStudents = studentService.listLeaveStudentByTrip(tripTime, tripId, LocalDate.now());
        //
        return this.baseMapper.findTripStopStudent(tripTime, BusApiUtil.getCurrentUser().getUserId(), inType, outType);
    }

    @Override
    public List<TripStateVO> listByBusTeacherForNowTrip(Long tripId, Long userId) {
//		Long busTeacherId = 1156518284485586945L;
//		return this.tripMapper.findTripState(busTeacherId);
        return this.baseMapper.findTripState(tripId, userId);
    }

    @Override
    public List<StopStudentVo> listStuByStopId(Long stopId, String tripTime) {
//		Long busTeacherId = 1156518284485586945L;
//		return this.tripMapper.findStuStateByStopId(stopId,tripTime,busTeacherId);
        String type = null;
        if ("1".equals(tripTime) || "3".equals(tripTime)) {
            type = "1";
        } else {
            type = "2";
        }
        return this.baseMapper.findStuStateByStopId(stopId, tripTime, BusApiUtil.getCurrentUser().getUserId(), type);
    }

    @Override
    public List<StopStudentVo> listSchoolStudentBy(String tripTime) {
        String type = null;
        if ("1".equals(tripTime) || "3".equals(tripTime)) {
            type = "2";
        } else {
            type = "1";
        }
        return this.baseMapper.findSchoolStuState(tripTime, BusApiUtil.getCurrentUser().getUserId(), type);
    }

    @Override
    public Trip getNowTripForStudent(Long studentId) {
        return baseMapper.getNowTripForStudent(studentId);
    }

    @Override
    public Trip getNowTripForRouteOperation(Long routeOperationId) {
        return baseMapper.getNowTripForRouteOperation(routeOperationId);
    }

    @Override
    public TripStopTimeDetailVO getBusStopTime(TripState tripState) throws BusApiException {
        String busCode = tripState.getBusDetailInfo().getBusCode();
        List<StopTimeStudentDetail> allStopTimes = tripState.getStopTimes();
        TripStopTimeDetailVO vo = new TripStopTimeDetailVO();
        vo.setTripEndTime(tripState.getEndTime());
        vo.setTripStartTime(tripState.getStartTime());
        vo.setTripId(tripState.getId());
        vo.setRouteId(tripState.getRouteId());
        vo.setRouteName(String.valueOf(tripState.getRouteName()));

        RList<StopTimeStudentDetail> completeStopTimelist = redissonClient.getList(Constants.QUEUE_COMPLETE_STOP_TIME_PREFIX + tripState.getId() + "." + busCode);
        // 最后到达的站点
        StopTimeStudentDetail currentStop = completeStopTimelist.get(completeStopTimelist.size() - 1);
        // 未开始行程，默认下一站是第一站
        StopTimeStudentDetail nextStop = allStopTimes.get(0);
//        RMap<String, StopTimeStudentDetail> stopMap = redissonClient.getMap(Constants.CACHE_STOP_TIME_PREFIX + busCode);
//        StopTimeStudentDetail nextStop = stopMap.get(Constants.CACHE_STOP_TIME_KEY_NEXT);
        if (currentStop != null) {
            vo.setStopId(currentStop.getStopId());
            vo.setStopName(currentStop.getStopName());
            vo.setArrivalTime(currentStop.getArrivalTime());
            if (vo.getRouteId() == null) {
                vo.setTripId(tripState.getId());
                vo.setRouteId(tripState.getRouteId());
            }

            for (int i = 0; i < allStopTimes.size(); i++) {
                StopTimeStudentDetail stopIndex = allStopTimes.get(i);
                if (currentStop.getStopId().equals(stopIndex.getStopId())) {
                    if (i == allStopTimes.size() - 1) {
                        nextStop = null;
                    } else {
                        nextStop = allStopTimes.get(i + 1);
                    }
                }
            }
        }
        if (nextStop != null) {
            vo.setNextStopId(nextStop.getStopId());
            vo.setNextStopName(nextStop.getStopName());
            // 车辆当前位置
            String dvrCode = tripState.getBusDetailInfo().getDvrCode();
            if (StringUtils.isNotBlank(dvrCode)) {
                DvrLocation location = cacheDeviceInfoService.getLastDvr(dvrCode);
                if (location == null) {
                    return vo;
                }
                List<String> origins = Collections.singletonList(location.getGlon().toPlainString() + "," + location.getGlat().toPlainString());
                String destination = nextStop.getStopLon() + "," + nextStop.getStopLat();
                AmapDistanceResultEntity entity = mapAmapService.getDistance(origins, destination, "1");
                if (entity.requestSuccess()) {
                    List<AmapDistanceResultEntity.AmapDistanceResult> results = entity.getResults();
                    if (!results.isEmpty()) {
                        AmapDistanceResultEntity.AmapDistanceResult result = results.get(0);
                        vo.setNextStopDuration(result.getDuration());
                        vo.setNextStopDistance(result.getDistance());
                    }
                }
            }
        }
        return vo;
    }

    @Override
    public DvrLocation location(String dvrCode) {
        Dvr dvr = dvrService.getByDvrCode(dvrCode);
        if (dvr != null) {
            if (dvr.getDvrServerId() != null) {
                DvrServer dvrServer = dvrServerService.getById(dvr.getDvrServerId());

                String token = zaDvrManager.getDvrServerToken(dvrServer.getHost(), dvrServer.getPort());
                if (token != null) {
                    String statusUrl = String.format("http://%s:%s%s?jsession=%s&devNos=%s", dvrServer.getHost(), dvrServer.getPort(), "/web/doAction/getDevStatus", token, dvrCode);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Type", "application/x-www-form-urlencoded");
                    HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(headers);

                    ResponseEntity<DvrStatusResponse[]> responseEntity1 = this.restTemplate.getForEntity(statusUrl, DvrStatusResponse[].class, formEntity);
                    HttpStatus statusCode = responseEntity1.getStatusCode();
                    DvrStatusResponse[] entityBody = responseEntity1.getBody();
                    log.info("post entityBody: {}", entityBody);
                    log.info("post statusCode: {}", statusCode);
                    if (entityBody == null || entityBody.length == 0) {
                        log.error("DVR 服务器返回空");
                        return null;
                    }
                    DvrStatusResponse d = entityBody[0];
                    if (d.getGlat() < 4 || d.getGlat() > 53) {
                        log.debug("坐标点不在国内 {},{}", d.getGlon(), d.getGlat());
                        return null;
                    }
                    if (d.getGlon() < 73 || d.getGlon() > 135) {
                        log.debug("坐标点不在国内 {},{}", d.getGlon(), d.getGlat());
                        return null;
                    }
                    // 最新的数据
                    RBucket<Long> lastTimeBucket = redissonClient.getBucket(BusApiConstant.DVR_LAST_TIME_PREFIX + d.getDevNo(), new LongCodec());
                    // 更新dvr状态
                    DvrLocation dvrLocation = zaDvrManager.buildDvrLocation(d);
                    // 设置最新的采集时间
                    lastTimeBucket.set(d.getGpstime());
                    // 保存到redis
                    cacheDeviceInfoService.saveDvrInfo(dvrLocation);
                    return dvrLocation;
                }

            }
        }
        return null;
    }


    public TripStopTimeDetailVO buildTripStopTimeDetailVO(Bus bus) {
        TripState tripState = getCurrentRunningTrip(bus);
        try {

            return getBusStopTime(tripState);
        } catch (BusApiException e) {
            return new TripStopTimeDetailVO();
        }
    }

    public TripStopTimeDetailVO buildTripStopTimeDetailVOIsNull(Bus bus) {
        TripState tripState = getCurrentRunningTrip(bus);
        try {
            if(tripState==null){
             return null;
            }
            TripStopTimeDetailVO busStopTime = getBusStopTime(tripState);
            return busStopTime;
        } catch (BusApiException e) {
            return new TripStopTimeDetailVO();
        }
    }

    @Override
    @Transactional
    public void deleteTripsByRouteIds(List<Long> routeIds) {
        LambdaUpdateWrapper<Trip> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Trip::getRouteId, routeIds);
        updateWrapper.set(Trip::getDeleted, true);
        this.update(updateWrapper);
    }

    @Override
    public List<TripStopTimeDetail> listTripStopTimeDetailByTime(LocalTime now) {
        return baseMapper.listTripStopTimeDetail(now, null, null);
    }

    @Override
    public List<TripStopTimeDetail> listTripStopTimeDetailByTripId(Long tripId) {
        return baseMapper.listTripStopTimeDetail(null, null, tripId);
    }

    @Override
    public boolean checkSchoolStop(TripState s, List<StopTimeStudentDetail> stopTimeDetails, StopTimeStudentDetail stopTimeStudentDetail) {
        int index = stopTimeDetails.indexOf(stopTimeStudentDetail);
        if (TripRedirectEnum.GO.equals(s.getDirectionId())) {
            // 是否是学校站点
            if (index == stopTimeDetails.size() - 1) {
                return true;
            }
        }
        if (TripRedirectEnum.BACK.equals(s.getDirectionId())) {
            // 是否是学校站点
            return index == 0;
        }
        return false;
    }

    @Override
    public TripState getTripStateByIdAndBusCode(Long tripId, String busCode) {
        List<TripState> tripStates = listTripStatesAll(busCode);
        for (TripState tr : tripStates) {
            if (tr.getId().equals(tripId)) {
                return tr;
            }
        }
        return null;
    }

    @Override
    public List<StopTimeStudentDetail> listStopTimeStudentDetailByTripId(Long tripId, Integer directionId) {
        return baseMapper.listStopTimeStudentDetailByTripId(tripId, directionId);
    }

    @Override
    @Transactional
    public TripLog start(Bus bus, Long tripId) throws BusApiException {
        String running = Constants.QUEUE_BINDBUS_RUNNING_TRIP_PREFIX + bus.getBusCode();
        RList<TripState> runningList = redissonClient.getList(running);
        TripState trip = null;
        int index = 0;
        for (TripState t : runningList) {
            if (t.getId().equals(tripId)) {
                trip = t;
                break;
            }
            index++;
        }
        if (trip == null) {
            throw new BusApiException("该行程不在运行时间段内");
        }

        trip.setLogRunningState(TripLogRunningEnum.RUNNING);
        runningList.fastSet(index, trip);

        TripLog tripLog = startTripOnTripLog(bus.getBusCode(), trip);
        TripState tripStateFill = buildTripStudentInfo(trip);
        context.publishEvent(new TripStartEvent(this, tripStateFill, tripLog));
        return tripLog;
    }

    @Override
    public TripLog startTripOnTripLog(String busCode, TripState trip) throws BusApiException {
        int allnum;
        int leavenum;

        List<Long> allStudentIds = listAllStudentId(trip);
        if (allStudentIds == null) {
            return null;
        }
        allnum = allStudentIds.size();
        List<Long> leaveStudentIds = listLeaveStudentId(trip, allStudentIds);
        if (leaveStudentIds == null) {
            return null;
        }
        leavenum = leaveStudentIds.size();
        log.info("[startTripOnTripLog] busCode:{}, tripId:{} 请假人数:{} 所有人数:{}", busCode, trip.getId(), leavenum, allnum);
        // 记录日志
        return tripLogService.startTripState(trip, trip.getBusDetailInfo().getBindBusTeacherId(), leavenum, allnum);
    }

    @Override
    public TripLog stopTripOnTripLog(String busCode, TripState tripState) throws BusApiException {
        int allnum;
        int leavenum;

        List<Long> allStudentIds = listAllStudentId(tripState);
        if (allStudentIds == null) {
            return null;
        }
        allnum = allStudentIds.size();
        List<Long> leaveStudentIds = listLeaveStudentId(tripState, allStudentIds);
        if (leaveStudentIds == null) {
            return null;
        }
        leavenum = leaveStudentIds.size();
        log.info("[stopTripOnTripLog] busCode:{}, tripId:{} 请假人数:{} 所有人数:{}", busCode, tripState.getId(), leavenum, allnum);
        return tripLogService.stopTripState(tripState, leavenum);
    }

    @Override
    @Transactional
    public TripLog stop(Bus bus, Long tripId) throws BusApiException {
        List<TripState> tripRunningStates = listTripStatesAll(bus.getBusCode());
        TripState tripRunning = null;
        for (TripState tripState : tripRunningStates) {
            if (tripId.equals(tripState.getId())) {
                tripRunning = tripState;
            }
        }
        if (tripRunning == null) {
            throw new BusApiException("当前车辆没有绑定路线");
        }
        // 检查行程中人员打卡是否完成
        TripLog tripLog = tripLogService.getByTripId(tripId, tripRunning.getBusDetailInfo().getBindBusTeacherId());
        if (!tripLog.getUpNum().equals(tripLog.getDownNum())) {
            throw new BusApiException("上车人数和下车人数不一致，请检查确认");
        }

        String finish = Constants.QUEUE_BINDBUS_FINISHED_TRIP_PREFIX + bus.getBusCode();
        RList<TripState> finishedList = redissonClient.getList(finish);
        finishedList.remove(tripRunning);
        String running = Constants.QUEUE_BINDBUS_RUNNING_TRIP_PREFIX + bus.getBusCode();
        RList<TripState> runningList = redissonClient.getList(running);
        runningList.remove(tripRunning);

        tripRunning.setRunningState(TripStateRunningEnum.FINISH);
        tripRunning.setLogRunningState(TripLogRunningEnum.FINISH);
        finishedList.add(tripRunning);
        // 记录日志
        TripLog endLog = stopTripOnTripLog(bus.getBusCode(), tripRunning);
        TripState tripStateFill = buildTripStudentInfo(tripRunning);
        context.publishEvent(new TripEndEvent(this, tripStateFill, endLog));
        return endLog;
    }

    @Override
    public RList<StudentGuardianInfo> listAllStudentsOnTrip(TripState tripRunning) {
        String busCode = tripRunning.getBusDetailInfo().getBusCode();
        List<StopTimeStudentDetail> stopTimes = tripRunning.getStopTimes();

        if (stopTimes == null || stopTimes.isEmpty()) {
            log.error("listAllStudentIdsOnTrip 站点列表为空， busCode = {}", busCode);
            return null;
        }

        StopTimeStudentDetail schoolStop;
        if (TripRedirectEnum.GO.equals(tripRunning.getDirectionId())) {
            schoolStop = stopTimes.get(stopTimes.size() - 1);
        } else if (TripRedirectEnum.BACK.equals(tripRunning.getDirectionId())) {
            schoolStop = stopTimes.get(0);
        } else {
            log.error("listAllStudentIdsOnTrip 行程方向错误， 不是 GO 和 BLACK，tripState.getDirectionId() = {}", tripRunning.getDirectionId());
            return null;
        }
        RList<StudentGuardianInfo> allStudents = getAllStudentList(busCode, schoolStop.getStopTimeId());
        return allStudents;
    }

    @Override
    public TripState getTripState(Bus bus, Long tripId) throws BusApiException {
        List<TripState> allTrips = listTripToday(bus);
        TripState trip = null;
        for (TripState tripState : allTrips) {
            if (tripId.equals(tripState.getId())) {
                trip = tripState;
                break;
            }
        }
        return buildTripStudentInfo(trip);
    }

    @Override
    public TripState getCurrentRunningTrip(Bus bus) {
        return getCurrentRunningTrip(bus.getBusCode());
    }

    @Override
    public TripState getCurrentRunningTrip(String busCode) {
        List<TripState> states = listTripStatesAll(busCode);
        TripState trip = null;
        for(TripState tripState : states) {
            if (TripLogRunningEnum.RUNNING.equals(tripState.getLogRunningState())) {
                // 最近一次启动的优先级最高
                trip = tripState;
            }
        }
        // 否则获取在时间范围内的行程
        if (trip == null) {
            LocalTime now = LocalTime.now();
            for (TripState tripState : states) {
                if (tripState.getStartTime().isBefore(now) && now.isBefore(tripState.getEndTime())) {
                    trip = tripState;
                    break;
                }
            }
        }
        // 否则获取在运行时间范围内的行程
        if (trip == null) {
            for (TripState tripState : states) {
                if (TripStateRunningEnum.RUNNING.equals(tripState.getRunningState())) {
                    // 最近一次启动的优先级最高
                    trip = tripState;
                }
            }
        }
        return buildTripStudentInfo(trip);
    }

    @Override
    public TripState getCurrentLogRunningTrip(String busCode) {
        List<TripState> states = listTripStatesAll(busCode);
        TripState trip = null;
        for(TripState ts : states) {
            if (TripLogRunningEnum.RUNNING.equals(ts.getLogRunningState())) {
                // 获取最后一个启动的
                trip = ts;
            }
        }
        return buildTripStudentInfo(trip);
    }

    @Override
    public TripState getLastestWaittingTrip(Bus bus) {
        String waitting = Constants.QUEUE_BINDBUS_WAITING_TRIP_PREFIX + bus.getBusCode();
        RBlockingQueue<TripState> waittingQueue = redissonClient.getBlockingDeque(waitting);
        TripState trip = waittingQueue.peek();
        if (trip == null) {
            return null;
        }
        return buildTripStudentInfo(trip);
    }

    @Override
    public TripState getLastestFinishedTrip(Bus bus) {
        String finished = Constants.QUEUE_BINDBUS_FINISHED_TRIP_PREFIX + bus.getBusCode();
        RBlockingQueue<TripState> finishedQueue = redissonClient.getBlockingQueue(finished);
        List<TripState> trips = finishedQueue.readAll();
        if (trips == null || trips.isEmpty()) {
            return null;
        }
        return buildTripStudentInfo(trips.get(trips.size() - 1));
    }

    private TripTime getTripTimeByTime(LocalTime time, Long busId) {
        return baseMapper.getTripTimeByTime(time, busId);
    }

    @Override
    public List<TripState> listTripToday(Bus bus) throws BusApiException {
        List<TripState> tripRunningStates = listTripStatesAll(bus.getBusCode());
        return tripRunningStates.stream().map(this::buildTripStudentInfo).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<TripState> listTripStatesAll(String busCode) {
        List<TripState> tripRunningStates = new ArrayList<>();
        String waiting = Constants.QUEUE_BINDBUS_WAITING_TRIP_PREFIX + busCode;
        String running = Constants.QUEUE_BINDBUS_RUNNING_TRIP_PREFIX + busCode;
        String finish = Constants.QUEUE_BINDBUS_FINISHED_TRIP_PREFIX + busCode;
        RList<TripState> waitingQueue = redissonClient.getList(waiting);
        RList<TripState> runningQueue = redissonClient.getList(running);
        RList<TripState> finishedQueue = redissonClient.getList(finish);
        List<TripState> waitingList = new ArrayList<>();
        if (waitingQueue.isExists() && !waitingQueue.isEmpty()) {
            waitingList = waitingQueue.readAll();
        }
        List<TripState> runningList = new ArrayList<>();
        if (runningQueue.isExists() && !runningQueue.isEmpty()) {
            runningList = runningQueue.readAll();
        }
        List<TripState> finishList = new ArrayList<>();
        if (finishedQueue.isExists() && !finishedQueue.isEmpty()) {
            finishList = finishedQueue.readAll();
        }

        tripRunningStates.addAll(finishList);
        tripRunningStates.addAll(runningList);
        tripRunningStates.addAll(waitingList);
        Comparator<TripState> tripTimeComparator = Comparator.comparing(o -> o.getTripTime().getValue());
        tripRunningStates.sort(tripTimeComparator);
        return tripRunningStates;
    }

    /**
     * @Description: 把学生放进去
     * @Param: [tripState]
     * @Return: void
     * @Author wen
     * @Date 2019/11/15 14:07
     */
    private TripState buildTripStudentInfo(TripState tripState) {
        if (tripState == null) {
            return null;
        }
        if (tripState.getBusDetailInfo() == null) {
            return null;
        }
        String busCode = tripState.getBusDetailInfo().getBusCode();
        List<StopTimeStudentDetail> stopTimes = tripState.getStopTimes();
        List<Long> allStudentIds = listAllStudentId(tripState);
        if (allStudentIds == null) {
            return null;
        }
        List<Long> leaveStudentIds = listLeaveStudentId(tripState, allStudentIds);
        if (leaveStudentIds == null) {
            return null;
        }
        for (StopTimeStudentDetail stopTimeStudentDetail : stopTimes) {
            RList<StudentGuardianInfo> studentRList = getAllStudentList(busCode, stopTimeStudentDetail.getStopTimeId());
            int allNum = studentRList.size();

            RList<Long> upList = getUpStudentList(tripState, busCode);
            RList<Long> downList = getDownStudentList(tripState, busCode);

            List<StudentGuardianInfo> students = new ArrayList<>();
            int upNum = 0;
            int downNum = 0;
            int leaveNum = 0;
            for (StudentGuardianInfo studentGuardianInfo : studentRList) {
                if (downList.contains(studentGuardianInfo.getStudentId())) {
                    studentGuardianInfo.setStopAttendanceState(StopAttendanceStateEnum.DOWN);
                    downNum++;
                } else {
                    if (upList.contains(studentGuardianInfo.getStudentId())) {
                        studentGuardianInfo.setStopAttendanceState(StopAttendanceStateEnum.UP);
                    }
                }

                if (upList.contains(studentGuardianInfo.getStudentId())) {
                    upNum++;
                }

                if (!leaveStudentIds.isEmpty() && leaveStudentIds.contains(studentGuardianInfo.getStudentId())) {
                    studentGuardianInfo.setStopAttendanceState(StopAttendanceStateEnum.LEAVE);
                    leaveNum++;
                }

                students.add(studentGuardianInfo);
            }

            stopTimeStudentDetail.setStudents(students);
            stopTimeStudentDetail.setUpNum(upNum);
            stopTimeStudentDetail.setDownNum(downNum);
            stopTimeStudentDetail.setAllNum(allNum);
            stopTimeStudentDetail.setLeaveNum(leaveNum);

        }
        return tripState;
    }

    @Override
    public List<Long> listLeaveStudentId(TripState tripState, List<Long> studentIds) {
        List<Student> leaveStudent = studentService.listLeaveStudentByTrip(tripState.getTripTime().getValue(), tripState.getId(), LocalDate.now(), (studentIds != null && !studentIds.isEmpty())?studentIds.toArray(new Long[0]): null);
        return leaveStudent.stream().map(Student::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> listAllStudentId(TripState tripState) {
        String busCode = tripState.getBusDetailInfo().getBusCode();
        List<StopTimeStudentDetail> stopTimes = tripState.getStopTimes();
        if (stopTimes == null || stopTimes.isEmpty()) {
            log.error("站点列表为空， busCode = {}", busCode);
            return null;
        }

        StopTimeStudentDetail schoolStop = null;
        if (TripRedirectEnum.GO.equals(tripState.getDirectionId())) {
            schoolStop = stopTimes.get(stopTimes.size() - 1);
        } else if (TripRedirectEnum.BACK.equals(tripState.getDirectionId())) {
            schoolStop = stopTimes.get(0);
        } else {
            log.error("行程方向错误， 不是 GO 和 BLACK，tripState.getDirectionId() = {}", tripState.getDirectionId());
            return null;
        }
        RList<StudentGuardianInfo> allStudents = getAllStudentList(busCode, schoolStop.getStopTimeId());
        List<Long> studentIds = allStudents.stream().map(StudentGuardianInfo::getStudentId).collect(Collectors.toList());
        return studentIds;
    }

    @Override
    public RList<StudentGuardianInfo> getAllStudentList(String busCode, Long stopTimeId) {
        String all = Constants.QUEUE_BINDBUS_STOPTIME_STUDENT_PREFIX + busCode + "." + stopTimeId;
        return redissonClient.getList(all);
    }

    /**
     * @Description: 上车打卡的学生
     * @Param: [tripState, busCode]
     * @Return: org.redisson.api.RList<java.lang.Long>
     * @Author wen
     * @Date 2019/11/15 14:44
     */
    @Override
    public RList<Long> getUpStudentList(TripState tripState, String busCode) {
        RList<Long> upList = null;
        if (TripRedirectEnum.GO.equals(tripState.getDirectionId())) {
            String up = Constants.QUEUE_BINDBUS_HOME_UP_STOPTIME_STUDENT_PREFIX + busCode + "." + tripState.getId();
            upList = redissonClient.getList(up);
        }
        if (TripRedirectEnum.BACK.equals(tripState.getDirectionId())) {
            String up = Constants.QUEUE_BINDBUS_SCHOOL_UP_STOPTIME_STUDENT_PREFIX + busCode + "." + tripState.getId();
            upList = redissonClient.getList(up);
        }
        return upList;
    }


    /**
     * @Description: 下车打卡的学生
     * @Param: [tripState, busCode]
     * @Return: org.redisson.api.RList<java.lang.Long>
     * @Author wen
     * @Date 2019/11/15 14:44
     */
    @Override
    public RList<Long> getDownStudentList(TripState tripState, String busCode) {
        RList<Long> upList = null;
        if (TripRedirectEnum.GO.equals(tripState.getDirectionId())) {
            String up = Constants.QUEUE_BINDBUS_SCHOOL_DOWN_STOPTIME_STUDENT_PREFIX + busCode + "." + tripState.getId();
            upList = redissonClient.getList(up);
        }
        if (TripRedirectEnum.BACK.equals(tripState.getDirectionId())) {
            String up = Constants.QUEUE_BINDBUS_HOME_DOWN_STOPTIME_STUDENT_PREFIX + busCode + "." + tripState.getId();
            upList = redissonClient.getList(up);
        }
        return upList;
    }


    @Override
    @Transactional
    public void attendanceUp(TripState tripState, Long studentId, Long teacherId, Long reason) throws BusApiException {
        attendance(tripState, studentId, teacherId, StopAttendanceStateEnum.UP, reason);

    }

    @Override
    @Transactional
    public void attendanceDown(TripState tripState, Long studentId, Long teacherId) throws BusApiException {
        // 检查是否上车打卡
        RList<Long> studentIds = getUpStudentList(tripState, tripState.getBusDetailInfo().getBusCode());
        if (studentIds.isEmpty() || !studentIds.contains(studentId)) {
            throw new BusApiException("没有上车打卡，无法下车打卡");
        }
        attendance(tripState, studentId, teacherId, StopAttendanceStateEnum.DOWN, null);
    }

    @Override
    public void faceAttendance(TripState tripState, Long studentId, Long teacherId) throws BusApiException {
        // 检查是否上车打卡
        RList<Long> studentIds = getUpStudentList(tripState, tripState.getBusDetailInfo().getBusCode());
        if (studentIds.isEmpty() || !studentIds.contains(studentId)) {
            log.info("faceAttendance up studentId {} tripState {}", studentId, JSON.toJSONString(tripState));
            attendance(tripState, studentId, teacherId, StopAttendanceStateEnum.UP, null);
        } else {
            log.info("faceAttendance down studentId {} tripState {}", studentId, JSON.toJSONString(tripState));
            attendance(tripState, studentId, teacherId, StopAttendanceStateEnum.DOWN, null);
        }
    }

    @Override
    public List<Trip> listTripByRouteId(Long routeId) {
        QueryWrapper<Trip> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Trip::getRouteId, routeId);
        return list(wrapper);
    }

    @Override
    public List<TripTime> listTripTimeByRoute(Long routeId) {
        return baseMapper.listTripTimeByRoute(routeId);
    }

    private void attendance(TripState tripState, Long studentId, Long teacherId, StopAttendanceStateEnum stateEnum, Long reason) throws
            BusApiException {
        StudentGuardianInfo student = null;
        StopTimeStudentDetail stopTime = null;
        String busCode = tripState.getBusDetailInfo().getBusCode();

        TripLog nowLog = tripLogService.getByTripId(tripState.getId(), teacherId);
        if (nowLog == null) {
            log.error("[学生打卡错误] 该行程日志未找到，老师未开启行程, studentId={}  busCode={} teacherId={}", studentId, busCode, teacherId);
            throw new BusApiException("该行程未找到，老师未开启行程");
        }

        List<StopTimeStudentDetail> stopTimes = tripState.getStopTimes();
        for (StopTimeStudentDetail stopTimeStudentDetail : stopTimes) {
            RList<StudentGuardianInfo> studentRList = getAllStudentList(busCode, stopTimeStudentDetail.getStopTimeId());
            for (StudentGuardianInfo studentGuardianInfo : studentRList) {
                if (studentGuardianInfo.getStudentId().equals(studentId)) {
                    student = studentGuardianInfo;
                    stopTime = stopTimeStudentDetail;
                    break;
                }
            }
        }
        if (student == null) {
            log.error("[学生打卡错误] 该学生未找到, studentId={}  busCode={}", studentId, busCode);
            throw new BusApiException("该学生未找到");
        }
        student.setStopAttendanceState(stateEnum);

        if (StopAttendanceStateEnum.UP.equals(stateEnum)) {
            RList<Long> up = getUpStudentList(tripState, busCode);
            if (!up.contains(studentId)) {
                up.add(studentId);
                tripLogService.incrTripLogUpNumber(nowLog.getId());
            } else {
                log.error("[学生打卡错误] 已上车打卡, studentId={}  busCode={}", studentId, busCode);
                throw new BusApiException("该学生已上车打卡");
            }
        } else if (StopAttendanceStateEnum.DOWN.equals(stateEnum)) {
            RList<Long> down = getDownStudentList(tripState, busCode);
            if (!down.contains(studentId)) {
                down.add(studentId);
                tripLogService.incrTripLogDownNumber(nowLog.getId());
            } else {
                log.error("[学生打卡错误] 已下车打卡, studentId={}  busCode={}", studentId, busCode);
                throw new BusApiException("该学生已下车打卡");
            }
        } else {
            log.error("[参数不正确] 只能是上车和下车状态");
            throw new BusApiException("参数不正确, 只能是上车和下车状态");
        }

        // 记录
        StopAttendance attendance = new StopAttendance();
        attendance.setStudentId(studentId);
        attendance.setTime(LocalDate.now());
        attendance.setType(stateEnum.getValue());
        if (reason != null) {
            attendance.setReason(reason);
        }

        attendance.setStopId(stopTime.getStopId());
        attendance.setStopName(stopTime.getStopName());
        attendance.setRouteId(tripState.getRouteId());
        attendance.setRouteName(tripState.getBusDetailInfo().getRouteName());
        attendance.setTripId(tripState.getId());

        attendance.setBusTeacherId(tripState.getBusDetailInfo().getBindBusTeacherId());
        attendance.setBusTeacherName(tripState.getBusDetailInfo().getBusTeacherName());
        attendance.setDriverId(tripState.getBusDetailInfo().getBindDriverId());
        attendance.setDriverName(tripState.getBusDetailInfo().getDriverName());
        attendance.setBusId(tripState.getBusDetailInfo().getId());
        attendance.setBusCode(tripState.getBusDetailInfo().getBusCode());
        attendance.setNumberPlate(tripState.getBusDetailInfo().getNumberPlate());

        stopAttendanceService.saveOrUpdate(attendance);
        TripState tripStateFill = buildTripStudentInfo(tripState);
        context.publishEvent(new StudentAttendanceEvent(this, tripStateFill, attendance));
    }
}
