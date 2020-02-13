package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.DvrMapper;
import com.phlink.bus.api.bus.domain.Dvr;
import com.phlink.bus.api.bus.service.IDvrService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
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
public class DvrServiceImpl extends ServiceImpl<DvrMapper, Dvr> implements IDvrService {

    @Override
    public Dvr findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<Dvr> listDvrs(QueryRequest request, Dvr dvr){
        QueryWrapper<Dvr> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        Page<Dvr> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort= StringUtils.isNotBlank(request.getSortOrder())?request.getSortOrder():"id";
        String order=StringUtils.isNotBlank(request.getSortField())?request.getSortField():BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createDvr(Dvr dvr) throws BusApiException {
        Dvr checkDvr = getByDvrCode(dvr.getDvrCode());
        if(checkDvr != null) {
            throw new BusApiException("该DVR编号已存在");
        }
        if(dvr.getChannelNumber() == null) {
            dvr.setChannelNumber(Dvr.CHANNEL_NUMBER);
        }
        dvr.setCreateTime(LocalDateTime.now());
        dvr.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(dvr);
    }

    @Override
    @Transactional
    public void modifyDvr(Dvr dvr) {
        this.updateById(dvr);
    }

    @Override
    public void deleteDvrs(String[] dvrIds) {
        List<Long> list = Stream.of(dvrIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public Dvr getByDvrCode(String dvrCode) {
        LambdaQueryWrapper<Dvr> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dvr::getDvrCode, dvrCode);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Dvr getByBusId(Long busId) {
        LambdaQueryWrapper<Dvr> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dvr::getBusId, busId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Dvr getByStudentId(Long studentId) {
        return baseMapper.getByStudentId(studentId);
    }

    @Override
    public int countRunning() {
        LambdaQueryWrapper<Dvr> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dvr::getOnline, 1);
        return baseMapper.selectCount(lambdaQueryWrapper);
    }

    @Override
    public List<Dvr> listDrv() {
        return baseMapper.selectList(null);
    }
}
