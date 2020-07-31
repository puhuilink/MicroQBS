package com.puhuilink.qbs.core.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.core.web.entity.Province;

import java.util.List;

public interface ProvinceService extends IService<Province> {
    List<Province> listByCondition(String name);
}
