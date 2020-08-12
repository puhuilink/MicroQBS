package com.puhuilink.qbs.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.web.entity.Country;

import java.util.List;

public interface CountryService extends IService<Country> {
    List<Country> listByCityId(String cityId);
}
