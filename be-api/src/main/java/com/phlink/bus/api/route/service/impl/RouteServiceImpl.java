package com.phlink.bus.api.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.route.dao.RouteMapper;
import com.phlink.bus.api.route.domain.*;
import com.phlink.bus.api.route.domain.enums.TripTimeEnum;
import com.phlink.bus.api.route.domain.vo.*;
import com.phlink.bus.api.route.service.*;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wen
 */
@Service
public class RouteServiceImpl extends ServiceImpl<RouteMapper, Route> implements IRouteService {

    @Lazy
    @Autowired
    private ITripService tripService;
    @Autowired
    private IRouteOperationService routeOperationService;
    @Autowired
    private IStopService stopService;
    @Autowired
    private IStopTimeService stopTimeService;
    @Autowired
    private IStudentService studentService;

    @Override
    public Route findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<Route> listRoutes(QueryRequest request, RouteViewVO routeViewVO) {
        Page<Route> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort = StringUtils.isNotBlank(request.getSortOrder()) ? request.getSortOrder() : "id";
        String order = StringUtils.isNotBlank(request.getSortField()) ? request.getSortField() : BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.baseMapper.listRoutes(page, routeViewVO);
    }

    @Override
    public List<Route> listRoutes(RouteViewVO routeViewVO) {
        Page<Route> page = new Page<>();
        page.setSearchCount(false);
        page.setSize(-1);
        this.baseMapper.listRoutes(page, routeViewVO);
        return page.getRecords();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createRoute(Route route) throws BusApiException {
        Route oldRoute = findByName(route.getRouteName());
        if (oldRoute != null) {
            throw new BusApiException("路线名称重复");
        }
        route.setCreateTime(LocalDateTime.now());
        route.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        route.setDeleted(false);
        this.save(route);
        // 创建trip
        List<TripTimeEnum> tripTimeEnums = route.getRouteType().getTripTime();
        for (TripTimeEnum tripTimeEnum : tripTimeEnums) {
            // 为每一个时间创建一个trip
            Trip trip = new Trip();
            trip.setRouteId(route.getId());
            trip.setSchoolId(route.getSchoolId());
            trip.setDirectionId(tripTimeEnum.getDirectionId());
            trip.setTripTime(tripTimeEnum);
            trip.setDeleted(false);
            tripService.createTrip(trip);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void modifyRoute(Route route) {
        route.setModifyTime(LocalDateTime.now());
        route.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(route);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteRoutes(String[] routeIds) {
        if (routeIds == null || routeIds.length == 0) {
            return;
        }
        List<Long> list = Stream.of(routeIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
        // 删除路线下的路线关联
        List<RouteOperation> allRouteOperation = routeOperationService.listByRouteIds(list);
        if (allRouteOperation != null && !allRouteOperation.isEmpty()) {
            List<String> routeOperationIds = allRouteOperation.stream()
                    .map(ro -> String.valueOf(ro.getId())).collect(Collectors.toList());
            routeOperationService.deleteRouteOperations(routeOperationIds.toArray(new String[0]));
        }
        // 删除站点
        stopService.deleteStopsByRouteIds(list);
        // 删除行程
        tripService.deleteTripsByRouteIds(list);
    }

    @Override
    public Route findByName(String routeName) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Route>().eq(Route::getRouteName, routeName));
    }

    @Override
    public List<RouteBusVO> listRouteBus() {
        return baseMapper.listRouteBus();
    }

    @Override
    public List<Route> listUnbindFence(String query) {
        return baseMapper.listUnbindFence(query);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Route saveRoute(SaveRouteVO routeVO, Long createUserId) throws BusApiException {
        Route oldRoute = findByName(routeVO.getRouteName());
        if (oldRoute != null) {
            throw new BusApiException("路线名称重复");
        }
        // 保存路线
        Route route = new Route();
        route.setCreateBy(createUserId);
        route.setCreateTime(LocalDateTime.now());
        route.setModifyBy(createUserId);
        route.setModifyTime(LocalDateTime.now());
        route.setOutboundTime(routeVO.getOutboundTime());
        route.setRouteDesc(routeVO.getRouteDesc());
        route.setRouteName(routeVO.getRouteName());
        route.setRouteType(routeVO.getRouteType());
        route.setTrajectoryId(routeVO.getTrajectoryId());
        route.setSchoolId(routeVO.getSchoolId());
        saveOrUpdate(route);
        // 创建行程
        List<TripTimeEnum> tripTimeEnums = route.getRouteType().getTripTime();
        Map<TripTimeEnum, Long> tripTimeIdMap = new HashMap<>();
        for (TripTimeEnum tripTimeEnum : tripTimeEnums) {
            // 为每一个时间创建一个trip
            Trip trip = new Trip();
            trip.setRouteId(route.getId());
            trip.setSchoolId(route.getSchoolId());
            trip.setDirectionId(tripTimeEnum.getDirectionId());
            trip.setTripTime(tripTimeEnum);
            trip.setDeleted(false);
            tripService.save(trip);
            tripTimeIdMap.put(tripTimeEnum, trip.getId());
        }
        // 创建站点
        List<SaveRouteStopVO> stops = routeVO.getStops();
        Map<Integer, Stop> processedSeqMaps = new HashMap<>();
        for (SaveRouteStopVO stopVO : stops) {
            Stop stop;
            if(!processedSeqMaps.containsKey(stopVO.getStopSequence())) {
                stop = new Stop();
                stop.setCreateBy(createUserId);
                stop.setCreateTime(LocalDateTime.now());
                stop.setRouteId(route.getId());
                stop.setCreateBy(createUserId);
                stop.setCreateTime(LocalDateTime.now());
                stop.setRouteId(route.getId());
                stop.setStopLat(stopVO.getLatitude());
                stop.setStopLon(stopVO.getLongitude());
                stop.setStopSequence(stopVO.getStopSequence());
                stop.setStopName(stopVO.getStopName());
                stopService.save(stop);
                processedSeqMaps.put(stopVO.getStopSequence(), stop);
            }else{
                stop = processedSeqMaps.get(stopVO.getStopSequence());
            }
            StopTime stopTime = new StopTime();
            stopTime.setCreateBy(createUserId);
            stopTime.setCreateTime(LocalDateTime.now());
            stopTime.setArrivalTime(stopVO.getArrivalTime());
            stopTime.setStopSequence(stopVO.getStopSequence());
            stopTime.setStopId(stop.getId());
            stopTime.setTripId(tripTimeIdMap.get(stopVO.getTripTime()));
            stopTimeService.save(stopTime);
        }

        return route;
    }

    @Override
    public Route updateRoute(UpdateRouteVO routeVO, Route route, Long userId) throws BusApiException {
        Route oldRoute = findByName(routeVO.getRouteName());
        if (oldRoute != null && !oldRoute.getId().equals(route.getId())) {
            throw new BusApiException("路线名称重复");
        }
        route.setModifyBy(userId);
        route.setModifyTime(LocalDateTime.now());
        route.setOutboundTime(routeVO.getOutboundTime());
        route.setRouteDesc(routeVO.getRouteDesc());
        route.setRouteName(routeVO.getRouteName());
        route.setTrajectoryId(routeVO.getTrajectoryId());
        route.setSchoolId(routeVO.getSchoolId());
        saveOrUpdate(route);

        // 删除站点和站点时刻
        if (routeVO.getDeleteStopIds() != null && !routeVO.getDeleteStopIds().isEmpty()) {
            stopService.removeByIds(routeVO.getDeleteStopIds());
            stopTimeService.removeByStopId(routeVO.getDeleteStopIds());
            // 解除学生的站点关联
            studentService.unbindStudentStopIds(routeVO.getDeleteStopIds());
        }

        // 创建或者更新站点
        List<SaveRouteStopVO> stops = routeVO.getStops();
        Map<Integer, Stop> processedSeqMaps = new HashMap<>();
        for (SaveRouteStopVO stopVO : stops) {
            Stop stop;
            if(!processedSeqMaps.containsKey(stopVO.getStopSequence())) {
                if (stopVO.getId() == null) {
                    stop = new Stop();
                    stop.setCreateBy(userId);
                    stop.setCreateTime(LocalDateTime.now());
                    stop.setRouteId(routeVO.getRouteId());
                } else {
                    stop = stopService.getById(stopVO.getId());
                    if (stop == null) {
                        stop = new Stop();
                        stop.setCreateBy(userId);
                        stop.setCreateTime(LocalDateTime.now());
                        stop.setRouteId(routeVO.getRouteId());
                    }else{
                        stop.setModifyBy(userId);
                        stop.setModifyTime(LocalDateTime.now());
                    }
                }
                stop.setStopLat(stopVO.getLatitude());
                stop.setStopLon(stopVO.getLongitude());
                stop.setStopSequence(stopVO.getStopSequence());
                stop.setStopName(stopVO.getStopName());
                stopService.saveOrUpdate(stop);
                processedSeqMaps.put(stopVO.getStopSequence(), stop);
            }else{
                stop = processedSeqMaps.get(stopVO.getStopSequence());
            }
            // 保存或者更新stopTime
            StopTime stopTime;
            if (stopVO.getStopTimeId() == null) {
                stopTime = new StopTime();
                stopTime.setCreateBy(userId);
                stopTime.setCreateTime(LocalDateTime.now());
            } else {
                stopTime = stopTimeService.getById(stopVO.getStopTimeId());
                if (stopTime == null) {
                    throw new BusApiException("站点不存在");
                }
                stopTime.setModifyBy(userId);
                stopTime.setModifyTime(LocalDateTime.now());
            }
            stopTime.setStopId(stop.getId());
            stopTime.setTripId(stopVO.getTripId());
            stopTime.setArrivalTime(stopVO.getArrivalTime());
            stopTime.setStopSequence(stopVO.getStopSequence());
            stopTimeService.saveOrUpdate(stopTime);
        }
        return route;
    }

    @Override
    public RouteDetailVO getRouteDetail(Route route, Long routeId) {
        RouteDetailVO routeVO = new RouteDetailVO();
        BeanUtils.copyProperties(route, routeVO);
        routeVO.setRouteId(routeId);
        List<Stop> stops = stopService.listStopByRoute(routeId);
        routeVO.setStops(stops);
        List<Long> stopIds = stops.stream().map(Stop::getId).collect(Collectors.toList());
        List<Trip> trips = tripService.listTripByRouteId(routeId);
        List<TripDetailVO> tripDetailVOS = trips.stream().map(t -> {
            TripDetailVO vo = new TripDetailVO();
            BeanUtils.copyProperties(t, vo);
            vo.setTripId(t.getId());
            List<StopTime> stopTimeList = stopTimeService.listByTrip(t.getId(), stopIds);
            vo.setStopTimes(stopTimeList);
          return vo;
        }).collect(Collectors.toList());
        routeVO.setTrips(tripDetailVOS);
        return routeVO;
    }

    @Override
    public List<Route> listBySchool(Long schoolId) {
        QueryWrapper<Route> wrapper = new QueryWrapper();
        wrapper.lambda().eq(Route::getSchoolId, schoolId);
        return list(wrapper);
    }
}