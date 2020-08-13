package com.puhuilink.qbs.auth.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.auth.entity.City;

public interface CityService extends IService<City> {
    List<City> listByProvinceId(String provinceId);
}
