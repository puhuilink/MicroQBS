package com.phlink.bus.api.serviceorg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.MyPageGuardian;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
public interface GuardianMapper extends BaseMapper<Guardian> {

	IPage<MyPageGuardian> myPageList(Page<MyPageGuardian> page, Guardian guardian);

	Integer countCurrentGuardianNum(@Param(value = "studentId") Long studentId);

	Integer findByUserId(@Param(value = "userId") Long userId);

	Guardian getGuardianDetail(@Param(value = "id") Long id);

	List<Guardian> getGuardianList(@Param("studentId") Long studentId);

	List<Guardian> listOtherGuardian(@Param("studentId") Long studentId);

	List<Guardian> listByUserId(@Param("userIds") Long[] userIds);

    List<Guardian> listOnRouteByWorkerId(@Param("busTeacherId") Long busTeacherId);

}
