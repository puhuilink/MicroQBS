package com.phlink.bus.api.system.service;

import com.phlink.bus.api.system.domain.City;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
/**
 * @author wen
 */
public interface ICityService extends IService<City> {


    /**
    * 获取详情
    */
    City findById(Long id);
}
