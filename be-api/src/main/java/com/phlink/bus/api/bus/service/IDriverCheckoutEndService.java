package com.phlink.bus.api.bus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.bus.domain.DriverCheckoutEnd;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.system.domain.User;

import java.time.LocalDateTime;

/**
 * @author wen
 */
public interface IDriverCheckoutEndService extends IService<DriverCheckoutEnd> {


    /**
    * 获取详情
    */
    DriverCheckoutEnd findById(Long id);

    /**
     * 查询列表
     * @param request
     * @param realname
     * @param dateStart
     * @param dateEnd
     * @param mobile
     * @return
     */
    IPage<DriverCheckoutEnd> listDriverCheckoutEnds(QueryRequest request, String realname, String dateStart, String dateEnd, String mobile);
    /**
    * 新增
    * @param driverCheckoutEnd
    */
    void createDriverCheckoutEnd(DriverCheckoutEnd driverCheckoutEnd, User user);

    /**
    * 修改
    * @param driverCheckoutEnd
    */
    void modifyDriverCheckoutEnd(DriverCheckoutEnd driverCheckoutEnd);

    /**
    * 批量删除
    * @param driverCheckoutEndIds
    */
    void deleteDriverCheckoutEnds(String[] driverCheckoutEndIds);

    DriverCheckoutEnd getByDay(Long userId, LocalDateTime checkTime);
}
