package com.phlink.bus.api.bus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.alarm.domain.BusTripTime;
import com.phlink.bus.api.bus.domain.*;
import com.phlink.bus.api.bus.domain.VO.BusViewVO;
import com.phlink.bus.api.bus.domain.VO.SchoolBusListVO;
import com.phlink.bus.api.bus.domain.VO.SchoolRouteBusListVO;
import com.phlink.bus.api.bus.domain.VO.UserBusVO;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.device.domain.VO.BusDetailLocationVO;
import com.phlink.bus.api.map.domain.enums.MapTypeEnum;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.system.domain.User;

import java.util.List;

/**
 * @author wen
 */
public interface IBusService extends IService<Bus> {

    List<Bus> listBus(BusViewVO busViewVO);

    /**
     * 获取详情
     */
    Bus findById(Long id);

    /**
     * 查询列表
     *
     * @param request
     * @param busViewVO
     * @return
     */
    Page<Bus> listBus(QueryRequest request, BusViewVO busViewVO);

    /**
     * 新增
     *
     * @param bus
     */
    void createBus(Bus bus) throws BusApiException;

    /**
     * 修改
     *
     * @param bus
     */
    void modifyBus(Bus bus) throws BusApiException;

    /**
     * 批量删除
     *
     * @param busIds
     */
    void deleteBusIds(String[] busIds) throws BusApiException, RedisConnectException;

//    /**
//     * 创建百度地图设备id
//     *
//     * @param busCode     车架号
//     * @param numberPlate 车牌号
//     */
//    boolean createBaiduEntity(String numberPlate, String busCode) throws BusApiException;
//
//    /**
//     * 创建高德地图设备id
//     *
//     * @param busCode     车架号
//     * @param numberPlate 车牌号
//     * @return
//     */
//    long createAmapEntity(String numberPlate, String busCode) throws BusApiException;

    /**
     * 根据busCode更新
     */
    void updateBusTerminalId(String busCode, MapTypeEnum mapTypeEnum, long terminalId) throws BusApiException;

    /**
     * 未绑定路线的车辆
     *
     * @param bus
     * @return
     */
    List<Bus> unbindBusList(Bus bus);

    /**
     * 未绑定的司机
     *
     * @param driver
     * @return
     */
    List<User> unbindDriverList(Driver driver);

    /**
     * 未绑定的随车老师
     *
     * @param busTeacher
     * @return
     */
    List<User> unbindBusTeacherList(BusTeacher busTeacher);

    /**
     * 批量保存
     *
     * @param list
     */
    void batchCreateBus(List<Bus> list);

    /**
     * 获得已绑定的学生列表
     *
     * @param routeOperationId
     * @param stopName
     * @param studentName
     * @return
     */
    List<Student> listBindStudent(Long routeOperationId, String stopName, String studentName);

    /**
     * 车牌号唯一验证
     *
     * @param numberPlate
     * @return
     */
    boolean checkNumberPlate(String numberPlate);

    /**
     * 根据学校分组获得学校下的车辆
     *
     * @param numberPlate
     * @return
     */
    List<SchoolBusListVO> listSchoolBus(String numberPlate);
    /**
     * 根据学校分组获得学校下的车辆
     *
     * @param numberPlate
     * @return
     */
    List<SchoolRouteBusListVO> listSchoolRouteBus(String numberPlate, String busCode);
    
    UserBusVO getUserAndBus(String mobile);

    /**
     * 根据绑定的老师获得车辆信息
     * @param busTeacherId
     * @return
     */
    Bus getBusByWorkerId(Long busTeacherId);

    /**
     * 根据buscode获取校车
     * @param busCode
     * @return
     */
    Bus getByBusCode(String busCode);

    /**
     * 获取没有在高德上注册的车辆
     * @return
     */
    List<Bus> listNoRegisterToGaode();

    /**
     * 批量注册车辆到高德
     * @param buses
     */
    void batchRegisterToGaode(List<Bus> buses);

    void batchRegisterToBaidu(List<Bus> buses);

    /**
     * 根据ID获取车辆详情，包括司机和随车老师的信息
     * @param busIdArray
     * @return
     */
    List<BusDetailLocationVO> listDetailByIds(String[] busIdArray);

    List<BusTripTime> listBusTripTime(Long busId);

    List<BindBusDetailInfo> listBindBusDetailInfo();

    BindBusDetailInfo getBindBusDetailInfoByBusCode(String busCode);

    BindBusDetailInfo getBindBusDetailInfoByWorkerId(Long userId);

    List<String> findEntitysByRouteId(Long routeId);

    List<BindBusStoptimeDetailInfo> listBusStopTimeDetailInfos();
}
