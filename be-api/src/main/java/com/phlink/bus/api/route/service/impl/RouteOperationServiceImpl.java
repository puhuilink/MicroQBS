package com.phlink.bus.api.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.properties.BusApiProperties;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.domain.enums.GroupTypeEnum;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.notify.event.ImGroupCreateEvent;
import com.phlink.bus.api.notify.event.ImGroupDeleteEvent;
import com.phlink.bus.api.notify.event.ImGroupStudentEnterEvent;
import com.phlink.bus.api.route.dao.RouteOperationMapper;
import com.phlink.bus.api.route.domain.Route;
import com.phlink.bus.api.route.domain.RouteOperation;
import com.phlink.bus.api.route.domain.Trip;
import com.phlink.bus.api.route.domain.vo.BindStudentVO;
import com.phlink.bus.api.route.domain.vo.RouteOperationDetailVO;
import com.phlink.bus.api.route.domain.vo.SaveRouteOperationVO;
import com.phlink.bus.api.route.service.IRouteOperationService;
import com.phlink.bus.api.route.service.IRouteService;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wen
 */
@Service
public class RouteOperationServiceImpl extends ServiceImpl<RouteOperationMapper, RouteOperation> implements IRouteOperationService {
    @Autowired
    private IRouteService routeService;
    @Lazy
    @Autowired
    private ITripService tripService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private IBusService busService;
    @Autowired
    private BusApiProperties busApiProperties;
    @Autowired
    private IImGroupsService groupsService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private IGuardianService guardianService;

    @Override
    public RouteOperation findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<RouteOperation> listRouteOperations(QueryRequest request, RouteOperation routeOperation) {
        Page<RouteOperation> page = new Page<>(request.getPageNum(), request.getPageSize());

        return baseMapper.listRouteOperations(page, routeOperation);
    }

    @Override
    public List<RouteOperation> listRouteOperations(RouteOperation routeOperation) {
        Page<RouteOperation> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        baseMapper.listRouteOperations(page, routeOperation);
        return page.getRecords();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createRouteOperation(RouteOperation routeOperation) throws RedisConnectException, BusApiException {
        // 检查车辆是否绑定过
        LambdaQueryWrapper<RouteOperation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RouteOperation::getBindBusId, routeOperation.getBindBusId());
        Integer checkBus = baseMapper.selectCount(lambdaQueryWrapper);
        if (checkBus > 0) {
            // 车辆已经绑定过了
            throw new BusApiException("车辆已经绑定过路线");
        }

        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RouteOperation::getBindBusTeacherId, routeOperation.getBindBusTeacherId());
        Integer checkTeacher = baseMapper.selectCount(lambdaQueryWrapper);
        if (checkTeacher > 0) {
            // 老师已经绑定过了
            throw new BusApiException("随车老师已经绑定过路线");
        }

        Bus bus = busService.getById(routeOperation.getBindBusId());
        if (bus == null) {
            throw new BusApiException("车辆不存在");
        }
        this.save(routeOperation);
        this.redisService.hset(BusApiConstant.ROUTE_BUS, String.valueOf(routeOperation.getRouteId()), bus.getBusCode());
        Route route = routeService.findById(routeOperation.getRouteId());
        // 创建群组
        ImGroups imGroups = new ImGroups();
        imGroups.setCompanyId(busApiProperties.getIm().getRouteOrgId());
        imGroups.setType(GroupTypeEnum.CUSTOMIZE);
        imGroups.setManagerId(routeOperation.getBindBusTeacherId());
        imGroups.setName(route.getRouteName());
        imGroups.setDepartId(String.valueOf(routeOperation.getId()));
        context.publishEvent(new ImGroupCreateEvent(this, imGroups, new Long[]{routeOperation.getBindDriverId()}));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public RouteOperation createRouteOperation(SaveRouteOperationVO routeOperationVO) throws BusApiException {
        Route route = routeService.findById(routeOperationVO.getRouteId());
        if(route == null) {
            throw new BusApiException("绑定的路线不存在");
        }
        // 检查车辆是否绑定过
        LambdaQueryWrapper<RouteOperation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RouteOperation::getBindBusId, routeOperationVO.getBindBusId());
        Integer checkBus = baseMapper.selectCount(lambdaQueryWrapper);
        if (checkBus > 0) {
            // 车辆已经绑定过了
            throw new BusApiException("车辆已经绑定过路线");
        }

        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RouteOperation::getBindBusTeacherId, routeOperationVO.getBindBusTeacherId());
        Integer checkTeacher = baseMapper.selectCount(lambdaQueryWrapper);
        if (checkTeacher > 0) {
            // 老师已经绑定过了
            throw new BusApiException("随车老师已经绑定过路线");
        }

        Bus bus = busService.getById(routeOperationVO.getBindBusId());
        if (bus == null) {
            throw new BusApiException("车辆不存在");
        }
        RouteOperation routeOperation = new RouteOperation();
        routeOperation.setBindBusId(routeOperationVO.getBindBusId());
        routeOperation.setBindBusTeacherId(routeOperationVO.getBindBusTeacherId());
        routeOperation.setBindDriverId(routeOperationVO.getBindDriverId());
        routeOperation.setRouteId(routeOperationVO.getRouteId());
        this.save(routeOperation);

        // 创建群组
        ImGroups imGroups = new ImGroups();
        imGroups.setCompanyId(busApiProperties.getIm().getRouteOrgId());
        imGroups.setType(GroupTypeEnum.CUSTOMIZE);
        imGroups.setManagerId(routeOperation.getBindBusTeacherId());
        imGroups.setName(route.getRouteName());
        imGroups.setDepartId(String.valueOf(routeOperation.getId()));
        context.publishEvent(new ImGroupCreateEvent(this, imGroups, new Long[]{routeOperation.getBindDriverId()}));

        // 绑定学生
        List<Long> updateBindStudentIds = new ArrayList<>();
        for (BindStudentVO bindStudentVO : routeOperationVO.getBindStudents()) {
            if(bindStudentVO.getStudentIds() == null || bindStudentVO.getStudentIds().isEmpty()) {
                continue;
            }
            updateBindStudentIds.addAll(bindStudentVO.getStudentIds());
            studentService.updateStudentStopId(bindStudentVO.getStopId(), bindStudentVO.getStudentIds(), routeOperation);
        }

        // 绑定站点异步操作
        context.publishEvent(new ImGroupStudentEnterEvent(this, routeOperation.getId(), updateBindStudentIds));
        return routeOperation;
    }

    @Override
    public RouteOperation updateRouteOperation(SaveRouteOperationVO routeOperationVO) throws BusApiException {

        RouteOperation oldRouteOperation = baseMapper.selectById(routeOperationVO.getId());
        ImGroups imGroups = groupsService.getByDepartId(routeOperationVO.getId());
        if(!oldRouteOperation.getBindBusTeacherId().equals(routeOperationVO.getBindBusTeacherId())) {
            // 更换群主
            try {
                groupsService.updateManager(imGroups.getGroupId(), routeOperationVO.getBindBusTeacherId());
            } catch (BusApiException | RedisConnectException e) {
                e.printStackTrace();
            }
            imGroups.setManagerId(routeOperationVO.getBindBusTeacherId());
            oldRouteOperation.setBindBusTeacherId(routeOperationVO.getBindBusTeacherId());
        }
        if(!oldRouteOperation.getBindDriverId().equals(routeOperationVO.getBindDriverId())) {
            // 更换成员
            try {
                groupsService.remove(imGroups.getGroupId(), new Long[]{oldRouteOperation.getBindDriverId()});
                groupsService.invite(imGroups.getGroupId(), new Long[]{routeOperationVO.getBindDriverId()});
            } catch (RedisConnectException e) {
                e.printStackTrace();
            }
            imGroups.removeMembers(new Long[]{oldRouteOperation.getBindDriverId()});
            imGroups.addMembers(new Long[]{routeOperationVO.getBindDriverId()});
            oldRouteOperation.setBindDriverId(routeOperationVO.getBindDriverId());
        }
        oldRouteOperation.setRouteId(routeOperationVO.getRouteId());
        oldRouteOperation.setBindBusId(routeOperationVO.getBindBusId());
        this.updateById(oldRouteOperation);

        // 绑定学生
        // 之前绑定的学生
        List<Student> students = studentService.listByRouteOperationId(oldRouteOperation.getId());
        List<Long> preStudentIds = students.stream().map(Student::getId).collect(Collectors.toList());
        List<Long> unbindStudents = students.stream().map(Student::getId).collect(Collectors.toList());
        List<Long> newBindStudentIds = new ArrayList<>();
        // 现在需要在路线绑定的学生
        for (BindStudentVO bindStudentVO : routeOperationVO.getBindStudents()) {
            if(bindStudentVO.getStudentIds() == null || bindStudentVO.getStudentIds().isEmpty()) {
                continue;
            }
            newBindStudentIds.addAll(bindStudentVO.getStudentIds());
            studentService.updateStudentStopId(bindStudentVO.getStopId(), bindStudentVO.getStudentIds(), bindStudentVO.getRouteOperationId());
        }
        unbindStudents.removeAll(newBindStudentIds);
        newBindStudentIds.removeAll(preStudentIds);

        // 需要移除群组的家长
        List<Long> unbindGuardianIdList = new ArrayList<>();
        if(!unbindStudents.isEmpty()) {
            studentService.unbindRoute(unbindStudents);
            for (Long studentId : unbindStudents) {
                // 其他监护人
                List<Guardian> guardianList = guardianService.listOtherGuardian(studentId);
                for(Guardian guardian : guardianList) {
                    //是否还有其他孩子在同一个路线关联中
                    List<Student> allStudents = studentService.listByGuardianInRouteOperation(guardian.getUserId(), routeOperationVO.getId());
                    if(allStudents.size() > 1) {
                        continue;
                    }
                    unbindGuardianIdList.add(guardian.getUserId());
                }
            }
        }
        // 需要邀请到群组的家长
        List<Long> bindGuardianIdList = new ArrayList<>();
        if(!newBindStudentIds.isEmpty()) {
            for (Long studentId : newBindStudentIds) {
                // 获取其他监护人
                List<Guardian> guardianList = guardianService.listOtherGuardian(studentId);
                for(Guardian guardian : guardianList) {
                    bindGuardianIdList.add(guardian.getUserId());
                }
            }
        }

        //从需要删除的家长ID中去除需要加入的家长
        unbindGuardianIdList.removeAll(bindGuardianIdList);

        // 将家长从路线的群组移除
        ImGroups groups = groupsService.getByDepartId(oldRouteOperation.getId());
        try {
            if(!unbindGuardianIdList.isEmpty()) {
                groupsService.remove(groups.getGroupId(), unbindGuardianIdList.toArray(new Long[0]));
            }
            if(!bindGuardianIdList.isEmpty()) {
                groupsService.invite(groups.getGroupId(), bindGuardianIdList.toArray(new Long[0]));
            }
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }


        return oldRouteOperation;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void modifyRouteOperation(RouteOperation routeOperation) {
        RouteOperation oldRouteOperation = baseMapper.selectById(routeOperation.getId());
        ImGroups imGroups = groupsService.getByDepartId(routeOperation.getId());
        if(!oldRouteOperation.getBindBusTeacherId().equals(routeOperation.getBindBusTeacherId())) {
            // 更换群主
            imGroups.setManagerId(routeOperation.getBindBusTeacherId());
            try {
                groupsService.updateManager(imGroups.getGroupId(), routeOperation.getBindBusTeacherId());
            } catch (BusApiException | RedisConnectException e) {
                e.printStackTrace();
            }
        }
        if(!oldRouteOperation.getBindDriverId().equals(routeOperation.getBindDriverId())) {
            // 更换成员
            imGroups.removeMembers(new Long[]{oldRouteOperation.getBindDriverId()});
            imGroups.addMembers(new Long[]{routeOperation.getBindDriverId()});
            try {
                groupsService.remove(imGroups.getGroupId(), new Long[]{oldRouteOperation.getBindDriverId()});
                groupsService.invite(imGroups.getGroupId(), new Long[]{routeOperation.getBindDriverId()});
            } catch (RedisConnectException e) {
                e.printStackTrace();
            }
        }
        this.updateById(routeOperation);
//        try {
//            this.redisService.hset(BusApiConstant.ROUTE_BUS, String.valueOf(routeOperation.getRouteId()), routeOperation.getBusCode());
//        } catch (RedisConnectException e) {
//            log.error(e.getMessage());
//        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteRouteOperations(String[] routeAssociateIds) {
        List<Long> list = Stream.of(routeAssociateIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
        //更新学生的关联关系
        studentService.deleteRouteOperation(list);
        // 获取群组ID
        List<ImGroups> imGroups = groupsService.listByDepartIds(routeAssociateIds);
        List<String> groupIds = new ArrayList<>();
        imGroups.forEach( g -> {
            groupIds.add(String.valueOf(g.getGroupId()));
        });
        context.publishEvent(new ImGroupDeleteEvent(this, groupIds.toArray(new String[0])));
    }

    @Override
    public RouteOperation getByBusCode(String busCode) {
        return baseMapper.getByBusCode(busCode);
    }

    @Override
    public List<Route> listUnbindRoutes(Long routeId) {
        return this.baseMapper.listUnbindRoutes(routeId);
    }

    @Override
    public RouteOperationDetailVO getDetailVO(Long routeOperationId) {
        Trip trip = tripService.getNowTripForRouteOperation(routeOperationId);
        Integer direction = 1;
        if (trip != null) {
            direction = trip.getDirectionId().getValue();
        }
        return baseMapper.getDetailVO(routeOperationId, direction);
    }

    @Override
    public RouteOperation getByBusId(Long busId) {
        LambdaQueryWrapper<RouteOperation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RouteOperation::getBindBusId, busId);
        queryWrapper.eq(RouteOperation::getDeleted, false);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public RouteOperation getByWorkerId(Long userId) {
        return baseMapper.getByWorkerId(userId);
    }

    @Override
    public List<RouteOperation> listByRouteIds(List<Long> routeIds) {
        LambdaQueryWrapper<RouteOperation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RouteOperation::getRouteId, routeIds);
        return list(queryWrapper);
    }
}
