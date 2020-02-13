package com.phlink.bus.api.bus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.alarm.domain.BusTripTime;
import com.phlink.bus.api.bus.domain.*;
import com.phlink.bus.api.bus.domain.VO.BusViewVO;
import com.phlink.bus.api.bus.domain.VO.SchoolBusListVO;
import com.phlink.bus.api.bus.domain.VO.SchoolRouteBusListVO;
import com.phlink.bus.api.bus.domain.VO.UserBusVO;
import com.phlink.bus.api.device.domain.VO.BusDetailLocationVO;
import com.phlink.bus.api.system.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wen
 */
public interface BusMapper extends BaseMapper<Bus> {

    void updateBusAmapTerminalId(String busCode, long terminalId);

    void updateBusBaiduTerminalId(String busCode);

    Page<Bus> listBus(Page page, @Param("busViewVO") BusViewVO busViewVO);

    List<Bus> unbindRouteBusList(@Param("entity") Bus bus);

    List<User> unbindRouteDriverList(@Param("entity") Driver driver, @Param("roleID") Long roleDriver);

    List<User> unbindRouteBusTeacherList(@Param("entity") BusTeacher busTeacher, @Param("roleID") Long roleBusTeacher);

    int checkNumberPlate(@Param("numberPlate") String numberPlate);

    List<String> findEntitysByids(@Param("routeId") Long routeId);

    Bus findBusByUserId(@Param("userId") Long userId);

    Bus getBusByWorkerId(@Param("userId") Long userId);

    List<SchoolBusListVO> listSchoolBus(@Param("numberPlate") String numberPlate);

    UserBusVO findUserAndBusByMobile(@Param("mobile") String mobile);

    List<SchoolRouteBusListVO> listSchoolRouteBus(@Param("numberPlate") String numberPlate, @Param("busCode") String busCode);

    List<UserBusVO> findUserAndBusByTeacher();

    List<UserBusVO> findUserAndBusByDriver();

    List<BusDetailLocationVO> listDetailByIds(@Param("busIds") String[] busIds);

    List<BusTripTime> listBusTripTime(@Param("busId") Long busId);

    List<BindBusDetailInfo> listBindBusDetailInfo(@Param("busCode") String busCode);

    BindBusDetailInfo getBindBusDetailInfo(@Param("busCode") String busCode, @Param("workerId") Long workerId);

    List<BindBusStoptimeDetailInfo> listBusStopTimeDetailInfos();
}
