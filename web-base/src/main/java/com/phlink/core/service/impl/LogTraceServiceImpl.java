package com.phlink.core.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.common.vo.PageVO;
import com.phlink.core.common.vo.SearchVO;
import com.phlink.core.entity.LogTrace;
import com.phlink.core.mapper.LogTraceMapper;
import com.phlink.core.service.LogTraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service("logService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogTraceServiceImpl extends ServiceImpl<LogTraceMapper, LogTrace> implements LogTraceService {

    @Override
    public List<LogTrace> listByCondition(Integer type, String key, SearchVO searchVo) {
        LambdaQueryWrapper<LogTrace> wrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotBlank(key)) {
            wrapper.or().like(LogTrace::getRequestUrl, key)
                    .like(LogTrace::getLogType, key)
                    .like(LogTrace::getRequestParam, key)
                    .like(LogTrace::getUsername, key)
                    .like(LogTrace::getIpInfo, key)
                    .like(LogTrace::getName, key);
        }
        if(StrUtil.isNotBlank(searchVo.getStartDate())&&StrUtil.isNotBlank(searchVo.getEndDate())) {
            Date start = DateUtil.parse(searchVo.getStartDate());
            Date end = DateUtil.parse(searchVo.getEndDate());
            wrapper.between(LogTrace::getCreateTime, start, end);
        }
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void removeAll() {
        UpdateWrapper<LogTrace> wrapper = new UpdateWrapper<>();
        baseMapper.delete(wrapper);
    }
}


