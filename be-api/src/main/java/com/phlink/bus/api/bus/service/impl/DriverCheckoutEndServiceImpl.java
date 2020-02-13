package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.DriverCheckoutEndMapper;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DriverCheckoutEnd;
import com.phlink.bus.api.bus.domain.Dvr;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.bus.service.IDriverCheckoutEndService;
import com.phlink.bus.api.bus.service.IDvrService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.route.domain.RouteOperation;
import com.phlink.bus.api.route.service.IRouteOperationService;
import com.phlink.bus.api.system.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author wen
*/
@Service
public class DriverCheckoutEndServiceImpl extends ServiceImpl<DriverCheckoutEndMapper, DriverCheckoutEnd> implements IDriverCheckoutEndService {

    @Autowired
    private IRouteOperationService routeOperationService;
    @Autowired
    private IBusService busService;
    @Autowired
    private IDvrService dvrService;

    @Override
    public DriverCheckoutEnd findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<DriverCheckoutEnd> listDriverCheckoutEnds(QueryRequest request, String realname, String dateStart, String dateEnd, String mobile){
        LambdaQueryWrapper<DriverCheckoutEnd> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(realname)) {
            queryWrapper.like(DriverCheckoutEnd::getRealname, realname);
        }
        if (StringUtils.isNotBlank(mobile)) {
            queryWrapper.like(DriverCheckoutEnd::getMobile, mobile);
        }
        LocalDateTime endDay = DateUtil.formatDateTimeStr(dateEnd);
        LocalDateTime startDay = DateUtil.formatDateTimeStr(dateStart);
        if(startDay != null) {
            queryWrapper
                    .ge(DriverCheckoutEnd::getCheckDate, startDay.toLocalDate());
        }
        if(endDay != null) {
            queryWrapper
                    .le(DriverCheckoutEnd::getCheckDate, endDay.toLocalDate().plusDays(1));
        }
        Page<DriverCheckoutEnd> page = new Page<>();
        if(request != null) {
            page.setCurrent(request.getPageNum());
            page.setSize(request.getPageSize());
            String sort= StringUtils.isNotBlank(request.getSortOrder())?request.getSortOrder():"id";
            String order=StringUtils.isNotBlank(request.getSortField())?request.getSortField():BusApiConstant.ORDER_DESC;
            SortUtil.handlePageSort(request, page, sort, order, true);
        }else{
            page.setSize(-1);
            page.setSearchCount(false);
            page.setDesc("id");
        }
        this.page(page, queryWrapper);
        return page;
    }

    @Override
    @Transactional
    public void createDriverCheckoutEnd(DriverCheckoutEnd driverCheckoutEnd, User user) {
        RouteOperation routeOperation = routeOperationService.getByWorkerId(user.getUserId());
        if(routeOperation != null) {
            Bus bus = busService.findById(routeOperation.getBindBusId());
            if(bus != null) {
                driverCheckoutEnd.setBusId(bus.getId());
                driverCheckoutEnd.setBusCode(bus.getBusCode());
                driverCheckoutEnd.setNumberPlate(bus.getNumberPlate());

                Dvr dvr = dvrService.getByBusId(bus.getId());
                if(dvr != null) {
                    driverCheckoutEnd.setDvrCode(dvr.getDvrCode());
                }
            }
        }
        driverCheckoutEnd.setUserId(user.getUserId());
        driverCheckoutEnd.setRealname(user.getRealname());
        driverCheckoutEnd.setMobile(user.getMobile());
        if(driverCheckoutEnd.getCheckTime() == null) {
            driverCheckoutEnd.setCheckTime(LocalDateTime.now());
            driverCheckoutEnd.setCheckDate(LocalDate.now());
        }else{
            driverCheckoutEnd.setCheckTime(driverCheckoutEnd.getCheckTime());
            driverCheckoutEnd.setCheckDate(driverCheckoutEnd.getCheckDate());
        }
        this.save(driverCheckoutEnd);
    }

    @Override
    @Transactional
    public void modifyDriverCheckoutEnd(DriverCheckoutEnd driverCheckoutEnd) {
        this.updateById(driverCheckoutEnd);
    }

    @Override
    public void deleteDriverCheckoutEnds(String[] driverCheckoutEndIds) {
        List<Long> list = Stream.of(driverCheckoutEndIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public DriverCheckoutEnd getByDay(Long userId, LocalDateTime checkTime) {

        LocalDate day = checkTime.toLocalDate();
        return baseMapper.getCheckoutByDay(userId, day);

    }
}
