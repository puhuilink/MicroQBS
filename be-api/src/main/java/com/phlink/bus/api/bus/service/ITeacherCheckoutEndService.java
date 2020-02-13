package com.phlink.bus.api.bus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.bus.domain.TeacherCheckoutEnd;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.system.domain.User;

import java.time.LocalDateTime;

/**
 * @author wen
 */
public interface ITeacherCheckoutEndService extends IService<TeacherCheckoutEnd> {


    /**
    * 获取详情
    */
    TeacherCheckoutEnd findById(Long id);

    /**
     * 查询列表
     * @param request
     * @param realname
     * @param dateStart
     * @param dateEnd
     * @param mobile
     * @return
     */
    IPage<TeacherCheckoutEnd> listTeacherCheckoutEnds(QueryRequest request, String realname, String dateStart, String dateEnd, String mobile);
    /**
    * 新增
     * @param teacherCheckoutEnd
     * @param user
     */
    void createTeacherCheckoutEnd(TeacherCheckoutEnd teacherCheckoutEnd, User user);

    /**
    * 修改
    * @param teacherCheckoutEnd
    */
    void modifyTeacherCheckoutEnd(TeacherCheckoutEnd teacherCheckoutEnd);

    /**
    * 批量删除
    * @param teacherCheckoutEndIds
    */
    void deleteTeacherCheckoutEnds(String[] teacherCheckoutEndIds);

    TeacherCheckoutEnd getByDay(Long userId, LocalDateTime checkTime);
}
