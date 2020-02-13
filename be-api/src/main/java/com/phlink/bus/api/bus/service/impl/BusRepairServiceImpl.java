package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.BusRepairMapper;
import com.phlink.bus.api.bus.domain.BusRepair;
import com.phlink.bus.api.bus.service.IBusRepairService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
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
public class BusRepairServiceImpl extends ServiceImpl<BusRepairMapper, BusRepair> implements IBusRepairService {

    @Override
    public IPage<BusRepair> listBusRepairs(QueryRequest request, BusRepair busRepair){
        QueryWrapper<BusRepair> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        if (busRepair.getCreateTimeFrom()!=null){
            queryWrapper.lambda().ge(BusRepair::getCreateTime, busRepair.getCreateTimeFrom());
        }
        if (busRepair.getCreateTimeTo()!=null ){
            queryWrapper.lambda().le(BusRepair::getCreateTime, busRepair.getCreateTimeTo());
        }
        Page<BusRepair> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createBusRepair(BusRepair busRepair) {
        busRepair.setCreateTime(LocalDateTime.now());
        this.save(busRepair);
    }

    @Override
    @Transactional
    public void modifyBusRepair(BusRepair busRepair) {
        busRepair.setModifyTime(LocalDateTime.now());
        busRepair.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(busRepair);
    }

    @Override
    public void deleteBusRepairIds(String[] busRepairIds) {
        List<Long> list = Stream.of(busRepairIds)
         .map(Long::parseLong)
         .collect(Collectors.toList());
         removeByIds(list);
    }
}
