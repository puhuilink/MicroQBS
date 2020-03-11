package com.phlink.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.core.entity.Dict;
import com.phlink.core.entity.DictData;
import com.phlink.core.mapper.DictDataMapper;
import com.phlink.core.mapper.DictMapper;
import com.phlink.core.service.DictDataService;
import com.phlink.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    @Override
    public List<DictData> listByCondition(DictData dictData) {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(dictData.getTitle())) {
            queryWrapper.lambda().like(DictData::getTitle, dictData.getTitle());
        }
        if(dictData.getStatus() != null) {
            queryWrapper.lambda().eq(DictData::getStatus, dictData.getStatus());
        }
        if(StrUtil.isNotBlank(dictData.getDictId())) {
            queryWrapper.lambda().eq(DictData::getDictId, dictData.getDictId());
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<DictData> listByDictId(String dictId) {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DictData::getDictId, dictId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteByDictId(String dictId) {
        UpdateWrapper<DictData> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(DictData::getDictId, dictId);
        baseMapper.delete(wrapper);

    }
}