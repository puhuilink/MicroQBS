package com.phlink.bus.api.serviceorg.service;

import com.phlink.bus.api.serviceorg.domain.HomeWorkPage;
import com.phlink.bus.api.serviceorg.domain.Homework;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
/**
 * @author Administrator
 */
public interface IHomeworkService extends IService<Homework> {

    /**
    * 查询列表
    * @param request
    * @param homework
    * @return
    */
    IPage<Homework> listHomeworks(QueryRequest request, Homework homework);

    /**
    * 新增
    * @param homework
    */
    void createHomework(Homework homework);

    /**
    * 修改
    * @param homework
    */
    void modifyHomework(Homework homework);

    /**
    * 批量删除
    * @param homeworkIds
    */
    void deleteHomeworkIds(String[] homeworkIds);

    IPage<HomeWorkPage> queryHomework(Long id, @NotBlank(message = "{required}") String createTime,QueryRequest request);

	void pushHomework(List<Homework> list) throws Exception;
}
