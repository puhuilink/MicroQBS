package com.phlink.bus.api.adinfo.dao;

import com.phlink.bus.api.adinfo.domain.Adinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author Maibenben
 */
public interface AdinfoMapper extends BaseMapper<Adinfo> {

    List<Adinfo> findList();

    int getCount();
}
