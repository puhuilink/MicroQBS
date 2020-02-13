package com.phlink.bus.api.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.system.dao.CityMapper;
import com.phlink.bus.api.system.domain.City;
import com.phlink.bus.api.system.service.ICityService;
import org.springframework.stereotype.Service;

/**
* @author wen
*/
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements ICityService {

    @Override
    public City findById(Long id){
        return this.getById(id);
    }

}
