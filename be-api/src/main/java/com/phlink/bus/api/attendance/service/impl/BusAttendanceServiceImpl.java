package com.phlink.bus.api.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.attendance.dao.BusAttendanceMapper;
import com.phlink.bus.api.attendance.domain.BusAttendance;
import com.phlink.bus.api.attendance.domain.vo.BusAttendanceVO;
import com.phlink.bus.api.attendance.service.IBusAttendanceService;
import com.phlink.bus.api.bus.dao.BusMapper;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.VO.UserBusVO;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.route.dao.RouteOperationMapper;
import com.phlink.bus.api.route.domain.RouteOperation;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ZHOUY
 */
@Service
public class BusAttendanceServiceImpl extends ServiceImpl<BusAttendanceMapper, BusAttendance> implements IBusAttendanceService {

    @Autowired
    private BusMapper busMapper;

    @Autowired
    private RouteOperationMapper routeOperationMapper;

    @Autowired
    private BusAttendanceMapper busAttendanceMapper;

    @Override
    public BusAttendance findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<BusAttendance> listBusAttendances(QueryRequest request, BusAttendance busAttendance) {
        QueryWrapper<BusAttendance> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        Page<BusAttendance> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort = StringUtils.isNotBlank(request.getSortOrder()) ? request.getSortOrder() : "id";
        String order = StringUtils.isNotBlank(request.getSortField()) ? request.getSortField() : BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<BusAttendance> listBusAttendances(BusAttendance busAttendance) {
        QueryWrapper<BusAttendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        //TODO:查询条件
        Page<BusAttendance> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        page.setDesc("id");
        this.page(page, queryWrapper);
        return page.getRecords();
    }

    @Override
    @Transactional
    public void createBusAttendance(BusAttendance busAttendance) throws BusApiException {
        Long userId = BusApiUtil.getCurrentUser().getUserId();
        BusAttendance buatt = busAttendanceMapper.findAttendanceInToday(userId);
        if (buatt != null) {
            if (busAttendance.getType().equals(buatt.getType())) {
                String message = "已经进行过该类型的打卡";
                throw new BusApiException(message);
            }
        }
        String numberPlate = busAttendance.getNumberPlate();
        //根据userId获取车辆的信息
        Bus bus = busMapper.getBusByWorkerId(userId);
        //根据busid获取路线信息
        if (bus != null) {
            RouteOperation route = routeOperationMapper.getRouteByBusId(numberPlate);
            if (!numberPlate.equals(bus.getNumberPlate())) {
                busAttendance.setReason("换乘司机打卡");
            }
            busAttendance.setUserId(userId);
            busAttendance.setBusId(bus.getId());
            busAttendance.setRouteId(route.getRouteId());
            busAttendance.setTime(LocalDate.now());
        } else {
            String message = "获取不到车辆的信息";
            throw new BusApiException(message);
        }

        busAttendance.setCreateTime(LocalDateTime.now());
        busAttendance.setCreateBy(userId);
        this.save(busAttendance);
    }

    @Override
    @Transactional
    public void createBusAttendanceInPlatform(BusAttendance busAttendance) throws BusApiException {
        Long userId = busAttendance.getUserId();
        BusAttendance buatt = busAttendanceMapper.findAttendanceInDay(userId, busAttendance.getTime());
        if (buatt != null) {
            if (busAttendance.getType().equals(buatt.getType())) {
                String message = "已经进行过该类型的打卡";
                throw new BusApiException(message);
            }
        }
        Bus bus = null;
        String numberPlate = busAttendance.getNumberPlate();
        //根据userId获取车辆的信息
        bus = busMapper.getBusByWorkerId(userId);
        if (bus == null) {
            bus = busMapper.findBusByUserId(userId);
        }
        //根据busid获取路线信息
        if (bus != null) {
            RouteOperation route = routeOperationMapper.getRouteByBusId(numberPlate);
            if (!numberPlate.equals(bus.getNumberPlate())) {
                busAttendance.setReason("换乘司机打卡");
            }
            busAttendance.setUserId(userId);
            busAttendance.setBusId(bus.getId());
            busAttendance.setRouteId(route.getRouteId());
        } else {
            String message = "获取不到车辆的信息";
            throw new BusApiException(message);
        }

        busAttendance.setCreateTime(LocalDateTime.now());
        busAttendance.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(busAttendance);
    }

    @Override
    @Transactional
    public void modifyBusAttendance(BusAttendance busAttendance) {
        busAttendance.setModifyTime(LocalDateTime.now());
        busAttendance.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(busAttendance);
    }

    @Override
    public void deleteBusAttendances(String[] busAttendanceIds) {
        List<Long> list = Stream.of(busAttendanceIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public Map<String, Object> listBusAttendances(BusAttendanceVO busAttendance) {
        Map<String, Object> returnMap = new HashedMap<>();
        int pageNum = getFirstIndex(busAttendance.getPageNum(), busAttendance.getPageSize());
        busAttendance.setPageNum(pageNum);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (busAttendance.getCreateTimeFrom() != null) {
            busAttendance.setTimeFrom(LocalDateTime.parse(busAttendance.getCreateTimeFrom(), df));
        }
        if (busAttendance.getCreateTimeTo() != null) {
            busAttendance.setTimeTo(LocalDateTime.parse(busAttendance.getCreateTimeTo(), df));
        }
        List<BusAttendanceVO> list = busAttendanceMapper.selectListByCondi(busAttendance);
        busAttendance.setPageNum(-1);
        List<BusAttendanceVO> listTotal = busAttendanceMapper.selectListByCondi(busAttendance);
        if (list == null || list.isEmpty()) {
            returnMap.put("rows", Collections.EMPTY_LIST);
            returnMap.put("total", 0);
        } else {
            returnMap.put("rows", list);
            returnMap.put("total", listTotal.size());
        }
        return returnMap;
    }

    public int getFirstIndex(int pageNum, int pageSize) {
        int offset = -1;
        if (pageNum > 0) {
            offset = (pageNum - 1) * pageSize;
        }
        return offset;
    }

    @Override
    public boolean getMyAttendance() {
        boolean flag = false;
        Long userId = BusApiUtil.getCurrentUser().getUserId();
        BusAttendance buatt = busAttendanceMapper.findAttendanceInToday(userId);
        if (buatt != null) {
            if ("1".equals(buatt.getType())) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public List<UserBusVO> findDetalByRoleType(String type) {
        List<UserBusVO> vo = new ArrayList<UserBusVO>();
        if ("1".equals(type)) {//司机绑定的信息
            vo = busMapper.findUserAndBusByDriver();
        } else {//老师绑定的信息
            vo = busMapper.findUserAndBusByTeacher();
        }
        return vo;
    }
}
