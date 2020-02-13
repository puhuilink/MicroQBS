package com.phlink.bus.api.fence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.fence.dao.FenceTimeMapper;
import com.phlink.bus.api.fence.domain.FenceTime;
import com.phlink.bus.api.fence.service.IFenceTimeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author wen
*/
@Service
public class FenceTimeServiceImpl extends ServiceImpl<FenceTimeMapper, FenceTime> implements IFenceTimeService {

    @Override
    public void deleteFenceTimes(String[] fenceTimeIds) {
        List<Long> list = Stream.of(fenceTimeIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public void deleteByFenceId(Long fenceId) {
        baseMapper.deleteByFenceId(fenceId);
    }

    @Override
    public List<FenceTime> listByFenceId(Long fenceId) {
        QueryWrapper<FenceTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FenceTime::getFenceId, fenceId);
        queryWrapper.lambda().orderByAsc(FenceTime::getStartTime);
        return this.list(queryWrapper);
    }
}
