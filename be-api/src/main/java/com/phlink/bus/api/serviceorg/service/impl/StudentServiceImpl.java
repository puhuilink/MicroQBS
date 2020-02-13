package com.phlink.bus.api.serviceorg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.route.domain.Route;
import com.phlink.bus.api.route.domain.RouteOperation;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.route.service.IRouteOperationService;
import com.phlink.bus.api.route.service.IRouteService;
import com.phlink.bus.api.route.service.IStopService;
import com.phlink.bus.api.serviceorg.dao.StudentMapper;
import com.phlink.bus.api.serviceorg.domain.*;
import com.phlink.bus.api.serviceorg.domain.VO.ServiceInvalidateVO;
import com.phlink.bus.api.serviceorg.domain.VO.StudentViewVO;
import com.phlink.bus.api.serviceorg.domain.enums.ServiceStatusEnum;
import com.phlink.bus.api.serviceorg.service.IClassesService;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.serviceorg.service.ISchoolService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserRoleService;
import com.phlink.bus.api.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouyi
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private IGuardianService guardianService;
    @Autowired
    private IRouteOperationService routeOperationService;
    @Autowired
    private IStopService stopService;
    @Autowired
    private IRouteService routeService;
    @Autowired
    private ISchoolService schoolService;
    @Autowired
    private IClassesService classesService;
    @Autowired
    private ApplicationContext context;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createStudent(Student student) throws BusApiException {
        try {
            //查询主监护人的信息
            User user = this.userService.findByMobile(student.getMainGuardianMobile());
            if (user == null) {
                //创建账号
                user = userService.createGuardianUser(student.getMainGuardianMobile(), student.getMainGuardianName(), student.getMainGuardianIdcard());
            }else{
                if(!(StringUtils.isNotBlank(student.getMainGuardianName()) && student.getMainGuardianName().equals(user.getRealname())) ||
                        !(StringUtils.isNotBlank(student.getMainGuardianIdcard()) && student.getMainGuardianIdcard().equalsIgnoreCase(user.getIdcard()))){
                    user.setRealname(student.getMainGuardianName());
                    user.setIdcard(student.getMainGuardianIdcard().replace("'", ""));
                    this.userService.updateProfile(user);
                }
            }
            //创建主责任人
            Guardian guardian = this.guardianService.getByGuardianId(user.getUserId());
            if (guardian == null) {
                guardian = new Guardian();
                guardian.setCreateTime(LocalDateTime.now());
                guardian.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
                if(StringUtils.isNotBlank(student.getMainGuardianIdcard())) {
                    guardian.setIdcard(student.getMainGuardianIdcard().replace("'", ""));
                }
                guardian.setUserId(user.getUserId());
                this.guardianService.saveOrUpdate(guardian);
            }
            // 删除游客角色
            this.userRoleService.deleteVisitorRole(user.getUserId());
            // 新增监护人角色
            this.userRoleService.addGuardianRole(user.getUserId());

            student.setMainGuardianId(user.getUserId());
            student.setLeaveGuardianId(user.getUserId());
            // 保存学生数据
            student.setCreateTime(LocalDateTime.now());
            student.setCreateBy(BusApiUtil.getCurrentUser().getUserId());

            this.saveOrUpdate(student);

            JSONObject jsonObject = guardian.getRelation();
            if (jsonObject == null) {
                jsonObject = new JSONObject();
            }
            if (StringUtils.isNotBlank(student.getMainGuardianRelation())) {
                jsonObject.put(String.valueOf(student.getId()), student.getMainGuardianRelation());
                guardian.setRelation(jsonObject);
            }
            guardian.addStudent(student.getId());
            this.guardianService.saveOrUpdate(guardian);
        } catch (Exception e) {
            log.error("学生信息新建失败", e);
            throw new BusApiException("学生信息新建失败，" + e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void modifyStudent(Student student) throws BusApiException {
        try {
            Classes classes = classesService.getOneByQuery(student.getSchoolId(), student.getGrade(), student.getClassLevel());
            //更新学生数据
            student.setModifyTime(LocalDateTime.now());
            student.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
            student.setClassId(classes.getId());
            this.updateById(student);
            User user = this.userService.findByMobile(student.getMainGuardianMobile());
            if (user == null) {
                //更新主责任人
                Guardian guardian = this.guardianService.getGuardianByIdcard(student.getMainGuardianIdcard());
                guardian.setIdcard(student.getMainGuardianIdcard());
//                guardian.addStudent(student.getId());
                guardian.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
                guardian.setModifyTime(LocalDateTime.now());
                this.guardianService.save(guardian);
                //更新账号
                user = new User();
                user.setUsername(student.getMainGuardianMobile());
                user.setRealname(student.getMainGuardianName());
                user.setModifyTime(new Date());
                user.setIdcard(student.getMainGuardianIdcard());
                this.userService.updateUser(user);
            }
        } catch (Exception e) {
            throw new BusApiException("学生信息更新失败，" + e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteStudentIds(String[] studentIds) {
        List<Long> list = Stream.of(studentIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public List<Student> getStudentByClassId(Long classId) {
        // TODO Auto-generated method stub
        return this.baseMapper.getStudentByClassId(classId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void studentBusServiceEnd() {
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Student::getDeleted, false);
        updateWrapper.lambda().eq(Student::getServiceEndDate, LocalDate.now().minusDays(1));
        updateWrapper.lambda().set(Student::getServiceStatus, ServiceStatusEnum.INEFFECTIVE);
        this.update(updateWrapper);
    }

    @Override
    public List<Student> getStudentsByClassId(Long classId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Student::getClassId, classId);
        queryWrapper.lambda().eq(Student::getDeleted, false);
        return this.list(queryWrapper);
    }

    @Override
    public Student getStudentDetail(Long studentId) {
        return this.baseMapper.getStudentDetail(studentId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateStudentStopId(Long stopId, List<Long> studentIds, Long routeOperationId){
        RouteOperation routeOperation = routeOperationService.getById(routeOperationId);
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(Student::getId, studentIds);
        updateWrapper.lambda().set(Student::getStopId, stopId);
        updateWrapper.lambda().set(Student::getRouteOperationId, routeOperationId);
        updateWrapper.lambda().set(Student::getBusId, routeOperation.getBindBusId());
        this.update(updateWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateStudentStopId(Long stopId, List<Long> studentIds, RouteOperation routeOperation){
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(Student::getId, studentIds);
        updateWrapper.lambda().set(Student::getStopId, stopId);
        updateWrapper.lambda().set(Student::getRouteOperationId, routeOperation.getId());
        updateWrapper.lambda().set(Student::getBusId, routeOperation.getBindBusId());
        this.update(updateWrapper);
    }

    @Override
    public List<Student> listStudentUnbindStopInSchool(Long schoolId) {
        return baseMapper.listStudentUnbindStopInSchool(schoolId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void removeStudentStopId(Long stopId, List<Long> studentIds, Long routeOperationId) throws BusApiException, RedisConnectException {
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(Student::getId, studentIds);
        updateWrapper.lambda().eq(Student::getStopId, stopId);
        updateWrapper.lambda().set(Student::getStopId, null);
        updateWrapper.lambda().set(Student::getRouteOperationId, null);
        updateWrapper.lambda().set(Student::getBusId, null);
        this.update(updateWrapper);
    }

    @Override
    public List<Student> listStudentByStopId(Long stopId) {
        StudentViewVO studentViewVO = new StudentViewVO();
        studentViewVO.setStopId(stopId);
        return baseMapper.listStudents(studentViewVO);
    }

    @Override
    public Page<Student> listPageStudentByRouteId(QueryRequest request, Long routeId, StudentViewVO studentViewVO) {
        studentViewVO.setRouteId(routeId);
        Page<Student> page = new Page<>(request.getPageNum(), request.getPageSize());
        return baseMapper.listStudents(page, studentViewVO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void invalidate(ServiceInvalidateVO serviceInvalidateVO) {
        baseMapper.invalidate(serviceInvalidateVO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void effective(Long studentId) {
        UpdateWrapper<Student> wrapper = new UpdateWrapper();
        wrapper.lambda().eq(Student::getId, studentId);
        wrapper.lambda().set(Student::getServiceStatus, ServiceStatusEnum.EFFECTIVE);
        this.update(wrapper);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void batchImport(List<Student> successList) throws BusApiException {
        for (Student student : successList) {
            // 绑定学生和车辆的关系
            // 1. 查看路线关联信息
            RouteOperation routeOperation = routeOperationService.getByBusCode(student.getBusCode());
            if (routeOperation != null) {
                student.setRouteId(routeOperation.getRouteId());
                student.setRouteOperationId(routeOperation.getId());
            }
            // 2. 查看路线信息
            Route route = routeService.findByName(student.getRouteName());
            if(route != null) {
                // 3. 查看站点信息
                Stop stop = stopService.findByName(student.getStopName(), route.getId());
                if (stop != null) {
                    student.setStopId(stop.getId());
                    student.setStopName(stop.getStopName());
                    student.setRouteId(route.getId());
                }
            }
            // 3. 学校信息绑定
            School school = schoolService.findByName(student.getSchoolName());
            if (school == null) {
                school = new School();
                school.setCreateTime(LocalDateTime.now());
                school.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
                school.setSchoolName(student.getSchoolName());
                schoolService.createSchool(school);
            }
            student.setSchoolId(school.getId());
            // 4. 班级信息绑定
            Classes classes = classesService.getOneByQuery(school.getId(), student.getGrade(), student.getClassLevel());
            if (classes == null) {
                classes = new Classes();
                classes.setGrade(student.getGrade());
                classes.setClassLevel(student.getClassLevel());
                classes.setSchoolId(student.getSchoolId());
                classes.setCreateTime(LocalDateTime.now());
                classes.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
                classesService.save(classes);
            }
            student.setClassId(classes.getId());
            this.createStudent(student);
        }
    }

    @Override
    public List<Student> listStudentByGuardian(Long guardianUserId) {
//        return baseMapper.listStudents();
        return baseMapper.listStudentByGuardian(guardianUserId);
    }

    @Override
    public Page<Student> listStudentsPage(QueryRequest request, StudentViewVO studentViewVO) {
        Page<Student> page = new Page<>(request.getPageNum(), request.getPageSize());
        return baseMapper.listStudents(page, studentViewVO);
    }

    @Override
    public List<Student> listStudentsPage(StudentViewVO studentViewVO) {
        Page<Student> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        baseMapper.listStudents(page, studentViewVO);
        return page.getRecords();
    }

    @Override
    public Student getStudentInfoById(Long id) {
        return baseMapper.findById(id);
    }

    @Override
    public Student getStudentByDeviceCode(String deviceCode) {
        return this.baseMapper.getStudentByDeviceCode(deviceCode);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteRouteOperation(List<Long> routeOperationIds) {
        LambdaUpdateWrapper<Student> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Student::getRouteOperationId, routeOperationIds);
        // 清空学生的绑定信息
        updateWrapper.set(Student::getRouteOperationId, null);
        updateWrapper.set(Student::getStopId, null);
        updateWrapper.set(Student::getBusId, null);
        update(updateWrapper);
    }

    @Override
    public List<Student> listByGuardianInRouteOperation(Long guardianId, Long routeOperationId) {
        return baseMapper.listByGuardianInRouteOperation(guardianId, routeOperationId);
    }

    @Override
    public List<Student> listLeaveStudentByTrip(String tripTime, Long tripId, LocalDate day) {
        if(day == null) {
            day = LocalDate.now();
        }
        return listLeaveStudentByTrip(tripTime, tripId, day, null);
    }

    @Override
    public List<StudentGuardianInfo> listStudentGuardianInfoNotLeaveByStop(Long tripId, String tripTime, Long stopId, LocalDate day) {
        return baseMapper.listStudentGuardianInfoNotLeaveByStop(tripId, tripTime, stopId, day);
    }

    @Override
    public List<StudentGuardianInfo> listStudentGuardianInfoByStop(Long stopId, Long routeOperationId) {
        return baseMapper.listStudentGuardianInfoByStop(stopId, routeOperationId);
    }

    @Override
    public List<Student> listLeaveStudentByTrip(String tripTime, Long tripId, LocalDate day, Long[] studentIds) {
        if(day == null) {
            day = LocalDate.now();
        }
        return baseMapper.listLeaveStudentByTrip(tripTime, tripId, day, studentIds);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void unbindStudentStopIds(List<Long> deleteStopIds) {
        LambdaUpdateWrapper<Student> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Student::getStopId, deleteStopIds);
        // 清空学生的绑定信息
        updateWrapper.set(Student::getRouteOperationId, null);
        updateWrapper.set(Student::getStopId, null);
        updateWrapper.set(Student::getBusId, null);
        update(updateWrapper);
    }

    /**
     * 每天凌晨05分执行
     */
    @Scheduled(cron = "0 5 0 * * ?")
    @Override
    public void updateAllServiceStatus(){
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Student::getServiceStatus, ServiceStatusEnum.EFFECTIVE);
        List<Student> students = list(wrapper);
        List<Student> updateStudent = new ArrayList<>();
        for(Student student : students) {
            if(student.getServiceEndDate() != null) {
                if(student.getServiceEndDate().isBefore(LocalDate.now())) {
                    student.setServiceStatus(ServiceStatusEnum.INEFFECTIVE);
                    updateStudent.add(student);
                }
            }else{
                student.setServiceStatus(ServiceStatusEnum.INEFFECTIVE);
                updateStudent.add(student);
            }
        }
        // 批量更新
        saveOrUpdateBatch(updateStudent);
    }

    @Override
    public List<Student> listByRouteOperationId(Long routeOperationId) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getRouteOperationId, routeOperationId);
        return list(queryWrapper);
    }

    @Override
    public void unbindRoute(List<Long> unbindStudentIds) {
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(Student::getId, unbindStudentIds);
        updateWrapper.lambda().set(Student::getRouteOperationId, null);
        updateWrapper.lambda().set(Student::getStopId, null);
        updateWrapper.lambda().set(Student::getBusId, null);
        update(updateWrapper);
    }
}
