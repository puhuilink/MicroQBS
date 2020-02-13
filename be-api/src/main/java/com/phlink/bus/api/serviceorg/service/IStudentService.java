package com.phlink.bus.api.serviceorg.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.route.domain.RouteOperation;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.domain.StudentGuardianInfo;
import com.phlink.bus.api.serviceorg.domain.VO.ServiceInvalidateVO;
import com.phlink.bus.api.serviceorg.domain.VO.StudentViewVO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhouyi
 */
public interface IStudentService extends IService<Student> {

    /**
     * 新增
     *
     * @param student
     */
    void createStudent(Student student) throws BusApiException;

    /**
     * 修改
     *
     * @param student
     */
    void modifyStudent(Student student) throws BusApiException;

    /**
     * 批量删除
     *
     * @param studentIds
     */
    void deleteStudentIds(String[] studentIds);

    /**
     * 查询一个班级的所有学生
     */
    List<Student> getStudentByClassId(Long classId);

    /**
     * 定时轮询学生服务状态
     */
    void studentBusServiceEnd();

    /**
     * 根据班级id获取所有学生
     *
     * @param ClassId
     * @return
     */
    List<Student> getStudentsByClassId(Long ClassId);

    /**
     * 获取学生分页列表，包括学校，班级，主责等信息
     *
     * @param request
     * @param studentViewVO
     * @return
     */
    Page<Student> listStudentsPage(QueryRequest request, StudentViewVO studentViewVO);

    /**
     * 根据id获取学生详细信息
     *
     * @param studentId
     * @return
     */
    Student getStudentDetail(Long studentId);

    /**
     * 批量更新学生的站点ID
     *
     * @param stopId
     * @param studentIds
     * @param routeOperationId
     */
    void updateStudentStopId(Long stopId, List<Long> studentIds, Long routeOperationId);

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    void updateStudentStopId(Long stopId, List<Long> studentIds, RouteOperation routeOperation);

    /**
     * 未绑定站点的学生
     *
     * @param schoolId
     * @return
     */
    List<Student> listStudentUnbindStopInSchool(Long schoolId);

    /**
     * 解绑站点
     *
     * @param stopId
     * @param studentIds
     */
    void removeStudentStopId(Long stopId, List<Long> studentIds, Long routeOperationId) throws BusApiException, RedisConnectException;

    /**
     * 站点的学生列表
     *
     * @param stopId
     * @return
     */
    List<Student> listStudentByStopId(Long stopId);

    /**
     * 站点的学生分页列表
     *
     * @param request
     * @param routeId
     * @param studentViewVO
     * @return
     */
    Page<Student> listPageStudentByRouteId(QueryRequest request, Long routeId, StudentViewVO studentViewVO);

    /**
     * 失效学生服务
     *
     * @param serviceInvalidateVO
     */
    void invalidate(ServiceInvalidateVO serviceInvalidateVO);

    /**
     * 恢复学生服务
     *
     * @param studentId
     */
    void effective(Long studentId);

    /**
     * 批量导入学生信息
     *
     * @param successList
     */
    void batchImport(List<Student> successList) throws BusApiException;

    /**
     * 监护人下的学生列表
     *
     * @param guardianUserId
     * @return
     */
    List<Student> listStudentByGuardian(Long guardianUserId);

    List<Student> listStudentsPage(StudentViewVO studentViewVO);

    Student getStudentInfoById(Long id);

    /**
     * 根据手环code查询学生姓名、学校、班级
     *
     * @param deviceCode
     * @return
     */
    Student getStudentByDeviceCode(String deviceCode);

    /**
     * 删除学生的路线绑定关系
     * @param routeOperationIds
     */
    void deleteRouteOperation(List<Long> routeOperationIds);

    /**
     * 该家长下同一路线中的所有学生
     * @param guardianId
     * @param routeOperationId
     * @return
     */
    List<Student> listByGuardianInRouteOperation(Long guardianId, Long routeOperationId);

    List<Student> listLeaveStudentByTrip(String tripTime, Long tripId, LocalDate day);

    List<StudentGuardianInfo> listStudentGuardianInfoNotLeaveByStop(Long tripId, String tripTime, Long stopId, LocalDate day);

    List<StudentGuardianInfo> listStudentGuardianInfoByStop(Long stopId, Long routeOperationId);

    List<Student> listLeaveStudentByTrip(String tripTime, Long tripId, LocalDate day, Long[] studentIds);

    void unbindStudentStopIds(List<Long> deleteStopIds);

    void updateAllServiceStatus();

    List<Student> listByRouteOperationId(Long routeOperationId);

    void unbindRoute(List<Long> unbindStudents);
}
