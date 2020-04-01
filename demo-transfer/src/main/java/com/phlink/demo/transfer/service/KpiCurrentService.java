/*
 * @Author: sevncz.wen
 * @Date: 2020-04-01 18:18:17
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-04-01 18:24:07
 */
package com.phlink.demo.transfer.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.core.web.base.vo.SearchVO;
import com.phlink.demo.transfer.entity.KpiCurrent;

/**
 * KpiCurrentService
 */
public interface KpiCurrentService extends IService<KpiCurrent> {

	List<KpiCurrent> listByCondition(SearchVO searchVo);

}
