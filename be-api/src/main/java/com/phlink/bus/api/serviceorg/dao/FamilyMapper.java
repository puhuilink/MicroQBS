package com.phlink.bus.api.serviceorg.dao;

import com.phlink.bus.api.serviceorg.domain.Family;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wen
 */
public interface FamilyMapper extends BaseMapper<Family> {

    Family getByStudentAndMainGuardian(@Param("studentId") Long studentId, @Param("guardianId") Long guardianId);
}
