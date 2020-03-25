package com.phlink.core.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.web.entity.Country;
import com.phlink.core.web.mapper.CountryMapper;
import com.phlink.core.web.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wen
 */
@Slf4j
@Service("countryService")
@Transactional
public class CountryServiceImpl extends ServiceImpl<CountryMapper, Country> implements CountryService {

    @Override
    public List<Country> listByCityId(String cityId) {
        LambdaQueryWrapper<Country> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Country::getCityId, cityId);
        return list(queryWrapper);
    }
}
