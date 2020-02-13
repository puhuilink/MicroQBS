package com.phlink.bus.api.large.service.Impl;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.lisenter.service.CacheDeviceInfoService;
import com.phlink.bus.api.device.domain.VO.BusDetailLocationVO;
import com.phlink.bus.api.large.dao.LargeMapper;
import com.phlink.bus.api.large.domain.AlarmList;
import com.phlink.bus.api.large.domain.BusAttendanceTime;
import com.phlink.bus.api.large.domain.LineList;
import com.phlink.bus.api.large.domain.NumberCountYear;
import com.phlink.bus.api.large.service.LargeService;
import com.phlink.bus.api.route.domain.TripStopTimeDetailVO;
import com.phlink.bus.api.route.service.impl.TripServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/8$ 12:51$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/8$ 12:51$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Service
public class LargeServiceImpl implements LargeService {

    @Autowired
    private LargeMapper largeMapper;
    @Autowired
    private TripServiceImpl trajectoryService;
    @Autowired
    private CacheDeviceInfoService cacheDeviceInfoService;
    @Autowired
    private IBusService busService;

    @Override
    public List<AlarmList> getAlarmBus() {
        return largeMapper.getAlarmBus();
    }

    @Override
    public List<BusAttendanceTime> getBusAttendance() {
        return largeMapper.getBusAttendance();
    }

    @Override
    public List<NumberCountYear> NumberConut() {
        return largeMapper.NumberConut();
    }

    @Override
    public List<LineList> LineList() throws BusApiException {
        String ks;
        String js;
        List<LineList> lineLists = largeMapper.LineList();
        List<LineList> lineListArratList = new ArrayList<LineList>();
        for (int i = 0; i < lineLists.size(); i++) {
            LineList lineList = lineLists.get(i);
            if (lineList.getDirectionId() == -1) {
                LineList lineList1 = lineLists.get(i);
                ks = lineList.getStandStop();
                js = lineList.getEndStop();
                lineList.setStandStop(js);
                lineList.setEndStop(ks);
                Bus busByWorkerId = busService.getBusByWorkerId(lineList.getUserId());
                if (!StringUtils.isEmpty(busByWorkerId)) {
                    TripStopTimeDetailVO tripStopTimeDetailVO = trajectoryService.buildTripStopTimeDetailVOIsNull(busByWorkerId);
                    if(tripStopTimeDetailVO!=null){
                        lineList.setStopName(tripStopTimeDetailVO.getStopName());
                    }else if(tripStopTimeDetailVO==null){
                        lineList.setStopName(null);
                    }
                    lineListArratList.add(lineList);
                }
                lineListArratList.add(lineList);
            }
            for (int j = 0; j < lineLists.size(); j++) {
                LineList lineList1 = lineLists.get(i);
                Bus busByWorkerId = busService.getBusByWorkerId(lineList1.getUserId());
                if (!StringUtils.isEmpty(busByWorkerId)) {
                    TripStopTimeDetailVO tripStopTimeDetailVO = trajectoryService.buildTripStopTimeDetailVOIsNull(busByWorkerId);
                    if(tripStopTimeDetailVO!=null){
                        lineList.setStopName(tripStopTimeDetailVO.getStopName());
                    }else if(tripStopTimeDetailVO==null){
                        lineList.setStopName("");
                    }
                    lineListArratList.add(lineList);
                }
                lineListArratList.add(lineList);
            }
        }
        return lineListArratList.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<BusDetailLocationVO> ListBusDetailLocationVO() {
        return largeMapper.ListBusDetailLocationVO();
    }

    @Override
    public List<LineList> LineById(Long busId) throws BusApiException {
        String ks;
        String js;
        List<LineList> lineLists = largeMapper.LineById(busId);
        List<LineList> lineListArratList = new ArrayList<LineList>();
        for (int i = 0; i < lineLists.size(); i++) {
            LineList lineList = lineLists.get(i);
            if (lineList.getDirectionId() == -1) {
                ks = lineList.getStandStop();
                js = lineList.getEndStop();
                lineList.setStandStop(js);
                lineList.setEndStop(ks);
                LineList lineList1 = lineLists.get(i);
                Bus busByWorkerId = busService.getBusByWorkerId(lineList1.getUserId());
                if (!StringUtils.isEmpty(busByWorkerId)) {
                    TripStopTimeDetailVO tripStopTimeDetailVO = trajectoryService.buildTripStopTimeDetailVOIsNull(busByWorkerId);
                    if(tripStopTimeDetailVO!=null){
                        lineList.setStopName(tripStopTimeDetailVO.getStopName());
                    }else if(tripStopTimeDetailVO==null){
                        lineList.setStopName("");
                    }
                    lineListArratList.add(lineList);
                }
                lineListArratList.add(lineList);
            }
            for (int j = 0; j < lineLists.size(); j++) {
                LineList lineList1 = lineLists.get(i);
                Bus busByWorkerId = busService.getBusByWorkerId(lineList1.getUserId());
                if (!StringUtils.isEmpty(busByWorkerId)) {
                    TripStopTimeDetailVO tripStopTimeDetailVO = trajectoryService.buildTripStopTimeDetailVOIsNull(busByWorkerId);
                    if(tripStopTimeDetailVO!=null){
                        lineList.setStopName(tripStopTimeDetailVO.getStopName());
                    }else if(tripStopTimeDetailVO==null){
                        lineList.setStopName("");
                    }
                    lineListArratList.add(lineList);
                }
                lineListArratList.add(lineList);
            }
        }
        return lineListArratList.stream().distinct().collect(Collectors.toList());
    }

}
