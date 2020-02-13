package com.phlink.bus.api.route.controller;


import com.phlink.bus.api.bus.domain.BindBusDetailInfo;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.route.domain.*;
import com.phlink.bus.api.route.domain.enums.TripLogRunningEnum;
import com.phlink.bus.api.route.domain.vo.StopAttendanceDownVO;
import com.phlink.bus.api.route.domain.vo.StopAttendanceUpVO;
import com.phlink.bus.api.route.domain.vo.TripStopTimeListVO;
import com.phlink.bus.api.route.manager.TripTask;
import com.phlink.bus.api.route.service.*;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import com.phlink.bus.api.trajectory.service.ITrajectoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


/**
 * @author wen
 */
@Slf4j
@RestController
@RequestMapping("/trip")
@Api(tags = "行程")
public class TripController extends BaseController {

    @Autowired
    public ITripService tripService;
    @Autowired
    public TripTask tripTask;
    @Autowired
    public IStopTimeService stopTimeService;
    @Autowired
    public IBusService busService;
    @Autowired
    public IStudentService studentService;
    @Autowired
    public IRouteOperationService routeOperationService;
    @Autowired
    public ITripLogService tripLogService;
    @Autowired
    public IStopAttendanceService stopAttendanceService;
    @Autowired
    public ITrajectoryService trajectoryService;
    @Autowired
    public IRouteService routeService;

    @GetMapping("/{id}")
//    //@RequiresPermissions("trip:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "行程", httpMethod = "GET")
    public Trip detail(@NotBlank(message = "{required}") @PathVariable Long id) {
        return this.tripService.findById(id);
    }

    @GetMapping("/{tripId}/stop-time")
//    //@RequiresPermissions("trip:view")
    @ApiOperation(value = "一条行程的站点列表", notes = "一条行程的站点列表", tags = "行程", httpMethod = "GET")
    public List<StopTime> stopTimes(@PathVariable Long tripId) {
        return this.stopTimeService.listByTrip(tripId);
    }

    @GetMapping("/for-bus-teacher")
//    //@RequiresPermissions("trip:view")
    @ApiOperation(value = "随车老师获得自己所在车辆的行程列表", notes = "随车老师获得自己所在车辆的行程列表", tags = "行程", httpMethod = "GET")
    public List<TripStopTimeListVO> listForBusTeacher() throws BusApiException {
        return this.stopTimeService.listTripStopTimeListVOByBusTeacher();
    }

    @GetMapping("/for-now-trip")
//    //@RequiresPermissions("trip:view")
    @ApiOperation(value = "随车老师获得自己所在车辆的行程状态", notes = "随车老师获得自己所在车辆的行程状态", tags = "行程", httpMethod = "GET")
    public Map<String, Object> listByBusTeacherForNowTrip() throws BusApiException {
        Bus bus = getBusByWorker();
        TripState tripState = tripService.getCurrentLogRunningTrip(bus.getBusCode());
        if(tripState == null) {
            throw new BusApiException("没有开启的行程");
        }
        return getDataTable(this.tripService.listByBusTeacherForNowTrip(tripState.getId(), BusApiUtil.getCurrentUser().getUserId()));
    }

    @GetMapping("/for-now-trip/student/{studentId}")
    @ApiOperation(value = "获得学生当前的行程", notes = "获得学生当前的行程", tags = "行程", httpMethod = "GET")
    public Trip getTrip(@PathVariable Long studentId) throws BusApiException {
        return this.tripService.getNowTripForStudent(studentId);
    }

    @GetMapping("/for-stop-student/{tripTime}")
//    //@RequiresPermissions("trip:view")
    @ApiOperation(value = "随车老师获得自己所在车辆的站点学生出勤信息", notes = "随车老师获得自己所在车辆的站点学生出勤信息", tags = "行程", httpMethod = "GET")
    public Map<String, Object> listByBusTeacherForStudent(@PathVariable String tripTime) throws BusApiException {
        User currentUser = BusApiUtil.getCurrentUser();
        BindBusDetailInfo bus = busService.getBindBusDetailInfoByWorkerId(currentUser.getUserId());
        return getDataTable(this.tripService.listTripStopTimeStudentDetail(tripTime, bus.getSchoolId()));
    }

    @GetMapping("/by-student/{studentId}")
//    //@RequiresPermissions("trip:get")
    @ApiOperation(value = "根据学生的ID获取该学生的全部行程列表", notes = "根据学生的ID获取该学生的全部行程列表", tags = "行程", httpMethod = "GET")
    public Map<String, Object> listTripByStudent(@PathVariable Long studentId) throws BusApiException {
        return getDataTable(this.tripService.listTripByStudentId(studentId));
    }

    @GetMapping("/by-student-stop/{stopId}/{tripTime}")
//    //@RequiresPermissions("trip:get")
    @ApiOperation(value = "随车老师获得自己所在车辆的某站点学生出勤信息", notes = "随车老师获得自己所在车辆的某站点学生出勤信息", tags = "行程", httpMethod = "GET")
    public Map<String, Object> listStudent(@PathVariable Long stopId, @PathVariable String tripTime) throws BusApiException {
        return getDataTable(this.tripService.listStuByStopId(stopId, tripTime));
    }

    @GetMapping("/by-school-stop/{tripTime}")
//    //@RequiresPermissions("trip:get")
    @ApiOperation(value = "随车老师获得自己所在车辆的学校站点的未打卡学生信息", notes = "随车老师获得自己所在车辆的学校站点的未打卡学生信息", tags = "行程", httpMethod = "GET")
    public Map<String, Object> listSchoolStudent(@PathVariable String tripTime) throws BusApiException {
        return getDataTable(this.tripService.listSchoolStudentBy(tripTime));
    }

    @GetMapping("/for-stop-state")
//  //@RequiresPermissions("trip:view")
    @ApiOperation(value = "随车老师获得自己所在车辆的站点", notes = "随车老师获得自己所在车辆的站点", tags = "行程", httpMethod = "GET")
    public BusApiResponse getBusStopTime() throws BusApiException {
        // 1、获得车辆当前的tripstate
        Bus bus = getBusByWorker();
        TripState state = tripService.getCurrentLogRunningTrip(bus.getBusCode());
        if(state == null) {
            throw new BusApiException("行程还未开始");
        }
        return new BusApiResponse().data(this.tripService.getBusStopTime(state));
    }

    @GetMapping("/student/{studentId}/for-stop-state")
//  //@RequiresPermissions("trip:view")
    @ApiOperation(value = "家长获得学生所在车辆的站点", notes = "家长获得学生所在车辆的站点", tags = "行程", httpMethod = "GET")
    public BusApiResponse getBusStopTime(@PathVariable("studentId") Long studentId) throws BusApiException {
        Student student = studentService.getById(studentId);
        if(student == null) {
            throw new BusApiException("该学生不存在");
        }
        RouteOperation routeOperation = routeOperationService.getById(student.getRouteOperationId());
        if(routeOperation == null) {
            throw new BusApiException("该学生还未绑定车辆");
        }
        Bus bus = busService.getById(routeOperation.getBindBusId());
        if(bus == null) {
            throw new BusApiException("绑定的车辆不存在");
        }
        TripState state = tripService.getCurrentLogRunningTrip(bus.getBusCode());
        if(state == null) {
            throw new BusApiException("行程还未开始");
        }
        return new BusApiResponse().data(this.tripService.getBusStopTime(state));
    }

    /*
     *
     *
     *
     *
     * V2版本
     *
     *
     *
     *
     *
     */
    @GetMapping("/v2/list")
//  //@RequiresPermissions("trip:view")
    @ApiOperation(value = "员工所在车辆的行程状态列表", notes = "员工所在车辆的行程状态列表", tags = "行程V2.0", httpMethod = "GET")
    public BusApiResponse listTripStates() throws BusApiException {
        Bus bus = getBusByWorker();
        List<TripState> stateList = tripService.listTripToday(bus);
        return new BusApiResponse().data(stateList);
    }

    @Log("开始行程")
    @PostMapping("/v2/start/{tripId}")
    @ApiOperation(value = "开始行程", notes = "开始行程，先判断有没有运行中的行程，如果有，会返回错误，没有，则开启下一个行程", tags = "行程V2.0", httpMethod = "POST")
    public BusApiResponse startTrip(@PathVariable Long tripId) throws BusApiException {
        Bus bus = getBusByWorker();
        TripState tripState = tripService.getTripState(bus, tripId);
        if(tripState != null){
            if(TripLogRunningEnum.FINISH.equals(tripState.getLogRunningState())) {
                throw new BusApiException("该行程已结束");
            }
            if(TripLogRunningEnum.RUNNING.equals(tripState.getLogRunningState())) {
                throw new BusApiException("该行程已开始");
            }
        }else{
            throw new BusApiException("未找到该行程");
        }

        TripLog tripLog = tripService.start(bus, tripId);
        return new BusApiResponse().data(tripLog);
    }

    @Log("结束行程")
    @PostMapping("/v2/end/{tripId}")
    @ApiOperation(value = "结束当日某一个行程", notes = "结束当日某一个行程，先判断有没有运行中的行程，如果没有，会返回错误，有，则停止该行程", tags = "行程V2.0", httpMethod = "POST")
    public BusApiResponse stopTrip(@PathVariable Long tripId) throws BusApiException {
        Bus bus = getBusByWorker();
        TripState tripState = tripService.getTripState(bus, tripId);
        if(tripState != null){
            if(TripLogRunningEnum.FINISH.equals(tripState.getLogRunningState())) {
                throw new BusApiException("该行程已结束");
            }
            if(TripLogRunningEnum.WAITE.equals(tripState.getLogRunningState())) {
                throw new BusApiException("该行程还未开始");
            }
        }else{
            throw new BusApiException("未找到该行程");
        }
        TripLog tripLog = tripService.stop(bus, tripId);
        return new BusApiResponse().data(tripLog);
    }

    public Bus getBusByWorker() throws BusApiException {
        User worker = BusApiUtil.getCurrentUser();
        Bus bus = busService.getBusByWorkerId(worker.getUserId());
        if(bus == null) {
            throw new BusApiException("还未绑定车辆");
        }
        return bus;
    }

    @Log("老师给学生打卡上车")
    @PostMapping("/v2/attendance-up")
//    //@RequiresPermissions("stopAttendance:add")
    @ApiOperation(value = "老师给学生打卡上车", notes = "老师给学生打卡上车", tags = "行程V2.0", httpMethod = "POST")
    public void studentAttendanceUp(@RequestBody StopAttendanceUpVO stopAttendanceVO) throws BusApiException {
        Student student = studentService.getById(stopAttendanceVO.getStudentId());
        if(student == null) {
            throw new BusApiException("学生不存在");
        }
        Bus bus = getBusByWorker();
        TripState state = tripService.getTripState(bus, stopAttendanceVO.getTripId());
        if(state == null) {
            throw new BusApiException("没有行程可供打卡");
        }
        // 打卡
        tripService.attendanceUp(state, stopAttendanceVO.getStudentId(), BusApiUtil.getCurrentUser().getUserId(), stopAttendanceVO.getReason());
    }

    @Log("老师给学生打卡下车")
    @PostMapping("/v2/attendance-down")
//    //@RequiresPermissions("stopAttendance:add")
    @ApiOperation(value = "老师给学生打卡下车", notes = "老师给学生打卡下车", tags = "行程V2.0", httpMethod = "POST")
    public void studentAttendanceDown(@RequestBody StopAttendanceDownVO stopAttendanceVO) throws BusApiException {
        Student student = studentService.getById(stopAttendanceVO.getStudentId());
        if(student == null) {
            throw new BusApiException("学生不存在");
        }
        Bus bus = getBusByWorker();
        TripState state = tripService.getTripState(bus, stopAttendanceVO.getTripId());
        if(state == null) {
            throw new BusApiException("没有行程可供打卡");
        }
        // 打卡
        tripService.attendanceDown(state, stopAttendanceVO.getStudentId(), BusApiUtil.getCurrentUser().getUserId());

    }

//    @Log("人脸打卡")
    @PostMapping("/v2/face-attendance/{studentId}")
//    //@RequiresPermissions("stopAttendance:add")
    @ApiOperation(value = "人脸打卡", notes = "人脸打卡", tags = "行程V2.0", httpMethod = "POST")
    public BusApiResponse studentFaceAttendance(@PathVariable Long studentId) throws BusApiException {
        Student student = studentService.getById(studentId);
        if(student == null) {
            log.info("studentFaceAttendance  {} 学生不存在", studentId);
            return new BusApiResponse().data(false).message("学生不存在");
        }
        RouteOperation routeOperation = routeOperationService.getById(student.getRouteOperationId());
        if(routeOperation == null) {
            log.info("studentFaceAttendance  {} 该学生还未绑定车辆", studentId);
            return new BusApiResponse().data(false).message("该学生还未绑定车辆");
        }
        Bus bus = busService.getById(routeOperation.getBindBusId());
        if(bus == null) {
            log.info("studentFaceAttendance  {} 绑定的车辆不存在 busId:{}", studentId, routeOperation.getBindBusId());
            return new BusApiResponse().data(false).message("绑定的车辆不存在");
        }
        TripState state = tripService.getCurrentLogRunningTrip(bus.getBusCode());
        if(state == null) {
            log.info("studentFaceAttendance  {} 行程还未开始 busCode:{}", studentId, bus.getBusCode());
            return new BusApiResponse().data(false).message("行程还未开始");
        }
        // 打卡
        tripService.faceAttendance(state, studentId, state.getBusDetailInfo().getBindBusTeacherId());
        return new BusApiResponse().data(true);
    }

    @GetMapping("/v2/student-trip")
//  //@RequiresPermissions("trip:view")
    @ApiOperation(value = "学生所在车辆的当前行程状态", notes = "没有运行中的行程时，返回上一次的行程，没有上一次的行程，则返回行程列表中第一条记录对应的行程", tags = "行程V2.0", httpMethod = "GET")
    public BusApiResponse getStudentTripState(@RequestParam(name = "studentId") Long studentId) throws BusApiException {
        Student student = studentService.getById(studentId);
        if(student == null) {
            throw new BusApiException("该学生不存在");
        }
        RouteOperation routeOperation = routeOperationService.getById(student.getRouteOperationId());
        if(routeOperation == null) {
            throw new BusApiException("该学生还未绑定车辆");
        }
        Bus bus = busService.getById(routeOperation.getBindBusId());
        if(bus == null) {
            throw new BusApiException("绑定的车辆不存在");
        }
        TripState tripState = tripService.getCurrentLogRunningTrip(bus.getBusCode());
        if(tripState == null) {
            throw new BusApiException("没有开启的行程");
        }
        TripStateStudent tripStateStudent = new TripStateStudent();
        BeanUtils.copyProperties(tripState, tripStateStudent);
        tripStateStudent.clearStudent();
        Long routeId = tripStateStudent.getBusDetailInfo().getRouteId();
        // 车辆定位
        Route route = routeService.getById(routeId);
        try{
            Trajectory trajectory = this.trajectoryService.detail(route.getTrajectoryId(), true);
            tripStateStudent.setPoints(trajectory.getSimplePoints());
        } catch (Exception e) {

        }

        return new BusApiResponse().data(tripStateStudent);
    }

    @Log("初始化行程")
    @PostMapping("/v2/test/initTrip")
    public void initTrip(){
        tripTask.initBusTripList();
    }

}
