package com.phlink.bus.api.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.route.dao.StopTimeMapper;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.route.domain.StopTime;
import com.phlink.bus.api.route.domain.vo.TripStopTimeListVO;
import com.phlink.bus.api.route.service.IStopService;
import com.phlink.bus.api.route.service.IStopTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wen
 */
@Service
public class StopTimeServiceImpl extends ServiceImpl<StopTimeMapper, StopTime> implements IStopTimeService {

    @Autowired
    private IStopService stopService;

    @Override
    public StopTime findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<StopTime> listStopTimes(QueryRequest request, StopTime stopTime) {
        QueryWrapper<StopTime> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        if (stopTime.getCreateTimeFrom() != null) {
            queryWrapper.lambda().ge(StopTime::getCreateTime, stopTime.getCreateTimeFrom());
        }
        if (stopTime.getCreateTimeTo() != null) {
            queryWrapper.lambda().le(StopTime::getCreateTime, stopTime.getCreateTimeTo());
        }
        Page<StopTime> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createStopTime(StopTime stopTime) throws BusApiException {
        buildNewStopTime(stopTime);
        this.save(stopTime);
    }

    @Override
    public void batchCreateStopTime(List<StopTime> stopTimeList) throws BusApiException {
        for (StopTime stopTime : stopTimeList) {
            buildNewStopTime(stopTime);
        }
        this.saveBatch(stopTimeList);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void modifyStopTime(StopTime stopTime) {
        buildUpdateStopTime(stopTime);
        this.updateById(stopTime);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void batchModifyStopTime(List<StopTime> stopTimeList) throws BusApiException {
        // 先删除
        List<Long> stopIds = stopTimeList.stream()
                .map(StopTime::getStopId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        if(!stopIds.isEmpty()) {
            UpdateWrapper<StopTime> wrapper = new UpdateWrapper<>();
            wrapper.lambda().in(StopTime::getStopId, stopIds);
            wrapper.lambda().set(StopTime::getDeleted, true);
            update(wrapper);
        }
        // 再添加
        batchCreateStopTime(stopTimeList);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteStopTimes(String[] stopTimeIds) {
        List<Long> list = Stream.of(stopTimeIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public List<TripStopTimeListVO> listTripStopTimeListVO(Long routeId) {
        return this.baseMapper.listTripStopTimeListVO(routeId, null);
    }

    @Override
    public List<StopTime> listByTrip(Long tripId, List<Long> stopIds) {
        QueryWrapper<StopTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StopTime::getTripId, tripId);
        queryWrapper.lambda().in(StopTime::getStopId, stopIds);
        queryWrapper.lambda().orderByAsc(StopTime::getStopSequence);
        return this.list(queryWrapper);
    }

    @Override
    public List<StopTime> listByTrip(Long tripId) {
        QueryWrapper<StopTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StopTime::getTripId, tripId);
        queryWrapper.lambda().orderByAsc(StopTime::getStopSequence);
        return this.list(queryWrapper);
    }

    @Override
    public List<TripStopTimeListVO> listTripStopTimeListVOByBusTeacher() {

        return this.baseMapper.listTripStopTimeListVO(null, BusApiUtil.getCurrentUser().getUserId());
    }

    private void buildNewStopTime(StopTime stopTime) throws BusApiException {
        Stop stop = stopService.getById(stopTime.getStopId());
        if (stop == null) {
            log.error("[stop_id: " + stopTime.getStopId() + "] Create stop time error, stop is not found.");
            throw new BusApiException("站点不存在!");
        }
        stopTime.setStopSequence(stop.getStopSequence());
        stopTime.setCreateTime(LocalDateTime.now());
        stopTime.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
    }

    private void buildUpdateStopTime(StopTime stopTime) {
        stopTime.setModifyTime(LocalDateTime.now());
        stopTime.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
    }

    @Override
    public StopTime getStopTimeByFenceId(String stopFenceId) {
        return this.baseMapper.getStopTimeByFenceId(stopFenceId);
    }

    @Override
    public StopTime getFirstStopOnTrip(Long tripId) {
        return baseMapper.getFirstStopOnTrip(tripId);
    }

    @Override
    public StopTime getNext(Long stopId, Long tripId) {
        return baseMapper.getNextStopTime(stopId, tripId);
    }

    @Override
    public void removeByStopId(List<Long> deleteStopIds) {
        UpdateWrapper<StopTime> wrapper = new UpdateWrapper<>();
        wrapper.lambda().in(StopTime::getStopId, deleteStopIds);
        wrapper.lambda().set(StopTime::getDeleted, true);
        this.update(wrapper);
    }
}
