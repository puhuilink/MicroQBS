package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.DvrLocationMapper;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IDvrLocationService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author wen
*/
@Service
public class DvrLocationServiceImpl extends ServiceImpl<DvrLocationMapper, DvrLocation> implements IDvrLocationService {


    @Override
    public DvrLocation findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<DvrLocation> listDvrLocations(QueryRequest request, DvrLocation dvrLocation){
        QueryWrapper<DvrLocation> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        Page<DvrLocation> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort= StringUtils.isNotBlank(request.getSortOrder())?request.getSortOrder():"id";
        String order=StringUtils.isNotBlank(request.getSortField())?request.getSortField():BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createDvrLocation(DvrLocation dvrLocation) {
        dvrLocation.setCreateTime(LocalDateTime.now());
        this.save(dvrLocation);
    }

    @Override
    @Transactional
    public void modifyDvrLocation(DvrLocation dvrLocation) {
        this.updateById(dvrLocation);
    }

    @Override
    public void deleteDvrLocations(String[] dvrLocationIds) {
        List<Long> list = Stream.of(dvrLocationIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public List<DvrLocation> listByBusId(Long busId, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<DvrLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DvrLocation::getBusId, busId);
        queryWrapper.ge(DvrLocation::getGpstime, startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        queryWrapper.le(DvrLocation::getGpstime, endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        queryWrapper.orderByAsc(DvrLocation::getGpstime);
        return this.list(queryWrapper);
    }

    @Override
    public List<DvrLocation> listByBusId(Long busId, Long startTime, Long endTime) {
        LambdaQueryWrapper<DvrLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DvrLocation::getBusId, busId);
        queryWrapper.ge(DvrLocation::getGpstime, startTime);
        queryWrapper.le(DvrLocation::getGpstime, endTime);
        queryWrapper.eq(DvrLocation::getOnline, 1);
        queryWrapper.orderByAsc(DvrLocation::getGpstime);
        return this.list(queryWrapper);
    }
}
