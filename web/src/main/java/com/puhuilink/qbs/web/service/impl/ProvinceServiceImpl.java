/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:42
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:29
 */
package com.puhuilink.qbs.web.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.web.entity.Province;
import com.puhuilink.qbs.web.mapper.ProvinceMapper;
import com.puhuilink.qbs.web.service.ProvinceService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



@Service("provinceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province> implements ProvinceService {

    @Override
    public List<Province> listByCondition(String name) {
        LambdaQueryWrapper<Province> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Province::getName, name);
        }
        return list(queryWrapper);
    }
}
