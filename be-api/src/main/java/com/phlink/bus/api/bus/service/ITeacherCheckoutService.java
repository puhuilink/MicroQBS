package com.phlink.bus.api.bus.service;

import com.phlink.bus.api.bus.domain.TeacherCheckout;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wen
 */
public interface ITeacherCheckoutService extends IService<TeacherCheckout> {


    /**
    * 获取详情
    */
    TeacherCheckout findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param teacherCheckout
    * @return
    */
    IPage<TeacherCheckout> listTeacherCheckouts(QueryRequest request, TeacherCheckout teacherCheckout);

    List<TeacherCheckout> listTeacherCheckouts(TeacherCheckout teacherCheckout);

    /**
    * 新增
    * @param teacherCheckout
    */
    void createTeacherCheckout(TeacherCheckout teacherCheckout);

    /**
    * 修改
    * @param teacherCheckout
    */
    void modifyTeacherCheckout(TeacherCheckout teacherCheckout);

    /**
    * 批量删除
    * @param teacherCheckoutIds
    */
    void deleteTeacherCheckouts(String[] teacherCheckoutIds);
    
    /**
     * 查看当前用户今天是否已经进行过晨检
     * @param userId
     * @return
     */
    TeacherCheckout findIsCheckout(Long userId);
    
    /**
     * 管理平台增加晨检
     * @param teacherCheckout
     * @return
     */
    boolean createTeacherCheckoutOnPlatform(TeacherCheckout teacherCheckout);

    TeacherCheckout getByDay(Long userId, LocalDateTime checkTime);
}
