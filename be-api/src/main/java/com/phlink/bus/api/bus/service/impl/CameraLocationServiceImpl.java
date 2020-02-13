package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.CameraLocationMapper;
import com.phlink.bus.api.bus.domain.CameraLocation;
import com.phlink.bus.api.bus.service.ICameraLocationService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
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
public class CameraLocationServiceImpl extends ServiceImpl<CameraLocationMapper, CameraLocation> implements ICameraLocationService {


    @Override
    public CameraLocation findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<CameraLocation> listCameraLocations(QueryRequest request, CameraLocation cameraLocation){
        QueryWrapper<CameraLocation> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        Page<CameraLocation> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort= StringUtils.isNotBlank(request.getSortOrder())?request.getSortOrder():"id";
        String order=StringUtils.isNotBlank(request.getSortField())?request.getSortField():BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createCameraLocation(CameraLocation cameraLocation) {
        this.save(cameraLocation);
    }

    @Override
    @Transactional
    public void modifyCameraLocation(CameraLocation cameraLocation) {
        this.updateById(cameraLocation);
    }

    @Override
    public void deleteCameraLocations(String[] cameraLocationIds) {
        List<Long> list = Stream.of(cameraLocationIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }
}
