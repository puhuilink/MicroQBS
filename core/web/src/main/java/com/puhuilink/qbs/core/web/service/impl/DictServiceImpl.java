/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:29
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:16
 */
package com.puhuilink.qbs.core.web.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.core.web.entity.Dict;
import com.puhuilink.qbs.core.web.mapper.DictMapper;
import com.puhuilink.qbs.core.web.service.DictService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    public List<Dict> listAllOrderBySortOrder() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(Dict::getSortOrder);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Dict getByType(String type) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Dict::getType, type);
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Dict> listByTitleOrTypeLike(String key) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(Dict::getTitle, key).or().like(Dict::getType, key);
        return baseMapper.selectList(queryWrapper);
    }
}
