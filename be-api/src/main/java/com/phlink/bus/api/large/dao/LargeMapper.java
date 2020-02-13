package com.phlink.bus.api.large.dao;

import com.phlink.bus.api.device.domain.VO.BusDetailLocationVO;
import com.phlink.bus.api.large.domain.AlarmList;
import com.phlink.bus.api.large.domain.BusAttendanceTime;
import com.phlink.bus.api.large.domain.LineList;
import com.phlink.bus.api.large.domain.NumberCountYear;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/8$ 12:53$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/8$ 12:53$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public interface LargeMapper{

    List<AlarmList> getAlarmBus();

    List<BusAttendanceTime> getBusAttendance();

    List<NumberCountYear> NumberConut();

    List<LineList> LineList();

    List<BusDetailLocationVO> ListBusDetailLocationVO();

    List<LineList> LineById(Long busId);
}
