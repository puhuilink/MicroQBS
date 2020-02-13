package com.phlink.bus.api.bus.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.bus.dao.CheckoutMapper;
import com.phlink.bus.api.bus.domain.*;
import com.phlink.bus.api.bus.domain.VO.CheckoutSpecialVO;
import com.phlink.bus.api.bus.service.*;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.manager.UserManager;
import com.phlink.bus.api.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;


@Component
public class CheckoutSpecialManager {

    @Autowired
    private CheckoutMapper checkoutMapper;
    @Autowired
    private IDriverCheckoutService driverCheckoutService;
    @Autowired
    private IDriverCheckoutEndService driverCheckoutEndService;
    @Autowired
    private ITeacherCheckoutService teacherCheckoutService;
    @Autowired
    private ITeacherCheckoutEndService teacherCheckoutEndService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private IBusService busService;

    public IPage<CheckoutSpecialVO> listMorning(QueryRequest request, CheckoutSpecialVO checkoutSpecialVO) {
        Page<CheckoutSpecialVO> page = new Page<>(request.getPageNum(), request.getPageSize());

        checkoutMapper.listMorning(page, checkoutSpecialVO);
        return page;
    }


    public IPage<CheckoutSpecialVO> listEvening(QueryRequest request, CheckoutSpecialVO checkoutSpecialVO) {
        Page<CheckoutSpecialVO> page = new Page<>(request.getPageNum(), request.getPageSize());

        checkoutMapper.listEvening(page, checkoutSpecialVO);
        return page;
    }

    public void addMorningCheckout(CheckoutSpecialVO vo) throws BusApiException {
        User user = userService.findByMobile(vo.getMobile());
        if(user == null) {
            throw new BusApiException("该手机号用户不存在");
        }
        Set<String> userRoles = userManager.getUserRoles(user.getUsername());
        if(StringUtils.isBlank(vo.getRolename())) {
            if(userRoles.contains("司机")) {
                createDriverSpecialCheckout(vo, user);
            }else if(userRoles.contains("随车老师")) {
                createTeacherSpecialCheckout(vo, user);
            }
        }else{
            // 添加到对应的日期
            if("司机".equals(vo.getRolename())) {
                // 添加到司机表
                createDriverSpecialCheckout(vo, user);
            }
            if("随车老师".equals(vo.getRolename())) {
                // 添加到老师表
                createTeacherSpecialCheckout(vo, user);
            }
        }
    }

    public void addEveningCheckout(CheckoutSpecialVO vo) throws BusApiException {
        User user = userService.findByMobile(vo.getMobile());
        if(user == null) {
            throw new BusApiException("该手机号用户不存在");
        }
        Set<String> userRoles = userManager.getUserRoles(user.getUsername());
        if(StringUtils.isBlank(vo.getRolename())) {
            if(userRoles.contains("司机")) {
                createDriverSpecialCheckoutEnd(vo, user);
            }else if(userRoles.contains("随车老师")) {
                createTeacherSpecialCheckoutEnd(vo, user);
            }
        }else{
            // 添加到对应的日期
            if("司机".equals(vo.getRolename())) {
                // 添加到司机表
                createDriverSpecialCheckoutEnd(vo, user);
            }
            if("随车老师".equals(vo.getRolename())) {
                // 添加到老师表
                createTeacherSpecialCheckoutEnd(vo, user);
            }
        }
    }

    public void createTeacherSpecialCheckout(CheckoutSpecialVO vo, User user) {
        TeacherCheckout checkout = teacherCheckoutService.getByDay(user.getUserId(), vo.getCheckTime());
        if(checkout == null) {
            checkout = new TeacherCheckout();
            checkout.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
            checkout.setCreateTime(LocalDateTime.now());
            checkout.setTime(vo.getCheckTime().toLocalDate());
            checkout.setUserName(user.getRealname());
            Bus bus = busService.getBusByWorkerId(user.getUserId());
            if(bus != null) {
                checkout.setNumberPlate(bus.getNumberPlate());
            }
        }
        checkout.setMobile(user.getMobile());
        checkout.setUserId(user.getUserId());
        checkout.setTime(vo.getCheckTime().toLocalDate());
        checkout.setDescription(vo.getDescription());
        checkout.setImagePath(vo.getImagePath());
        checkout.setVideoPath(vo.getVideoPath());
        teacherCheckoutService.saveOrUpdate(checkout);
    }

    public void createDriverSpecialCheckout(CheckoutSpecialVO vo, User user) {
        DriverCheckout driverCheckout = driverCheckoutService.getByDay(user.getUserId(), vo.getCheckTime());
        if(driverCheckout == null) {
            driverCheckout = new DriverCheckout();
            driverCheckout.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
            driverCheckout.setCreateTime(LocalDateTime.now());
            driverCheckout.setTime(vo.getCheckTime().toLocalDate());
            driverCheckout.setUserName(user.getRealname());
            Bus bus = busService.getBusByWorkerId(user.getUserId());
            if(bus != null) {
                driverCheckout.setNumberPlate(bus.getNumberPlate());
            }
        }
        driverCheckout.setMobile(user.getMobile());
        driverCheckout.setUserId(user.getUserId());
        driverCheckout.setTime(vo.getCheckTime().toLocalDate());
        driverCheckout.setDescription(vo.getDescription());
        driverCheckout.setImagePath(vo.getImagePath());
        driverCheckout.setVideoPath(vo.getVideoPath());
        driverCheckoutService.saveOrUpdate(driverCheckout);
    }

    public void createTeacherSpecialCheckoutEnd(CheckoutSpecialVO vo, User user) {
        TeacherCheckoutEnd checkoutEnd = teacherCheckoutEndService.getByDay(user.getUserId(), vo.getCheckTime());
        if(checkoutEnd == null) {
            checkoutEnd = new TeacherCheckoutEnd();
            checkoutEnd.setUserId(BusApiUtil.getCurrentUser().getUserId());
            checkoutEnd.setCheckTime(vo.getCheckTime());
            checkoutEnd.setRealname(user.getRealname());
            Bus bus = busService.getBusByWorkerId(user.getUserId());
            if(bus != null) {
                checkoutEnd.setBusId(bus.getId());
                checkoutEnd.setBusCode(bus.getBusCode());
                checkoutEnd.setNumberPlate(bus.getNumberPlate());
            }
        }
        checkoutEnd.setMobile(user.getMobile());
        checkoutEnd.setUserId(user.getUserId());
        checkoutEnd.setCheckDate(vo.getCheckTime().toLocalDate());
        checkoutEnd.setDescription(vo.getDescription());
        checkoutEnd.setImagePath(vo.getImagePath());
        checkoutEnd.setVideoPath(vo.getVideoPath());
        teacherCheckoutEndService.saveOrUpdate(checkoutEnd);
    }

    public void createDriverSpecialCheckoutEnd(CheckoutSpecialVO vo, User user) {
        DriverCheckoutEnd checkoutEnd = driverCheckoutEndService.getByDay(user.getUserId(), vo.getCheckTime());
        if(checkoutEnd == null) {
            checkoutEnd = new DriverCheckoutEnd();
            checkoutEnd.setUserId(BusApiUtil.getCurrentUser().getUserId());
            checkoutEnd.setCheckTime(vo.getCheckTime());
            checkoutEnd.setRealname(user.getRealname());
            Bus bus = busService.getBusByWorkerId(user.getUserId());
            if(bus != null) {
                checkoutEnd.setBusId(bus.getId());
                checkoutEnd.setBusCode(bus.getBusCode());
                checkoutEnd.setNumberPlate(bus.getNumberPlate());
            }
        }
        checkoutEnd.setMobile(user.getMobile());
        checkoutEnd.setUserId(user.getUserId());
        checkoutEnd.setCheckDate(vo.getCheckTime().toLocalDate());
        checkoutEnd.setDescription(vo.getDescription());
        checkoutEnd.setImagePath(vo.getImagePath());
        checkoutEnd.setVideoPath(vo.getVideoPath());
        driverCheckoutEndService.saveOrUpdate(checkoutEnd);
    }
}
