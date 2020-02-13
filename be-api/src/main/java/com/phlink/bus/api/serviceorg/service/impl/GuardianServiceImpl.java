package com.phlink.bus.api.serviceorg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.notify.event.AddOtherGuardianEvent;
import com.phlink.bus.api.notify.event.LeavePermissionChangeEvent;
import com.phlink.bus.api.serviceorg.dao.GuardianMapper;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.domain.enums.UserStatusEnum;
import com.phlink.bus.api.system.service.UserRoleService;
import com.phlink.bus.api.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wen
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GuardianServiceImpl extends ServiceImpl<GuardianMapper, Guardian> implements IGuardianService {

    @Autowired
    private UserService userService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IImGroupsService groupsService;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public IPage<Guardian> listGuardians(QueryRequest request, Guardian guardian) {
        QueryWrapper<Guardian> queryWrapper = new QueryWrapper<>();
        // TODO:查询条件
        Page<Guardian> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createGuardian(Guardian guardian) throws Exception {
        guardian.setCreateTime(LocalDateTime.now());
        this.save(guardian);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void addOtherGuardian(Long studentId, String realname, String mobile, String idCard) throws Exception {
        Student student = studentService.getById(studentId);
        if (student == null) {
            throw new BusApiException("学生不存在");
        }
        if (baseMapper.countCurrentGuardianNum(studentId) > 5) {
            String message = "添加共同监护人数量超过5个，不能添加";
            throw new BusApiException(message);
        }

        // 学生绑定的监护人 idcard 是否存在
        Guardian idcardChecekGuardian = getGuardianByIdcard(idCard);
        if (idcardChecekGuardian != null) {
            Long[] bindStudentIds = idcardChecekGuardian.getStudentId();
            if (bindStudentIds != null && Arrays.binarySearch(bindStudentIds, studentId) > 0) {
                throw new BusApiException("该学生不能添加重复身份证监护人");
            }
        }

        User checkUser = checkExistGuardianUser(realname, mobile, idCard);
        if (checkUser == null) {
            // 该用户不存在，创建用户
            checkUser = userService.createGuardianUser(mobile, realname, idCard);
        }
        // 是否存在家长
        Guardian guardian = getByGuardianId(checkUser.getUserId());
        if (guardian == null) {
            guardian = new Guardian();
        } else {
            Long[] bindStudentIds = guardian.getStudentId();
            if (bindStudentIds != null && Arrays.binarySearch(bindStudentIds, studentId) > 0) {
                throw new BusApiException("该学生不能添加重复手机号监护人");
            }
        }
        checkUser.setRoleId(String.valueOf(BusApiConstant.ROLE_MAINGUARDIAN));
        userService.updateUser(checkUser);

        guardian.setUserId(checkUser.getUserId());
        guardian.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        guardian.setCreateTime(LocalDateTime.now());
        guardian.setDeleted(false);
        guardian.addStudent(studentId);
        guardian.setIdcard(idCard);
        this.saveOrUpdate(guardian);
        // 将监护人加入群组
        intoGroup(student.getRouteOperationId(), new Long[]{guardian.getUserId()});
        // 主责添加监护人
        context.publishEvent(new AddOtherGuardianEvent(this, checkUser.getRealname(), checkUser.getMobile(), student.getName()));
    }

    private User checkExistGuardianUser(String realname, String mobile, String idCard) throws BusApiException {
        User user = userService.findByMobile(mobile);
        if(user != null) {
            if(StringUtils.isNotBlank(user.getIdcard()) && !idCard.equals(user.getIdcard())){
                // 手机号相同，身份证号码不同
                throw new BusApiException("该监护人信息系统已存在，身份证号码输入不一致");
            }else if(StringUtils.isNotBlank(user.getRealname()) && !realname.equals(user.getRealname())) {
                // 手机号相同，身份证号码相同，姓名不同
                throw new BusApiException("该监护人信息系统已存在，姓名输入不一致");
            }
        }else{
            // 不存在该mobile
            // 检查身份证号是否存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getIdcard, idCard);
            List<User> idcardUsers = userService.list(queryWrapper);
            if(idcardUsers != null) {
                for (User idcardUser : idcardUsers) {
                    if(StringUtils.isNotBlank(idcardUser.getRealname()) && realname.equals(idcardUser.getRealname())) {
                        throw new BusApiException("该监护人身份证号码系统已存在，请重新输入");
                    }
                }
            }
        }
        return user;
    }

    @Override
    @Transactional
    public void intoGroup(Long routeOperationId, Long[] guardianIds) {
        ImGroups groups = groupsService.getByDepartId(routeOperationId);
        if (groups == null) {
            log.error("群组{}不存在", routeOperationId);
            return;
        }
        try {
            groupsService.invite(groups.getGroupId(), guardianIds);
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateLeavePermissions(Long guardianId, Long studentId, Boolean isLeavePermission) {
        // 更新学生表的请假人
        Student student = studentService.getById(studentId);
        if (isLeavePermission) {
            student.setLeaveGuardianId(guardianId);
        } else {
            student.setLeaveGuardianId(BusApiUtil.getCurrentUser().getUserId());
        }
        student.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        student.setModifyTime(LocalDateTime.now());
        studentService.saveOrUpdate(student);
        context.publishEvent(new LeavePermissionChangeEvent(this, guardianId, BusApiUtil.getCurrentUser().getUserId(), studentId, isLeavePermission));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void modifyGuardian(Guardian guardian) {
        guardian.setModifyTime(LocalDateTime.now());
        guardian.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(guardian);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteGuardianIds(String[] guardianIds) throws Exception {
        List<Long> list = Stream.of(guardianIds).map(Long::parseLong).collect(Collectors.toList());
        List<Guardian> guardians = baseMapper.listByUserId(list.toArray(new Long[0]));
        List<Long> ids = guardians.stream().map(Guardian::getId).collect(Collectors.toList());
        removeByIds(ids);
        // 更新角色
        for (Long userId : list) {
            User findByMobile = userService.getById(userId);
            findByMobile.setRoleId(String.valueOf(BusApiConstant.ROLE_VISTOR));
            userService.updateUser(findByMobile);
        }

    }

    @Override
    public void removeGuardianIds(Long studentId, String[] guardianIds) {
        List<Long> list = Stream.of(guardianIds).map(Long::parseLong).collect(Collectors.toList());
        // 将studentId从guardian学生列表中删除
        List<Guardian> guardians = baseMapper.listByUserId(list.toArray(new Long[0]));
        for (Guardian guardian : guardians) {
            Long[] studentIds = guardian.getStudentId();
            List<Long> studentIdList = new ArrayList<>();
            if(studentIds != null) {
                studentIdList = Stream.of(studentIds).collect(Collectors.toList());
                studentIdList.remove(studentId);
            }

//			if(studentIdList.isEmpty()) {
				// 监护人下没有任何学生，则更新角色
//                userRoleService.deleteGuardianRole(guardian.getUserId());
                // 删除监护人
//                userRoleService.addVisitorRole(guardian.getUserId());
//				removeById(guardian.getId());
//			}
            // 更新删除学生之后的监护人信息
            guardian.setStudentId(studentIdList.toArray(new Long[0]));
            JSONObject relations = guardian.getRelation();
            if(relations != null) {
                relations.remove(String.valueOf(studentId));
                guardian.setRelation(relations);
            }
            saveOrUpdate(guardian);

        }


    }

    @Override
    public List<Guardian> listByStudentId(Long studentId) {
        return this.baseMapper.getGuardianList(studentId);
    }

    @Override
    public Guardian getByGuardianId(Long guardianId) {
        LambdaQueryWrapper<Guardian> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Guardian::getUserId, guardianId);
        return this.getOne(queryWrapper);

    }

    @Override
    public void checkMainGuardian(Long studentId) throws BusApiException {
        Long userId = BusApiUtil.getCurrentUser().getUserId();
        Student student = studentService.getById(studentId);
        if (!userId.equals(student.getMainGuardianId())) {
            throw new BusApiException("非主责任人没有权限操作");
        }
    }

    @Override
    public Guardian getGuardianDetail(Long id) {
        return this.baseMapper.getGuardianDetail(id);
    }

    @Override
    public Guardian getGuardianByIdcard(String idcard) {
        QueryWrapper<Guardian> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Guardian::getIdcard, idcard);
        queryWrapper.lambda().eq(Guardian::getDeleted, false);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Guardian> listOtherGuardian(Long studentId) {
        return baseMapper.listOtherGuardian(studentId);
    }

    @Override
    public void invalidate(Long guardianId) {
//		Guardian guardian=this.getById(guardianId);
//        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.lambda().eq(User::getUserId, guardianId);
//        updateWrapper.lambda().set(User::getStatus, UserStatusEnum.INVALIDATE);
//        this.userService.update(updateWrapper);
        User user = userService.getById(guardianId);
        user.setStatus(UserStatusEnum.INVALIDATE.getValue());
        try {
            this.userService.updateProfile(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void effective(Long guardianId) {
//		Guardian guardian=this.getById(guardianId);
//        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.lambda().eq(User::getUserId, guardianId);
//        updateWrapper.lambda().set(User::getStatus, UserStatusEnum.EFFECTIVE);
        User user = userService.getById(guardianId);
        user.setStatus(UserStatusEnum.EFFECTIVE.getValue());
        try {
            this.userService.updateProfile(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Guardian> listOnRouteByWorkerId(Long busTeacherId) {
        return baseMapper.listOnRouteByWorkerId(busTeacherId);
    }

    @Override
    public void checkGuardian(Long studentId, Long userId) throws BusApiException {
        List<Guardian> guardians = baseMapper.listOtherGuardian(studentId);
        List<Long> userIds = guardians.stream().map(Guardian::getUserId).collect(Collectors.toList());
        if (!userIds.contains(userId)) {
            throw new BusApiException("该用户不是学生监护人");
        }
    }

    @Override
    public Guardian getByUserId(Long userId) {
        QueryWrapper<Guardian> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Guardian::getUserId, userId);
        Guardian guardian = null;
        try {
            guardian = this.baseMapper.selectOne(queryWrapper);
        } catch (Exception e) {

        }
        return guardian;
    }
}
