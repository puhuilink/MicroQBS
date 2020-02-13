package com.phlink.bus.api.serviceorg.service;

import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.serviceorg.domain.School;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.serviceorg.domain.VO.SchoolViewVO;

import java.util.List;

/**
 * @author wen
 */
public interface ISchoolService extends IService<School> {

    /**
    * 查询列表
    * @param request
    * @param schoolViewVO
    * @return
    */
    IPage<School> listSchools(QueryRequest request, SchoolViewVO schoolViewVO);

    List<School> listSchools(SchoolViewVO schoolViewVO);

    /**
     *
     * @return
     */
    List<School> listSchools(School school);

    /**
    * 新增
    * @param school
    */
    void createSchool(School school) throws BusApiException;

    /**
    * 修改
    * @param school
    */
    void modifySchool(School school);

    /**
    * 批量删除
    * @param schoolIds
    */
    void deleteSchoolIds(String[] schoolIds);

    School findByName(String schoolName);

    /**
     * 根据学校名称创建学校
     * @param schoolName
     * @return
     */
    School createSchool(String schoolName);
}
