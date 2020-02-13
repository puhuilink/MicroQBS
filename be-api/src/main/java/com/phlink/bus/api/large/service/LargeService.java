package com.phlink.bus.api.large.service;

import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.device.domain.VO.BusDetailLocationVO;
import com.phlink.bus.api.large.domain.AlarmList;
import com.phlink.bus.api.large.domain.BusAttendanceTime;
import com.phlink.bus.api.large.domain.LineList;
import com.phlink.bus.api.large.domain.NumberCountYear;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/8$ 12:51$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/8$ 12:51$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public interface LargeService {
    List<AlarmList> getAlarmBus();

    List<BusAttendanceTime> getBusAttendance();

    List<NumberCountYear> NumberConut();

    List<LineList> LineList() throws BusApiException;


    List<BusDetailLocationVO> ListBusDetailLocationVO();

    List<LineList> LineById(Long busId) throws BusApiException;
}
