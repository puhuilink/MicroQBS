/*
 * @Author: sevncz.wen
 * @Date: 2020-04-01 18:18:13
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-04-01 20:46:15
 */
package com.phlink.demo.transfer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.demo.transfer.entity.KpiCurrent;

/**
 * KpiCurrentMapper
 */
@DS("recMain")
public interface KpiCurrentMapper extends BaseMapper<KpiCurrent>{


}
