package com.phlink.bus.api.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.route.dao.StopMapper;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.route.domain.vo.FenceStopVO;
import com.phlink.bus.api.route.service.IStopService;
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
public class StopServiceImpl extends ServiceImpl<StopMapper, Stop> implements IStopService {

    @Autowired
    private IFenceService fenceService;

    @Override
    public Stop findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<Stop> listStops(QueryRequest request, Stop stop) {
        QueryWrapper<Stop> queryWrapper = new QueryWrapper<>();
        if (stop.getCreateTimeFrom() != null) {
            queryWrapper.lambda().ge(Stop::getCreateTime, stop.getCreateTimeFrom());
        }
        if (stop.getCreateTimeTo() != null) {
            queryWrapper.lambda().le(Stop::getCreateTime, stop.getCreateTimeTo());
        }
        if (stop.getRouteId() != null) {
            queryWrapper.lambda().eq(Stop::getRouteId, stop.getRouteId());
        }
        Page<Stop> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createStop(Stop stop) throws BusApiException {
        // 一条路线上站点不能重复
        Stop oldStop = getStopByNameOnRoute(stop.getStopName(), stop.getRouteId());
        if (oldStop != null) {
            throw new BusApiException("站点名称重复");
        }
        stop.setCreateTime(LocalDateTime.now());
        stop.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(stop);
        //不再创建站点围栏
//        QueryWrapper<Fence> query = new QueryWrapper<>();
//        query.lambda().eq(Fence::getRelationId, stop.getRouteId());
//        query.lambda().eq(Fence::getRelationType, RelationTypeEnum.ROUTE);
//        Fence fence = this.fenceService.getOne(query);
//        if (fence != null) {
//            FenceStopVO fenceStopVO = new FenceStopVO();
//            fenceStopVO.setId(stop.getId());
//            fenceStopVO.setCenter(stop.getStopLon() + "," + stop.getStopLat());
//            fenceStopVO.setFenceName(stop.getStopName());
//            this.fenceService.createStopFence(fenceStopVO, stop.getRouteId());
//        }
    }

    public Stop getStopByNameOnRoute(String stopName, Long routeId) {
        QueryWrapper<Stop> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Stop::getRouteId, routeId);
        queryWrapper.lambda().eq(Stop::getStopName, stopName);
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void batchCreateStop(List<Stop> stops) {
        this.saveBatch(stops.stream().peek(s -> {
            s.setId(null);
            s.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
            s.setCreateTime(LocalDateTime.now());
        }).collect(Collectors.toList()));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void modifyStop(Stop stop) {
        buildUpdateStop(stop);
        this.updateById(stop);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void batchModifyStop(List<Stop> stopList) {
        // 先删除
        List<Long> stopIds = stopList.stream()
                .map(Stop::getId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        if(!stopIds.isEmpty()) {
            removeByIds(stopIds);
        }
        // 再添加
        batchCreateStop(stopList);
    }

    private void buildUpdateStop(Stop stop) {
        stop.setModifyTime(LocalDateTime.now());
        stop.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
    }

    @Override
    public void deleteStops(String[] stopIds) {
        List<Long> list = Stream.of(stopIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public List<Stop> listStopByRoute(Long routeId) {
        QueryWrapper<Stop> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Stop::getRouteId, routeId);
        queryWrapper.orderByAsc("stop_sequence");
        return this.list(queryWrapper);
    }

    @Override
    public Stop findByName(String stopName, Long routeId) {
        LambdaQueryWrapper<Stop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Stop::getStopName, stopName);
        if(routeId != null) {
            queryWrapper.eq(Stop::getRouteId, routeId);
        }
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<FenceStopVO> listUnbindFenceStopInfo(Long routeId) {
        return baseMapper.listUnbindFenceStopInfo(routeId);
    }

    @Override
    public List<Stop> listStopByBindBus(Long busId) {
        return this.baseMapper.listStopByBindBus(busId);
    }

    @Override
    public void deleteStopsByRouteIds(List<Long> routeIds) {
        LambdaUpdateWrapper<Stop> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Stop::getRouteId, routeIds);
        updateWrapper.set(Stop::getDeleted, true);
        this.update(updateWrapper);
    }

    @Override
    public List<Stop> listStopByRouteIds(List<Long> routeIds) {
        return this.baseMapper.listByRouteIds(routeIds.toArray(new Long[0]));
    }
}
