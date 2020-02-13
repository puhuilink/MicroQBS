package com.phlink.bus.api.route.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.BusTeacher;
import com.phlink.bus.api.bus.domain.Driver;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnCheckID;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.route.domain.Route;
import com.phlink.bus.api.route.domain.RouteOperation;
import com.phlink.bus.api.route.domain.vo.BindStudentVO;
import com.phlink.bus.api.route.domain.vo.RouteOperationDetailVO;
import com.phlink.bus.api.route.domain.vo.SaveRouteOperationVO;
import com.phlink.bus.api.route.service.IRouteOperationService;
import com.phlink.bus.api.route.service.IRouteService;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


/**
 * @author wen
 */
@Slf4j
@RestController
@Validated
@RequestMapping("/route-operation")
@Api(tags = "路线关联")
public class RouteOperationController extends BaseController {

    @Autowired
    public IRouteOperationService routeOperationService;

    @Autowired
    public IStudentService studentService;

    @Autowired
    public IRouteService routeService;

    @Autowired
    public IBusService busService;

    @Autowired
    public UserService userService;

    @GetMapping
//    //@RequiresPermissions("routeOperation:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "路线关联", httpMethod = "GET")
    public Map<String, Object> listRouteOperation(QueryRequest request, RouteOperation routeOperation) {
        return getDataTable(this.routeOperationService.listRouteOperations(request, routeOperation));
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("routeOperation:view")
    @ApiOperation(value = "详情", notes = "详情", tags = "路线关联", httpMethod = "GET")
    public RouteOperationDetailVO detail(@PathVariable Long id) {
        return this.routeOperationService.getDetailVO(id);
    }

    @GetMapping("/detail-by-student")
//    //@RequiresPermissions("routeOperation:view")
    @ApiOperation(value = "根据学生ID获得路线详情", notes = "根据学生ID获得路线详情", tags = "路线关联", httpMethod = "GET")
    public RouteOperationDetailVO detailByStudent(@NotNull(message = "{required}") @RequestParam(name = "studentId") Long studentId) throws BusApiException {
        Student student = studentService.getById(studentId);
        if(student == null) {
            throw new BusApiException("该学生不存在");
        }
        return this.routeOperationService.getDetailVO(student.getRouteOperationId());
    }

    @GetMapping("/detail-by-bus")
//    //@RequiresPermissions("routeOperation:view")
    @ApiOperation(value = "根据车辆ID获得路线详情", notes = "根据车辆ID获得路线详情", tags = "路线关联", httpMethod = "GET")
    public RouteOperationDetailVO detailByBus(@NotNull(message = "{required}") @RequestParam(name = "busId") Long busId) throws BusApiException {
        RouteOperation routeOperation = routeOperationService.getByBusId(busId);
        if(routeOperation == null) {
            throw new BusApiException("该车辆未绑定路线");
        }
        return this.routeOperationService.getDetailVO(routeOperation.getId());
    }


    @Log("删除路线关联")
    @DeleteMapping("/{routeOperationIds}")
//    //@RequiresPermissions("routeOperation:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "路线关联", httpMethod = "DELETE")
    public void deleteRouteOperation(@NotBlank(message = "{required}") @PathVariable String routeOperationIds) throws BusApiException {
        try {
            String[] ids = routeOperationIds.split(StringPool.COMMA);
            this.routeOperationService.deleteRouteOperations(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
//    //@RequiresPermissions("routeOperation:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "路线关联", httpMethod = "POST")
    public void exportRouteOperation(@RequestBody RouteOperation routeOperation, HttpServletResponse response) throws BusApiException {
        try {
            List<RouteOperation> routeOperations = this.routeOperationService.listRouteOperations(routeOperation);
            ExcelKit.$Export(RouteOperation.class, response).downXlsx(routeOperations, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("批量为多个站点绑定学生")
    @PutMapping("/bind-stops-students")
    @Validated(value = {OnAdd.class})
//    //@RequiresPermissions("routeOperation:updateBind")
    @ApiOperation(value = "批量为多个站点绑定学生", notes = "站点和学生绑定", tags = "路线关联", httpMethod = "PUT")
    public void bind(@RequestBody @Valid List<BindStudentVO> bindStudentVOList) throws BusApiException, RedisConnectException {
        for (BindStudentVO bindStudentVO : bindStudentVOList) {
            if(bindStudentVO.getStudentIds() == null || bindStudentVO.getStudentIds().isEmpty()) {
                continue;
            }
            studentService.updateStudentStopId(bindStudentVO.getStopId(), bindStudentVO.getStudentIds(), bindStudentVO.getRouteOperationId());
        }
    }


    @Log("批量为多个站点解绑学生")
    @PutMapping("/unbind-students")
    @Validated(value = {OnUpdate.class})
    //@RequiresPermissions("routeOperation:updateUnbind")
    @ApiOperation(value = "批量为多个站点解绑学生", notes = "站点和学生解绑", tags = "路线关联", httpMethod = "PUT")
    public void unbind(@RequestBody @Valid List<BindStudentVO> bindStudentVOList) throws BusApiException, RedisConnectException {
        for (BindStudentVO bindStudentVO : bindStudentVOList) {
            if(bindStudentVO.getStudentIds() == null || bindStudentVO.getStudentIds().isEmpty()) {
                continue;
            }
            studentService.removeStudentStopId(bindStudentVO.getStopId(), bindStudentVO.getStudentIds(), bindStudentVO.getRouteOperationId());
        }
    }

    @GetMapping("/unbind-student")
//    //@RequiresPermissions("routeOperation:view")
    @ApiOperation(value = "未绑定的学生列表", notes = "未绑定的学生列表，只和该路线所关联的学校相关", tags = "路线关联", httpMethod = "GET")
    public List<Student> unbindStudentList(@RequestParam Long schoolId) throws BusApiException{

        return studentService.listStudentUnbindStopInSchool(schoolId);
    }

    @GetMapping("/unbind-bus")
//    //@RequiresPermissions("routeOperation:view")
    @ApiOperation(value = "未绑定的车辆列表", notes = "未绑定的车辆列表", tags = "路线关联", httpMethod = "GET")
    public List<Bus> unbindBusList(Bus bus) throws BusApiException{
        return busService.unbindBusList(bus);
    }

    @GetMapping("/unbind-driver")
//    //@RequiresPermissions("routeOperation:view")
    @ApiOperation(value = "未绑定的司机列表", notes = "未绑定的司机列表", tags = "路线关联", httpMethod = "GET")
    public List<User> unbindDriverList(Driver driver) throws BusApiException{
        return busService.unbindDriverList(driver);
    }

    @GetMapping("/unbind-teacher")
//    //@RequiresPermissions("routeOperation:view")
    @ApiOperation(value = "未绑定的老师列表", notes = "未绑定的老师列表", tags = "路线关联", httpMethod = "GET")
    public List<User> unbindTeacherList(BusTeacher busTeacher) throws BusApiException{
        return busService.unbindBusTeacherList(busTeacher);
    }

    @GetMapping("/bind-student")
//    //@RequiresPermissions("routeOperation:view")
    @ApiOperation(value = "已绑定的学生列表", notes = "已绑定的学生列表", tags = "路线关联", httpMethod = "GET")
    public List<Student> bindStudent(@RequestParam Long routeOperationId,
                                     @RequestParam(required = false) String stopName,
                                     @RequestParam(required = false) String studentName) throws BusApiException{
        return busService.listBindStudent(routeOperationId, stopName, studentName);
    }

    @GetMapping("/unbind-routes")
//    //@RequiresPermissions("routeOperation:unbindRoutes")
    @ApiOperation(value = "未绑定路线列表", notes = "未绑定路线列表", tags = "路线关联", httpMethod = "GET")
    public List<Route> unbindRoutes(@RequestParam(required = false) Long routeId) throws BusApiException{
        return routeOperationService.listUnbindRoutes(routeId);
    }


    /*
     *
     *
     *
     * 路线创建、编辑V2
     *
     *
     *
     */
    @Log("添加路线关联")
    @Validated(OnAdd.class)
    @PostMapping("/v2/save")
    //@RequiresPermissions("routeOperation:add")
    @ApiOperation(value = "添加路线关联", notes = "添加路线关联", tags = "路线V2.0", httpMethod = "POST")
    public BusApiResponse saveRoute(@RequestBody @Valid SaveRouteOperationVO routeOperationVO) throws BusApiException {
        Route route = routeService.getById(routeOperationVO.getRouteId());
        if(route == null) {
            throw new BusApiException("绑定的路线不存在");
        }
        RouteOperation routeOperation = this.routeOperationService.createRouteOperation(routeOperationVO);
        return new BusApiResponse().data(routeOperation);
    }

    @Log("修改路线关联")
    @Validated({OnUpdate.class, OnCheckID.class})
    @PutMapping("/v2/update")
    //@RequiresPermissions("routeOperation:update")
    @ApiOperation(value = "修改路线关联", notes = "修改路线关联", tags = "路线V2.0", httpMethod = "PUT")
    public BusApiResponse updateRoute(@RequestBody @Valid SaveRouteOperationVO routeOperationVO) throws BusApiException {
        RouteOperation routeOperation = routeOperationService.getById(routeOperationVO.getId());
        if(routeOperation == null) {
            throw new BusApiException("更新的路线关联不存在");
        }
        Route route = routeService.getById(routeOperationVO.getRouteId());
        if(route == null) {
            throw new BusApiException("绑定的路线不存在");
        }
        RouteOperation routeOperationResult = this.routeOperationService.updateRouteOperation(routeOperationVO);
        return new BusApiResponse().data(routeOperationResult);
    }
}
