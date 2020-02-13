package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.BusMapper;
import com.phlink.bus.api.bus.dao.TeacherCheckoutMapper;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.TeacherCheckout;
import com.phlink.bus.api.bus.service.ITeacherCheckoutService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.system.dao.UserMapper;
import com.phlink.bus.api.system.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wen
 */
@Service
public class TeacherCheckoutServiceImpl extends ServiceImpl<TeacherCheckoutMapper, TeacherCheckout> implements ITeacherCheckoutService {
    @Autowired
    private TeacherCheckoutMapper teacherCheckoutMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BusMapper busMapper;

    @Override
    public TeacherCheckout findById(Long id) {
        return this.getById(id);
    }


    @Override
    public IPage<TeacherCheckout> listTeacherCheckouts(QueryRequest request, TeacherCheckout teacherCheckout) {
        QueryWrapper<TeacherCheckout> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(teacherCheckout.getCreateTimeFrom())) {
            LocalDate date = Instant.ofEpochMilli(Long.parseLong(teacherCheckout.getCreateTimeFrom())).atZone(ZoneId.systemDefault()).toLocalDate();
            queryWrapper.lambda().ge(TeacherCheckout::getTime, date);
        }
        if (StringUtils.isNotBlank(teacherCheckout.getCreateTimeTo())) {
            LocalDate date = Instant.ofEpochMilli(Long.parseLong(teacherCheckout.getCreateTimeTo())).atZone(ZoneId.systemDefault()).toLocalDate();
            queryWrapper.lambda().le(TeacherCheckout::getTime, date.plusDays(1));
        }
        if (teacherCheckout.getUserName() != null) {
            queryWrapper.lambda().like(TeacherCheckout::getUserName, teacherCheckout.getUserName());
        }
        if (teacherCheckout.getMobile() != null) {
            queryWrapper.lambda().like(TeacherCheckout::getMobile, teacherCheckout.getMobile());
        }
        queryWrapper.orderByDesc("id");
        Page<TeacherCheckout> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<TeacherCheckout> listTeacherCheckouts(TeacherCheckout teacherCheckout) {
        QueryWrapper<TeacherCheckout> queryWrapper = new QueryWrapper<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (teacherCheckout.getCreateTimeFrom() != null) {
            queryWrapper.lambda().ge(TeacherCheckout::getTime, LocalDateTime.parse(teacherCheckout.getCreateTimeFrom(), df));
        }
        if (teacherCheckout.getCreateTimeTo() != null) {
            queryWrapper.lambda().le(TeacherCheckout::getTime, LocalDateTime.parse(teacherCheckout.getCreateTimeTo(), df));
        }
        if (teacherCheckout.getUserName() != null) {
            queryWrapper.lambda().like(TeacherCheckout::getUserName, teacherCheckout.getUserName());
        }
        if (teacherCheckout.getMobile() != null) {
            queryWrapper.lambda().like(TeacherCheckout::getMobile, teacherCheckout.getMobile());
        }
        queryWrapper.orderByDesc("id");
        //TODO:查询条件
        Page<TeacherCheckout> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        this.page(page, queryWrapper);
        return page.getRecords();
    }

    @Override
    @Transactional
    public void createTeacherCheckout(TeacherCheckout teacherCheckout) {
        teacherCheckout.setCreateTime(LocalDateTime.now());
        teacherCheckout.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(teacherCheckout);
    }

    @Override
    @Transactional
    public void modifyTeacherCheckout(TeacherCheckout teacherCheckout) {
        teacherCheckout.setModifyTime(LocalDateTime.now());
        teacherCheckout.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(teacherCheckout);
    }

    @Override
    public void deleteTeacherCheckouts(String[] teacherCheckoutIds) {
        List<Long> list = Stream.of(teacherCheckoutIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public TeacherCheckout findIsCheckout(Long userId) {
        return teacherCheckoutMapper.findIsCheckout(userId);
    }


    @Override
    public boolean createTeacherCheckoutOnPlatform(TeacherCheckout teacherCheckout) {
        boolean flag = false;
        //根据参数手机号查询出用户收信息
        User user = userMapper.findDetalByMobile(teacherCheckout.getMobile());
        if (user != null) {
            Long userId = user.getUserId();
            //查询改用户改日是否已经晨检过
            TeacherCheckout check = teacherCheckoutMapper.findIsCheckoutInTime(userId, teacherCheckout.getTime());
            if (check != null) {
                return flag;
            }
            //根据userid查询到用户绑定的车辆信息
            Bus bus = busMapper.findBusByUserId(userId);
            teacherCheckout.setNumberPlate(bus.getNumberPlate());
            teacherCheckout.setUserId(userId);
            teacherCheckout.setUserName(user.getRealname());
            teacherCheckout.setCreateTime(LocalDateTime.now());
            teacherCheckout.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
            boolean number = this.save(teacherCheckout);
            if (number) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public TeacherCheckout getByDay(Long userId, LocalDateTime checkTime) {
        LocalDate date = checkTime.toLocalDate();
        return baseMapper.findIsCheckoutInTime(userId, date);
    }
}
