/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:33
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:21
 */
package com.puhuilink.qbs.core.web.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.core.base.vo.SearchVO;
import com.puhuilink.qbs.core.web.entity.LogTrace;
import com.puhuilink.qbs.core.web.mapper.LogTraceMapper;
import com.puhuilink.qbs.core.web.service.LogTraceService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

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
