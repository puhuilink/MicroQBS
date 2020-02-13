package com.phlink.bus.api.attendance.service;

import com.phlink.bus.api.attendance.domain.BusAttendance;
import com.phlink.bus.api.attendance.domain.vo.BusAttendanceVO;
import com.phlink.bus.api.bus.domain.VO.UserBusVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
/**
 * @author ZHOUY
 */
public interface IBusAttendanceService extends IService<BusAttendance> {


    /**
    * 获取详情
    */
    BusAttendance findById(Long id);
    
    /**
    * 获取用户和车辆详情
    */
    List<UserBusVO> findDetalByRoleType(String type);

    /**
    * 查询列表
    * @param request
    * @param busAttendance
    * @return
    */
    IPage<BusAttendance> listBusAttendances(QueryRequest request, BusAttendance busAttendance);
    
    /**
     * 查询列表
     * @param busAttendance
     * @return
     */
    Map<String, Object> listBusAttendances(BusAttendanceVO busAttendance);

    List<BusAttendance> listBusAttendances(BusAttendance busAttendance);

    /**
    * 新增
    * @param busAttendance
     * @throws BusApiException 
    */
    void createBusAttendance(BusAttendance busAttendance) throws BusApiException;
    void createBusAttendanceInPlatform(BusAttendance busAttendance) throws BusApiException;
    /**
    * 修改
    * @param busAttendance
    */
    void modifyBusAttendance(BusAttendance busAttendance);

    /**
    * 批量删除
    * @param busAttendanceIds
    */
    void deleteBusAttendances(String[] busAttendanceIds);
    
    /**
     * 获取当前登录用户的今天最近的行程
     * @return
     */
    boolean getMyAttendance();
    
}
