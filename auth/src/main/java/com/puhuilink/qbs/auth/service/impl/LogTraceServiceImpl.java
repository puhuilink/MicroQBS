/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:33
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:21
 */
package com.puhuilink.qbs.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.core.base.enums.ResultCode;
import com.puhuilink.qbs.core.base.exception.WarnException;
import com.puhuilink.qbs.core.base.vo.SearchVO;
import com.puhuilink.qbs.core.base.utils.DateFormatUtil;
import com.puhuilink.qbs.auth.entity.LogTrace;
import com.puhuilink.qbs.auth.mapper.LogTraceMapper;
import com.puhuilink.qbs.auth.service.LogTraceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


@Service("logService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogTraceServiceImpl extends ServiceImpl<LogTraceMapper, LogTrace> implements LogTraceService {

    @Override
    public List<LogTrace> listByCondition(Integer type, String key, SearchVO searchVo) {
        LambdaQueryWrapper<LogTrace> wrapper = new LambdaQueryWrapper<>();
        if (type != null) {
            wrapper.eq(LogTrace::getLogType, type);
        }
        if (StringUtils.isNotBlank(key)) {
            wrapper.or().like(LogTrace::getRequestUrl, key).like(LogTrace::getRequestParam, key)
                    .like(LogTrace::getUsername, key).like(LogTrace::getIpInfo, key).like(LogTrace::getName, key);
        }
        if (StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())) {
            Date start = null;
            try {
                start = DateFormatUtil.pareDate(searchVo.getStartDate());
            } catch (ParseException e) {
                throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "开始时间格式错误");
            }
            Date end = null;
            try {
                end = DateFormatUtil.pareDate(searchVo.getEndDate());
            } catch (ParseException e) {
                throw new WarnException(ResultCode.BAD_REQUEST_PARAMS.getCode(), "结束时间格式错误");
            }
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
