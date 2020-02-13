package com.phlink.bus.api.bus.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.bus.domain.VO.CheckoutSpecialVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wen
 */
public interface CheckoutMapper{

	IPage<CheckoutSpecialVO> listMorning(Page<CheckoutSpecialVO> page, @Param("vo") CheckoutSpecialVO checkoutSpecialVO);

	IPage<CheckoutSpecialVO> listEvening(Page<CheckoutSpecialVO> page, @Param("vo") CheckoutSpecialVO checkoutSpecialVO);
}
