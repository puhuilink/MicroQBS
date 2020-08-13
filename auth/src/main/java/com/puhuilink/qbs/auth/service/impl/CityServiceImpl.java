/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:14
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:12:53
 */
package com.puhuilink.qbs.auth.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.auth.entity.City;
import com.puhuilink.qbs.auth.mapper.CityMapper;
import com.puhuilink.qbs.auth.service.CityService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("cityService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements CityService {

    @Override
    public List<City> listByProvinceId(String provinceId) {
        LambdaQueryWrapper<City> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(City::getProvinceId, provinceId);
        return list(queryWrapper);
    }
}
