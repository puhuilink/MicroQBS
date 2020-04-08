package com.phlink.core.web.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.base.vo.SearchVO;
import com.phlink.core.web.entity.LogTrace;
import com.phlink.core.web.mapper.LogTraceMapper;
import com.phlink.core.web.service.LogTraceService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wen
 */
@Slf4j
@Service("logService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogTraceServiceImpl extends ServiceImpl<LogTraceMapper, LogTrace> implements LogTraceService {

    @Override
    public List<LogTrace> listByCondition(Integer type, String key, SearchVO searchVo) {
        LambdaQueryWrapper<LogTrace> wrapper = new LambdaQueryWrapper<>();
        if (type != null) {
            wrapper.eq(LogTrace::getLogType, type);
        }
        if (StrUtil.isNotBlank(key)) {
            wrapper.or().like(LogTrace::getRequestUrl, key).like(LogTrace::getRequestParam, key)
                    .like(LogTrace::getUsername, key).like(LogTrace::getIpInfo, key).like(LogTrace::getName, key);
        }
        if (StrUtil.isNotBlank(searchVo.getStartDate()) && StrUtil.isNotBlank(searchVo.getEndDate())) {
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
