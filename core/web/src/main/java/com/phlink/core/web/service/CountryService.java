package com.phlink.core.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.core.web.entity.Country;

import java.util.List;

public interface CountryService extends IService<Country> {
    List<Country> listByCityId(String cityId);
}
