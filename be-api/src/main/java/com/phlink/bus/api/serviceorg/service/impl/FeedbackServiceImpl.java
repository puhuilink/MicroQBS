package com.phlink.bus.api.serviceorg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.serviceorg.dao.FeedbackMapper;
import com.phlink.bus.api.serviceorg.domain.Calendar;
import com.phlink.bus.api.serviceorg.domain.Feedback;
import com.phlink.bus.api.serviceorg.service.IFeedbackService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.system.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author wen
*/
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {


    @Override
    public Feedback findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<Feedback> listFeedbacks(QueryRequest request, Feedback feedback){
        Page<Feedback> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", BusApiConstant.ORDER_DESC, true);
        return this.baseMapper.listFeedbacks(page, feedback);
    }

    @Override
    @Transactional
    public void createFeedback(Feedback feedback) {
        User user = BusApiUtil.getCurrentUser();
    	feedback.setUserId(user.getUserId());
    	feedback.setMobile(user.getMobile());
    	feedback.setRealname(user.getRealname());
    	feedback.setState("0");
        feedback.setCreateTime(LocalDateTime.now());
        this.save(feedback);
    }

    @Override
    @Transactional
    public void modifyFeedback(Feedback feedback) {
        this.updateById(feedback);
    }

    @Override
    public void deleteFeedbacks(String[] feedbackIds) {
        List<Long> list = Stream.of(feedbackIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }
}
