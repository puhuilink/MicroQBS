/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:10
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:12:54
 */
package com.phlink.core.web.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.web.entity.Country;
import com.phlink.core.web.mapper.CountryMapper;
import com.phlink.core.web.service.CountryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("countryService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CountryServiceImpl extends ServiceImpl<CountryMapper, Country> implements CountryService {

    @Override
    public List<Country> listByCityId(String cityId) {
        LambdaQueryWrapper<Country> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Country::getCityId, cityId);
        return list(queryWrapper);
    }
}
