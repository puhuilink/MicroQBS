package com.phlink.qbs.core.web.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.qbs.core.web.entity.DepartmentMaster;

import java.util.List;

/**
 * 部门负责人接口
 */
public interface DepartmentMasterService extends IService<DepartmentMaster> {
    /**
     * 通过部门和负责人类型获取
     * @param departmentId
     * @param type
     * @return
     */
    List<String> listMasterByDepartmentId(String departmentId, Integer type);

    /**
     * 通过部门id删除
     * @param departmentId
     */
    void deleteByDepartmentId(String departmentId);

    /**
     * 通过userId删除
     * @param userId
     */
    void deleteByUserId(String userId);
}
