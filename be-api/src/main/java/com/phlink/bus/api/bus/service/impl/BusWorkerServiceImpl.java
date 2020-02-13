package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.BusMapper;
import com.phlink.bus.api.bus.dao.BusWorkerMapper;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.BusWorker;
import com.phlink.bus.api.bus.service.IBusWorkerService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;

import org.springframework.beans.factory.annotation.Autowired;
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
public class BusWorkerServiceImpl extends ServiceImpl<BusWorkerMapper, BusWorker> implements IBusWorkerService {
	
	@Autowired
	private BusMapper busMapper;

    @Override
    public IPage<BusWorker> listBusWorkers(QueryRequest request, BusWorker busWorker){
        QueryWrapper<BusWorker> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        if (busWorker.getCreateTimeFrom()!=null){
            queryWrapper.lambda().ge(BusWorker::getCreateTime, busWorker.getCreateTimeFrom());
        }
        if (busWorker.getCreateTimeTo()!=null ){
            queryWrapper.lambda().le(BusWorker::getCreateTime, busWorker.getCreateTimeTo());
        }
        Page<BusWorker> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createBusWorker(BusWorker busWorker) {
        busWorker.setCreateTime(LocalDateTime.now());
        this.save(busWorker);
    }

    @Override
    @Transactional
    public void modifyBusWorker(BusWorker busWorker) {
        this.updateById(busWorker);
    }

    @Override
    public void deleteBusWorkerIds(String[] busWorkerIds) {
        List<Long> list = Stream.of(busWorkerIds)
         .map(Long::parseLong)
         .collect(Collectors.toList());
         removeByIds(list);
    }

	@Override
	public Bus findMyBus() {
		Bus bus = null;
		Long userId = BusApiUtil.getCurrentUser().getUserId();
		bus = busMapper.getBusByWorkerId(userId);
		if(bus != null) {
			return bus;
		}else {
			bus = busMapper.findBusByUserId(userId);
			return bus;
		}		
	}
}
