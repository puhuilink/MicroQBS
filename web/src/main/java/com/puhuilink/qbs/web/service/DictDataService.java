package com.puhuilink.qbs.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.web.entity.DictData;

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
