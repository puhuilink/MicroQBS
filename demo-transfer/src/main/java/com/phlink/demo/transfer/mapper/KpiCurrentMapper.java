/*
 * @Author: sevncz.wen
 * @Date: 2020-04-01 18:18:13
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-04-02 15:54:12
 */
package com.phlink.demo.transfer.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.demo.transfer.entity.KpiCurrent;

import org.apache.ibatis.annotations.Param;

/**
 * KpiCurrentMapper
 */
public interface KpiCurrentMapper extends BaseMapper<KpiCurrent>{

	List<KpiCurrent> listByOffset(@Param("pageLimit") Integer pageLimit, @Param("pageOffset") Integer pageOffset);

}
