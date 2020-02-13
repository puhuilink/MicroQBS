package com.phlink.bus.api.system.service;


import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.system.domain.Dept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface DeptService extends IService<Dept> {

    Map<String, Object> findDepts(QueryRequest request, Dept dept);

    List<Dept> findDepts(Dept dept, QueryRequest request);

    void createDept(Dept dept);

    void registerToImServer(Dept dept, Boolean createGroup);

    void updateToImServer(Dept dept, Boolean createGroup);

    void updateDept(Dept dept);

    void deleteDepts(String[] deptIds);

    /**
     * 获得自己以及所有子部门
     * @param deptId
     * @return
     */
    List<Dept> listChildrenDepts(Long deptId);

    Dept getByDeptName(String deptName);
}
