package com.phlink.bus.api.device.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.Dvr;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.bus.service.IDvrLocationService;
import com.phlink.bus.api.bus.service.IDvrService;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.lisenter.service.CacheDeviceInfoService;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.device.domain.EwatchLocation;
import com.phlink.bus.api.device.domain.VO.BusDetailLocationVO;
import com.phlink.bus.api.device.domain.VO.BusLocationInfoVO;
import com.phlink.bus.api.device.domain.VO.BusLocationListVO;
import com.phlink.bus.api.device.service.IDeviceService;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.route.service.IStopService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import com.phlink.bus.api.trajectory.service.ITrajectoryService;
import io.rpc.core.device.EWatchInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/location")
@Api(tags = ApiTagsConstant.TAG_LOCATION)
@Validated
public class LocationController {

    @Autowired
    private IStudentService studentService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IDvrService dvrService;
    @Autowired
    private CacheDeviceInfoService cacheDeviceInfoService;
    @Autowired
    private IBusService busService;
    @Autowired
    private IStopService stopService;
    @Autowired
    private ITrajectoryService trajectoryService;
    @Autowired
    private IDvrLocationService dvrLocationService;

    @GetMapping("/{studentId}/device")
//    //@RequiresPermissions("device:view")
    @ApiOperation(value = "获得学生设备定位信息", notes = "获得学生设备定位信息", tags = ApiTagsConstant.TAG_LOCATION, httpMethod = "GET")
    public EwatchLocation deviceLocation(@PathVariable Long studentId) throws BusApiException {
        Device device = deviceService.getByStudentId(studentId);
        if(device == null) {
            throw new BusApiException("该学生未绑定手环");
        }

        EWatchInfo eWatchInfo = cacheDeviceInfoService.getLastEWatch(device.getDeviceCode());
        return new EwatchLocation(eWatchInfo);
    }

    @GetMapping("/device/search")
//    //@RequiresPermissions("device:view")
    @ApiOperation(value = "根据查询条件查询学生的设备定位信息", notes = "根据查询条件查询学生的设备定位信息", tags = ApiTagsConstant.TAG_LOCATION, httpMethod = "GET")
    public List<EwatchLocation> searchDeviceLocation(@RequestParam(required = false) Long studentId,
                                                     @RequestParam(required = false) String studentName,
                                                     @RequestParam(required = false) String schoolName,
                                                     @RequestParam(required = false) String busCode,
                                                     @RequestParam(required = false) String numberPlate
                                                     ) {
        List<Device> devices = deviceService.listByStudentInfo(studentId, studentName, schoolName, busCode, numberPlate);

        List<EwatchLocation> ewatchLocations = devices.stream().map(d -> {
            EWatchInfo info = cacheDeviceInfoService.getLastEWatch(d.getDeviceCode());
            EwatchLocation location = new EwatchLocation(info);
            location.setStudentName(d.getStudentName());
            location.setSchoolName(d.getSchoolName());
            location.setMainGuardianMobile(d.getMainGuardianMobile());
            location.setMainGuardianName(d.getMainGuardianName());
            return location;
        }).collect(Collectors.toList());

        return ewatchLocations;
    }

    @GetMapping("/{studentId}/device/list")
//    //@RequiresPermissions("device:view")
    @ApiOperation(value = "获得学生设备历史定位信息", notes = "获得学生设备历史定位信息", tags = ApiTagsConstant.TAG_LOCATION, httpMethod = "GET")
    public List<EwatchLocation> deviceLocationHis(@PathVariable Long studentId,
                                                  @ApiParam(name = "startTimestamp", example = "1568110087000") @Valid @NotNull(message = "{required}") @RequestParam(name = "startTimestamp") Long startTimestamp,
                                                  @ApiParam(name = "endTimestamp", example = "1569319693142") @Valid @NotNull(message = "{required}")  @RequestParam(name = "endTimestamp")  Long endTimestamp) throws BusApiException {
        Device device = deviceService.getByStudentId(studentId);
        if(device == null) {
            throw new BusApiException("该学生未绑定手环");
        }
        if(endTimestamp < startTimestamp) {
            throw new BusApiException("开始时间必须小于结束时间");
        }

        Collection<EWatchInfo> eWatchList = cacheDeviceInfoService.listDeviceInfo(startTimestamp, endTimestamp, device.getDeviceCode());
        return eWatchList.stream().map(EwatchLocation::new).collect(Collectors.toList());
    }

    @GetMapping("/{studentId}/bus")
//    //@RequiresPermissions("device:view")
    @ApiOperation(value = "根据学生ID获取车辆定位信息", notes = "根据学生ID获取车辆定位信息", tags = ApiTagsConstant.TAG_LOCATION, httpMethod = "GET")
    public DvrLocation busLocationByStudentId(@PathVariable Long studentId) throws BusApiException {
        // 获得学生所在的车辆
        Dvr dvr = dvrService.getByStudentId(studentId);
        if(dvr == null) {
            throw new BusApiException("无法获取DVR信息");
        }

        DvrLocation dvrLocation = cacheDeviceInfoService.getLastDvr(dvr.getDvrCode());
        return dvrLocation;
    }

    @GetMapping("/bus/{busId}")
//    //@RequiresPermissions("device:view")
    @ApiOperation(value = "车辆定位信息", notes = "车辆定位信息", tags = ApiTagsConstant.TAG_LOCATION, httpMethod = "GET")
    public DvrLocation busLocationByBusId(@PathVariable Long busId) throws BusApiException {
        // 获得车辆绑定的DVR
        Dvr dvr = dvrService.getByBusId(busId);
        if(dvr == null) {
            throw new BusApiException("该学生未绑定车辆");
        }

        DvrLocation dvrLocation = cacheDeviceInfoService.getLastDvr(dvr.getDvrCode());
        return dvrLocation;
    }

    @GetMapping("/bus")
//    //@RequiresPermissions("device:view")
    @ApiOperation(value = "车辆定位信息，包括路线和站点", notes = "车辆定位信息", tags = ApiTagsConstant.TAG_LOCATION, httpMethod = "GET", hidden = true)
    public BusLocationInfoVO busLocationByBusId(@RequestParam(name = "numberPlate", required = false) String numberPlate,
                                                @RequestParam(name = "busCode", required = false) String busCode) throws BusApiException {
        if(StringUtils.isBlank(numberPlate) && StringUtils.isBlank(busCode)) {
            throw new BusApiException("查询条件不能为空");
        }
        Bus bus = searchBus(numberPlate, busCode);
        // 获得车辆绑定的DVR
        Dvr dvr = dvrService.getByBusId(bus.getId());
        if(dvr == null) {
            throw new BusApiException("该车辆未绑定DVR，没有定位信息");
        }
        // 获得定位数据
        DvrLocation dvrLocation = cacheDeviceInfoService.getLastDvr(dvr.getDvrCode());

        // 获得站点数据
        List<Stop> stops = stopService.listStopByBindBus(bus.getId());

        // 获取车辆路线绑定的轨迹
        Trajectory trajectory = trajectoryService.getByBusId(bus.getId());

        BusLocationInfoVO vo = new BusLocationInfoVO();
        vo.setBus(bus);
        vo.setLocation(dvrLocation);
        vo.setStopList(stops);
        vo.setTrajectory(trajectory);

        return vo;
    }

    public Bus searchBus(@RequestParam(name = "numberPlate", required = false) String numberPlate, @RequestParam(name = "busCode", required = false) String busCode) throws BusApiException {
        LambdaQueryWrapper<Bus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bus::getDeleted, false);
        if(StringUtils.isNotBlank(numberPlate)) {
            queryWrapper.eq(Bus::getNumberPlate, numberPlate);
        }
        if(StringUtils.isNotBlank(busCode)) {
            queryWrapper.eq(Bus::getBusCode, busCode);
        }

        List<Bus> busList = busService.list(queryWrapper);
        if(busList == null || busList.isEmpty()) {
            throw new BusApiException("车辆不存在");
        }
        //TODO 这里要处理下
        return busList.get(0);
    }

    @GetMapping("/bus/his")
//    //@RequiresPermissions("device:view")
    @ApiOperation(value = "车辆历史定位信息", notes = "车辆历史定位信息", tags = ApiTagsConstant.TAG_LOCATION, httpMethod = "GET")
    public BusLocationListVO busLocationHis(@RequestParam(name = "numberPlate", required = false) String numberPlate,
                                            @RequestParam(name = "busCode", required = false) String busCode,
                                            @ApiParam(name = "startTimestamp", example = "1568110087000") @Valid @NotNull(message = "{required}") @RequestParam(name = "startTimestamp") Long startTimestamp,
                                            @ApiParam(name = "endTimestamp", example = "1568110087000") @Valid @NotNull(message = "{required}") @RequestParam(name = "endTimestamp") Long endTimestamp
                                            ) throws BusApiException {
        BusLocationListVO vo = new BusLocationListVO();
        Bus bus = searchBus(numberPlate, busCode);
        if(bus == null) {
            return vo;
        }
        vo.setBus(bus);
        List<DvrLocation> locations = dvrLocationService.listByBusId(bus.getId(), startTimestamp, endTimestamp);
        vo.setLocations(locations);
        return vo;
    }

    @GetMapping("/bus/realtime")
//    //@RequiresPermissions("device:view")
    @ApiOperation(value = "车辆实时定位信息", notes = "车辆实时定位信息", tags = ApiTagsConstant.TAG_LOCATION, httpMethod = "GET")
    public List<BusDetailLocationVO> busLocationHis(@NotBlank(message = "{required}") @RequestParam(name = "busIds") String busIds) throws BusApiException {
        if(StringUtils.isBlank(busIds)) {
            throw new BusApiException("参数不能为空");
        }

        String[] busIdArray = busIds.split(StringPool.COMMA);
//        List<Long> busIdList = new ArrayList<>();
//        for(int i=0;i<=busIdArray.length;i++) {
//            busIdList.add(Long.parseLong(busIdArray[i]));
//        }
        List<BusDetailLocationVO> busDetailLocationVOS = busService.listDetailByIds(busIdArray);
        return busDetailLocationVOS.stream().peek( b -> {
            DvrLocation dvrLocation = cacheDeviceInfoService.getLastDvr(b.getDvrCode());
            b.setLocation(dvrLocation);
        }).collect(Collectors.toList());
    }
}
