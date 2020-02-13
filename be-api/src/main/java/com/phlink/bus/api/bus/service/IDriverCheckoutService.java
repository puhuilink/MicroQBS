package com.phlink.bus.api.bus.service;

import com.phlink.bus.api.bus.domain.DriverCheckout;
import com.phlink.bus.api.bus.domain.TeacherCheckout;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wen
 */
public interface IDriverCheckoutService extends IService<DriverCheckout> {


    /**
    * 获取详情
    */
    DriverCheckout findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param driverCheckout
    * @return
    */
    IPage<DriverCheckout> listDriverCheckouts(QueryRequest request, DriverCheckout driverCheckout);

    List<DriverCheckout> listDriverCheckouts(DriverCheckout driverCheckout);

    /**
    * 新增
    * @param driverCheckout
    */
    void createDriverCheckout(DriverCheckout driverCheckout);

    /**
    * 修改
    * @param driverCheckout
    */
    void modifyDriverCheckout(DriverCheckout driverCheckout);

    /**
    * 批量删除
    * @param driverCheckoutIds
    */
    void deleteDriverCheckouts(String[] driverCheckoutIds);
    
    /**
     * 查看当前用户今天是否已经进行过晨检
     * @param userId
     * @return
     */
    DriverCheckout findIsCheckout(Long userId);

	boolean createDriverCheckoutOnPlatform(DriverCheckout driverCheckout);

    /**
     * 根据时间查询
     * @param userId
     * @param checkTime
     * @return
     */
    DriverCheckout getByDay(Long userId, LocalDateTime checkTime);
}
