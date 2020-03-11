package com.phlink.core.service;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.Page;
import com.phlink.core.entity.DictData;

import java.util.List;

public interface DictDataService extends IService<DictData> {
    /**
     * 多条件获取
     * @param dictData
     * @return
     */
    List<DictData> listByCondition(DictData dictData);

    /**
     * 通过dictId获取启用字典 已排序
     * @param dictId
     * @return
     */
    List<DictData> listByDictId(String dictId);

    /**
     * 通过dictId删除
     * @param dictId
     */
    void deleteByDictId(String dictId);
}
