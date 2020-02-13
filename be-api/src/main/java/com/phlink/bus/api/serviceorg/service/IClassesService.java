package com.phlink.bus.api.serviceorg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.serviceorg.domain.Classes;
import com.phlink.bus.api.serviceorg.domain.VO.ClassesViewVO;

import java.util.List;

/**
 * @author zhouyi
 */
public interface IClassesService extends IService<Classes> {


    /**
     * 获取详情
     */
    Classes findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param classesViewVO
    * @return
    */
    IPage<Classes> listClassess(QueryRequest request, ClassesViewVO classesViewVO);

    List<Classes> listClassess(ClassesViewVO classesViewVO);

    /**
    * 新增
    * @param classes
    */
    void createClasses(Classes classes) throws BusApiException;

    /**
    * 修改
    * @param classes
    */
    void modifyClasses(Classes classes) throws BusApiException;

    /**
    * 批量删除
    * @param classesIds
    */
    void deleteClassess(String[] classesIds);

    /**
     * 自动升级年级
     */
    void gradeUp();

    /**
     * 根据学校id获取班级信息
     *
     * @param schoolId
     * @return
     */
    List<Classes> getClassesBySchoolId(Long schoolId);

    /**
     * 获取学校已录入的年级
     * @param schoolId
     * @return
     */
    String listGradeClassesCascade(Long schoolId);

    /**
     * 根据条件查询一个班级信息
     * @param schoolId
     * @param grade
     * @param classLevel
     * @return
     */
    Classes getOneByQuery(Long schoolId, Integer grade, Integer classLevel);

    Classes getOne(Long schoolId, Integer startYear, Integer grade, Integer classLevel);
}
