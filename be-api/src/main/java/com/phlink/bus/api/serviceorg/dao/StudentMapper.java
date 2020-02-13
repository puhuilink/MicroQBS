package com.phlink.bus.api.serviceorg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.domain.StudentGuardianInfo;
import com.phlink.bus.api.serviceorg.domain.VO.ServiceInvalidateVO;
import com.phlink.bus.api.serviceorg.domain.VO.StudentViewVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhouyi
 */
public interface StudentMapper extends BaseMapper<Student> {

    List<Student> getStudentByClassId(@Param("classId") Long classId);

    Page<Student> listStudents(Page<Student> page, @Param("studentViewVO") StudentViewVO studentViewVO);

    Student getStudentDetail(@Param("studentId") Long studentId);

    List<Student> listStudents(@Param("studentViewVO") StudentViewVO studentViewVO);

    void invalidate(@Param("serviceInvalidateVO") ServiceInvalidateVO serviceInvalidateVO);

    List<Student> listStudentByGuardian(@Param("guardianUserId") Long guardianUserId);

    Student findById(@Param("studentId") Long studentId);

    Student getStudentByDeviceCode(@Param("deviceCode") String deviceCode);

    List<Student> listByGuardianInRouteOperation(@Param("guardianId") Long guardianId, @Param("routeOperationId") Long routeOperationId);

    List<Student> listLeaveStudentByTrip(@Param("tripTime") String tripTime, @Param("tripId") Long tripId, @Param("day") LocalDate day, @Param("studentIds") Long[] studentIds);

    List<StudentGuardianInfo> listStudentGuardianInfoNotLeaveByStop(@Param("tripId") Long tripId, @Param("tripTime") String tripTime, @Param("stopId") Long stopId, @Param("day") LocalDate day);

    List<StudentGuardianInfo> listStudentGuardianInfoByStop(@Param("stopId") Long stopId, @Param("routeOperationId") Long routeOperationId);

    List<Student> listStudentUnbindStopInSchool(@Param("schoolId") Long schoolId);
}
