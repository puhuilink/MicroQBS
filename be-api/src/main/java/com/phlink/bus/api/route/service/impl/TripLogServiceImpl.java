package com.phlink.bus.api.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.domain.BindBusDetailInfo;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.bus.service.IDvrService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.route.dao.TripLogMapper;
import com.phlink.bus.api.route.domain.TripLog;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.route.domain.enums.TripLogRunningEnum;
import com.phlink.bus.api.route.service.IRouteOperationService;
import com.phlink.bus.api.route.service.ITripLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouyi
 */
@Slf4j
@Service
public class TripLogServiceImpl extends ServiceImpl<TripLogMapper, TripLog> implements ITripLogService {

    @Autowired
    private IBusService busService;
    @Autowired
    private IRouteOperationService routeOperationService;
    @Autowired
    private IDvrService dvrService;
    @Autowired
    private ApplicationContext context;

    @Override
    public TripLog findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<TripLog> listTripLogs(QueryRequest request, TripLog tripLog) {
        QueryWrapper<TripLog> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        Page<TripLog> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public TripLog startTripState(TripState tripState, Long busTeacherId, int leaveNum, int studentNum) throws BusApiException {

        // 获得今天该行程的tripLog
        TripLog lastLog = baseMapper.getByTripId(tripState.getId(), busTeacherId);
        if (lastLog == null) {
            lastLog = new TripLog();
        }
        BindBusDetailInfo detailInfo = tripState.getBusDetailInfo();
        lastLog.setDriverId(detailInfo.getBindDriverId());
        lastLog.setBusTeacherId(detailInfo.getBindBusTeacherId());
        lastLog.setBusNumberPlate(detailInfo.getNumberPlate());
        lastLog.setBusModel(detailInfo.getModel());
        lastLog.setBusId(detailInfo.getId());
        lastLog.setBusCode(detailInfo.getBusCode());
        lastLog.setRouteId(detailInfo.getRouteId());
        lastLog.setDvrno(detailInfo.getDvrCode());

        lastLog.setTripId(tripState.getId());
        lastLog.setTripBeginTime(LocalTime.now());
        if(TripLogRunningEnum.RUNNING.equals(tripState.getLogRunningState())) {
            lastLog.setState("1");
        }else{
            lastLog.setState("-1");
        }
        lastLog.setTripTime(tripState.getTripTime().getValue());
        lastLog.setTime(LocalDate.now());

        lastLog.setLeaveNum(leaveNum);
        lastLog.setStudentNum(studentNum);
        lastLog.setUpNum(0);
        lastLog.setDownNum(0);

        this.saveOrUpdate(lastLog);
        return lastLog;
    }

    @Override
    @Transactional
    public TripLog stopTripState(TripState tripState, int leavenum) {
        // 获得今天该行程的tripLog
        TripLog lastLog = baseMapper.getByTripId(tripState.getId(), tripState.getBusDetailInfo().getBindBusTeacherId());
        if (lastLog != null) {
            lastLog.setTripEndTime(LocalTime.now());
            if(TripLogRunningEnum.FINISH.equals(tripState.getLogRunningState())) {
                lastLog.setState("2");
            }else{
                lastLog.setState("-2");
            }
            lastLog.setLeaveNum(leavenum);
            this.saveOrUpdate(lastLog);
        }
        return lastLog;
    }

    @Override
    @Transactional
    public void deleteTripLogs(String[] tripLogIds) {
        List<Long> list = Stream.of(tripLogIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public TripLog detailNowTripLog(String tripTime) {
        return detailNowTripLog(tripTime, BusApiUtil.getCurrentUser().getUserId());
    }

    @Override
    public TripLog detailNowTripLog(String tripTime, Long teacherId) {
        return baseMapper.detailNowTripLog(tripTime, teacherId);
    }

    @Override
    public List<TripLog> listTripLogNotEnd(Long busTeacherId) {
        LambdaQueryWrapper<TripLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TripLog::getBusTeacherId, busTeacherId);
        queryWrapper.eq(TripLog::getState, "1");
        return list(queryWrapper);
    }

    @Override
    public TripLog getByBusCode(String busCode) {
        return this.baseMapper.getByBusCode(busCode);
    }

    @Override
    @Transactional
    public void incrTripLogUpNumber(Long tripLogId) {
        this.baseMapper.incrTripLogUpNumber(tripLogId);
    }

    @Override
    @Transactional
    public void incrTripLogDownNumber(Long tripLogId) {
        this.baseMapper.incrTripLogDownNumber(tripLogId);
    }

    @Override
    public TripLog getByTripId(Long tripId, Long teacherId) {
        return baseMapper.getByTripId(tripId, teacherId);
    }


}
