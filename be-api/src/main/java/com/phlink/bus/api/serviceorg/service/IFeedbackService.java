package com.phlink.bus.api.serviceorg.service;

import com.phlink.bus.api.serviceorg.domain.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
/**
 * @author wen
 */
public interface IFeedbackService extends IService<Feedback> {


    /**
    * 获取详情
    */
    Feedback findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param feedback
    * @return
    */
    IPage<Feedback> listFeedbacks(QueryRequest request, Feedback feedback);

    /**
    * 新增
    * @param feedback
    */
    void createFeedback(Feedback feedback);

    /**
    * 修改
    * @param feedback
    */
    void modifyFeedback(Feedback feedback);

    /**
    * 批量删除
    * @param feedbackIds
    */
    void deleteFeedbacks(String[] feedbackIds);
}
