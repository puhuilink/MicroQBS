package com.phlink.qbs.core.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.qbs.core.web.entity.Province;

import java.util.List;

public interface ProvinceService extends IService<Province> {
    List<Province> listByCondition(String name);
}
