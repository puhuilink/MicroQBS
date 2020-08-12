package com.puhuilink.qbs.web.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.web.entity.City;

public interface CityService extends IService<City> {
    List<City> listByProvinceId(String provinceId);
}
