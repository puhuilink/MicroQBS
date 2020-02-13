package com.phlink.bus.api.serviceorg.service;

import com.phlink.bus.api.serviceorg.domain.Family;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
/**
 * @author wen
 */
public interface IFamilyService extends IService<Family> {


    /**
    * 获取详情
    */
    Family findById(Long id);

    /**
    * 新增
    * @param family
    */
    void createFamily(Family family);

    /**
    * 修改
    * @param family
    */
    void modifyFamily(Family family);

    /**
    * 批量删除
    * @param familyIds
    */
    void deleteFamilys(String[] familyIds);

    /**
     * 家长和学生所在的家庭
     * @param studentId
     * @param mainGuardianId
     * @return
     */
    Family getByStudentAndMainGuardian(Long studentId, Long mainGuardianId);
}
