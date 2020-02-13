package com.phlink.bus.api.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.device.dao.EwatchLocationMapper;
import com.phlink.bus.api.device.domain.EwatchLocation;
import com.phlink.bus.api.device.service.IEwatchLocationService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.SortUtil;
import io.rpc.core.device.EWatchInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author wen
*/
@Service
public class EwatchLocationServiceImpl extends ServiceImpl<EwatchLocationMapper, EwatchLocation> implements IEwatchLocationService {


    @Override
    public EwatchLocation findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<EwatchLocation> listEwatchLocations(QueryRequest request, EwatchLocation ewatchLocation){
        QueryWrapper<EwatchLocation> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        Page<EwatchLocation> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort= StringUtils.isNotBlank(request.getSortOrder())?request.getSortOrder():"id";
        String order=StringUtils.isNotBlank(request.getSortField())?request.getSortField():BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public EwatchLocation createEwatchLocation(EWatchInfo eWatchInfo) {
        EwatchLocation ewatchLocation = new EwatchLocation(eWatchInfo);
        ewatchLocation.setCreateTime(LocalDateTime.now());
        this.save(ewatchLocation);
        return ewatchLocation;
    }

    @Override
    @Transactional
    public void modifyEwatchLocation(EwatchLocation ewatchLocation) {
        this.updateById(ewatchLocation);
    }

    @Override
    public void deleteEwatchLocations(String[] ewatchLocationIds) {
        List<Long> list = Stream.of(ewatchLocationIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }
}
