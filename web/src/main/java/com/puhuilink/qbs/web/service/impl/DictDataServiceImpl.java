/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:52:25
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:11
 */
package com.puhuilink.qbs.web.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.web.entity.DictData;
import com.puhuilink.qbs.web.mapper.DictDataMapper;
import com.puhuilink.qbs.web.service.DictDataService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    @Override
    public List<DictData> listByCondition(DictData dictData) {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dictData.getTitle())) {
            queryWrapper.lambda().like(DictData::getTitle, dictData.getTitle());
        }
        if (dictData.getStatus() != null) {
            queryWrapper.lambda().eq(DictData::getStatus, dictData.getStatus());
        }
        if (StringUtils.isNotBlank(dictData.getDictId())) {
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
