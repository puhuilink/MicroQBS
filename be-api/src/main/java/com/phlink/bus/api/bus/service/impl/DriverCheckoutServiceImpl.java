package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.BusMapper;
import com.phlink.bus.api.bus.dao.DriverCheckoutMapper;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DriverCheckout;
import com.phlink.bus.api.bus.service.IDriverCheckoutService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.system.dao.UserMapper;
import com.phlink.bus.api.system.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wen
 */
@Service
public class DriverCheckoutServiceImpl extends ServiceImpl<DriverCheckoutMapper, DriverCheckout> implements IDriverCheckoutService {

    @Autowired
    private DriverCheckoutMapper driverCheckoutMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BusMapper busMapper;

    @Override
    public DriverCheckout findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<DriverCheckout> listDriverCheckouts(QueryRequest request, DriverCheckout driverCheckout) {
        QueryWrapper<DriverCheckout> queryWrapper = new QueryWrapper<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(driverCheckout.getCreateTimeFrom())) {
            queryWrapper.lambda().ge(DriverCheckout::getTime, LocalDateTime.parse(driverCheckout.getCreateTimeFrom(), df));
        }
        if (StringUtils.isNotBlank(driverCheckout.getCreateTimeTo())) {
            queryWrapper.lambda().le(DriverCheckout::getTime, LocalDateTime.parse(driverCheckout.getCreateTimeTo(), df));
        }
        if (driverCheckout.getUserName() != null) {
            queryWrapper.lambda().like(DriverCheckout::getUserName, driverCheckout.getUserName());
        }
        if (driverCheckout.getMobile() != null) {
            queryWrapper.lambda().like(DriverCheckout::getMobile, driverCheckout.getMobile());
        }
        Page<DriverCheckout> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<DriverCheckout> listDriverCheckouts(DriverCheckout driverCheckout) {
        QueryWrapper<DriverCheckout> queryWrapper = new QueryWrapper<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (driverCheckout.getCreateTimeFrom() != null) {
            queryWrapper.lambda().ge(DriverCheckout::getTime, LocalDateTime.parse(driverCheckout.getCreateTimeFrom(), df));
        }
        if (driverCheckout.getCreateTimeTo() != null) {
            queryWrapper.lambda().le(DriverCheckout::getTime, LocalDateTime.parse(driverCheckout.getCreateTimeTo(), df));
        }
        if (driverCheckout.getUserName() != null) {
            queryWrapper.lambda().like(DriverCheckout::getUserName, driverCheckout.getUserName());
        }
        if (driverCheckout.getMobile() != null) {
            queryWrapper.lambda().like(DriverCheckout::getMobile, driverCheckout.getMobile());
        }
        queryWrapper.orderByDesc("id");
        //TODO:查询条件
        Page<DriverCheckout> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        this.page(page, queryWrapper);
        return page.getRecords();
    }

    @Override
    @Transactional
    public void createDriverCheckout(DriverCheckout driverCheckout) {
        driverCheckout.setCreateTime(LocalDateTime.now());
        driverCheckout.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(driverCheckout);
    }

    @Override
    @Transactional
    public void modifyDriverCheckout(DriverCheckout driverCheckout) {
        driverCheckout.setModifyTime(LocalDateTime.now());
        driverCheckout.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(driverCheckout);
    }

    @Override
    public void deleteDriverCheckouts(String[] driverCheckoutIds) {
        List<Long> list = Stream.of(driverCheckoutIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public DriverCheckout findIsCheckout(Long userId) {
        return driverCheckoutMapper.findIsCheckout(userId);
    }

    @Override
    public boolean createDriverCheckoutOnPlatform(DriverCheckout driverCheckout) {
        boolean flag = false;
        //根据参数手机号查询出用户收信息
        User user = userMapper.findDetalByMobile(driverCheckout.getMobile());
        if (user != null) {
            Long userId = user.getUserId();
            //查询改用户改日是否已经晨检过
            DriverCheckout check = driverCheckoutMapper.findIsCheckoutInTime(userId, driverCheckout.getTime());
            if (check != null) {
                return flag;
            }
            //根据userid查询到用户绑定的车辆信息
            Bus bus = busMapper.findBusByUserId(userId);
            if (bus != null) {
                driverCheckout.setNumberPlate(bus.getNumberPlate());
            }
            driverCheckout.setUserId(userId);
            driverCheckout.setUserName(user.getRealname());
            driverCheckout.setCreateTime(LocalDateTime.now());
            driverCheckout.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
            boolean number = this.save(driverCheckout);
            if (number) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public DriverCheckout getByDay(Long userId, LocalDateTime checkTime) {
        LocalDate date = checkTime.toLocalDate();
        return baseMapper.findIsCheckoutInTime(userId, date);
    }

}
