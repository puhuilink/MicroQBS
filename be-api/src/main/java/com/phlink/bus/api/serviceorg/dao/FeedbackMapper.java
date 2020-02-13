package com.phlink.bus.api.serviceorg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.serviceorg.domain.Feedback;
import org.apache.ibatis.annotations.Param;

/**
 * @author wen
 */
public interface FeedbackMapper extends BaseMapper<Feedback> {

    IPage<Feedback> listFeedbacks(Page<Feedback> page, @Param("feedback") Feedback feedback);
}
