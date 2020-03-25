package com.phlink.core.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.core.web.entity.City;
import com.phlink.core.web.entity.Dict;

import java.util.List;

public interface CityService extends IService<City> {
    List<City> listByProvinceId(String provinceId);
}
