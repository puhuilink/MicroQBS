package com.phlink.bus.api.serviceorg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.serviceorg.domain.School;
import com.phlink.bus.api.serviceorg.domain.VO.SchoolViewVO;

/**
 * @author wen
 */
public interface SchoolMapper extends BaseMapper<School> {

    /**
     * 学校
     * @param page
     * @param schoolViewVO
     * @return
     */
    IPage<School> listSchools(Page page, SchoolViewVO schoolViewVO);
}
