package com.puhuilink.qbs.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.auth.entity.Province;

import java.util.List;

public interface ProvinceService extends IService<Province> {
    List<Province> listByCondition(String name);
}
