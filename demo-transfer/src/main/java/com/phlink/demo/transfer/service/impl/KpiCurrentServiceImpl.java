/*
 * @Author: sevncz.wen
 * @Date: 2020-04-01 18:24:25
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-04-01 20:46:08
 */
package com.phlink.demo.transfer.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.web.base.vo.SearchVO;
import com.phlink.demo.transfer.entity.KpiCurrent;
import com.phlink.demo.transfer.mapper.KpiCurrentMapper;
import com.phlink.demo.transfer.service.KpiCurrentService;

import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * KpiCurrentServiceImpl
 */
@Service
public class KpiCurrentServiceImpl extends ServiceImpl<KpiCurrentMapper, KpiCurrent> implements KpiCurrentService {

    @Override
    public List<KpiCurrent> listByCondition(SearchVO searchVo) {

        LambdaQueryWrapper<KpiCurrent> wrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotBlank(searchVo.getStartDate())&&StrUtil.isNotBlank(searchVo.getEndDate())) {
            Date start = DateUtil.parse(searchVo.getStartDate());
            Date end = DateUtil.parse(searchVo.getEndDate());
            wrapper.between(KpiCurrent::getInsertTime, start, end);
        }
        return baseMapper.selectList(wrapper);
    }
}
