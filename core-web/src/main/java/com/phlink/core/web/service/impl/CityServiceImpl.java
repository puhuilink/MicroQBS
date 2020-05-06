/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:14
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-06 14:52:14
 */
package com.phlink.core.web.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.web.entity.City;
import com.phlink.core.web.mapper.CityMapper;
import com.phlink.core.web.service.CityService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wen
 */
@Service("cityService")
@Transactional
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements CityService {

    @Override
    public List<City> listByProvinceId(String provinceId) {
        LambdaQueryWrapper<City> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(City::getProvinceId, provinceId);
        return list(queryWrapper);
    }
}
