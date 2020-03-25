package com.phlink.core.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.web.entity.City;
import com.phlink.core.web.entity.Province;
import com.phlink.core.web.mapper.CityMapper;
import com.phlink.core.web.mapper.ProvinceMapper;
import com.phlink.core.web.service.CityService;
import com.phlink.core.web.service.ProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wen
 */
@Slf4j
@Service("provinceService")
@Transactional
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province> implements ProvinceService {

    @Override
    public List<Province> listByCondition(String name) {
        LambdaQueryWrapper<Province> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotBlank(name)) {
            queryWrapper.like(Province::getName, name);
        }
        return list(queryWrapper);
    }
}
