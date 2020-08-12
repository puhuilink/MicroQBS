package com.puhuilink.qbs.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.web.entity.Dict;

import java.util.List;

public interface DictService extends IService<Dict> {
    /**
     * 排序获取全部
     * @return
     */
    List<Dict> listAllOrderBySortOrder();

    /**
     * 通过type获取
     * @param type
     * @return
     */
    Dict getByType(String type);

    /**
     * 模糊搜索
     * @param key
     * @return
     */
    List<Dict> listByTitleOrTypeLike(String key);
}
