package com.phlink.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.entity.Department;
import com.phlink.core.entity.Dict;
import com.phlink.core.mapper.DepartmentMapper;
import com.phlink.core.mapper.DictMapper;
import com.phlink.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
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