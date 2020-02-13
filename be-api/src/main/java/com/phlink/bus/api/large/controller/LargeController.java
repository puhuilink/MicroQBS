package com.phlink.bus.api.large.controller;

import com.phlink.bus.api.alarm.domain.AlarmBus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.lisenter.service.CacheDeviceInfoService;
import com.phlink.bus.api.device.domain.VO.BusDetailLocationVO;
import com.phlink.bus.api.large.domain.AlarmList;
import com.phlink.bus.api.large.domain.BusAttendanceTime;
import com.phlink.bus.api.large.domain.LineList;
import com.phlink.bus.api.large.domain.NumberCountYear;
import com.phlink.bus.api.large.service.LargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/8$ 12:36$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/8$ 12:36$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/large")
@Api(tags = ApiTagsConstant.TAG_ALARM_BUS)
public class LargeController {
    @Autowired
    private LargeService largeService;
    @Autowired
    private CacheDeviceInfoService cacheDeviceInfoService;
    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/AlarmList")
//    //@RequiresPermissions("large:view")
    @ApiOperation(value = "大屏告警列表", notes = "大屏告警列表", tags = ApiTagsConstant.TAG_ALARM_BUS, httpMethod = "GET")
    public List<AlarmList> listBanner(QueryRequest request, AlarmBus alarmBus) {
        return largeService.getAlarmBus();
    }

    @GetMapping("/AttendanceList")
    //@RequiresPermissions("large:view")
    @ApiOperation(value = "车辆统计", notes = "车辆统计", tags = ApiTagsConstant.TAG_ALARM_BUS, httpMethod = "GET")
    public List<BusAttendanceTime> listBanner(QueryRequest request, BusAttendanceTime alarmBus) {
        return largeService.getBusAttendance();
    }

    @GetMapping("/NumberConut")
    //@RequiresPermissions("large:view")
    @ApiOperation(value = "应到实到人数", notes = "应到实到人数", tags = ApiTagsConstant.TAG_ALARM_BUS, httpMethod = "GET")
    public List<NumberCountYear> NumberConut() {
        return largeService.NumberConut();
    }
    @GetMapping("/LineList")
    //@RequiresPermissions("large:view")
    @ApiOperation(value = "线路列表查询", notes = "线路列表查询", tags = ApiTagsConstant.TAG_ALARM_BUS, httpMethod = "GET")
    public List<LineList> LineList() throws BusApiException {
        return largeService.LineList();
    }

    @GetMapping("/LineById/{busId}")
    //@RequiresPermissions("large:view")
    @ApiOperation(value = "查询某车的线路列表", notes = "查询某车的线路列表", tags = ApiTagsConstant.TAG_ALARM_BUS, httpMethod = "GET")
    public List<LineList> LineById(@PathVariable Long busId) throws BusApiException {
        return largeService.LineById(busId);
    }

    @GetMapping("/DvrOrBusList")
    //@RequiresPermissions("large:view")
    @ApiOperation(value = "所有车辆定位", notes = "所有车辆定位", tags = ApiTagsConstant.TAG_ALARM_BUS, httpMethod = "GET")
    public List<BusDetailLocationVO> DvrOrBusList(){
        //先去查出车辆详细的信息
        List<BusDetailLocationVO> busDetailLocationVOS = largeService.ListBusDetailLocationVO();
        //查出最近的定位信息
        List<BusDetailLocationVO> collect = busDetailLocationVOS.stream().peek(b -> {
            DvrLocation dvrLocation = cacheDeviceInfoService.getLastDvr(b.getDvrCode());
            b.setLocation(dvrLocation);
        }).collect(Collectors.toList());
        return collect;
    }

}