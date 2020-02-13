package com.phlink.bus.api.route.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnCheckID;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.route.service.IStopService;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


/**
* @author wen
*/
@Slf4j
@Validated
@RestController
@RequestMapping("/stop")
@Api(tags ="站点")
public class StopController extends BaseController {

    @Autowired
    public IStopService stopService;
    @Autowired
    public IStudentService studentService;

    @GetMapping
//    //@RequiresPermissions("stop:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "站点", httpMethod = "GET")
    public Map<String, Object> listStop(QueryRequest request, Stop stop) {
        return  getDataTable(this.stopService.listStops(request, stop));
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("stop:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "站点", httpMethod = "GET")
    public Stop detail(@NotBlank(message = "{required}") @PathVariable Long id) {
        return this.stopService.findById(id);
    }

    @Log("添加站点")
    @PostMapping
    @Validated(value = {OnAdd.class})
//    //@RequiresPermissions("stop:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "站点", httpMethod = "POST")
    public Stop addStop(@RequestBody @Valid Stop stop) throws BusApiException {
        try {
            this.stopService.createStop(stop);
            return stop;
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @GetMapping("check/{stopName}")
    public boolean checkStopName(@NotBlank(message = "{required}") @PathVariable String stopName) {
        return this.stopService.findByName(stopName, null) == null;
    }

    @Log("批量添加站点")
    @PostMapping("/batch")
    @Validated(value = {OnAdd.class})
//    //@RequiresPermissions("stop:add")
    @ApiOperation(value = "批量添加站点", notes = "批量添加站点", tags = "站点", httpMethod = "POST")
    public List<Stop> batchAddStop(@RequestBody @Valid List<Stop> stops) throws BusApiException {
        try {
            this.stopService.batchCreateStop(stops);
            return stops;
        } catch (Exception e) {
            String message = "批量新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改站点")
    @PutMapping
    @Validated(value = {OnUpdate.class, OnCheckID.class})
//    //@RequiresPermissions("stop:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "站点", httpMethod = "PUT")
    public void updateStop(@RequestBody @Valid Stop stop) throws BusApiException{
        try {
            this.stopService.modifyStop(stop);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("批量修改站点")
    @PutMapping("batch")
    @Validated(value = {OnAdd.class})
//    //@RequiresPermissions("stop:update")
    @ApiOperation(value = "批量修改", notes = "批量修改", tags = "站点", httpMethod = "PUT")
    public List<Stop> batchUpdateStop(@RequestBody @Valid List<Stop> stopList) throws BusApiException{
        this.stopService.batchModifyStop(stopList);
        return stopList;
    }

    @Log("编辑站点坐标点")
    @PutMapping("/point")
    @Validated(value = {OnUpdate.class, OnCheckID.class})
//    //@RequiresPermissions("stop:update")
    @ApiOperation(value = "编辑坐标点", notes = "编辑坐标点", tags = "站点", httpMethod = "PUT")
    public void updatePoint(@RequestBody @Valid Stop stop) throws BusApiException{
        this.stopService.modifyStop(stop);
    }

    @Log("编辑站点坐标点")
    @PutMapping("/point/batch")
    @Validated(value = {OnUpdate.class, OnCheckID.class})
//    //@RequiresPermissions("stop:update")
    @ApiOperation(value = "批量编辑坐标点", notes = "批量编辑坐标点", tags = "站点", httpMethod = "PUT")
    public void batchUpdatePoint(@RequestBody @Valid List<Stop> stopList) throws BusApiException{
        stopService.updateBatchById(stopList);
    }

    @Log("删除站点")
    @DeleteMapping("/{stopIds}")
//    //@RequiresPermissions("stop:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "站点", httpMethod = "DELETE")
    public void deleteStop(@NotBlank(message = "{required}") @PathVariable String stopIds) throws BusApiException{
        try {
            String[] ids = stopIds.split(StringPool.COMMA);
    this.stopService.deleteStops(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("stop:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "站点", httpMethod = "POST")
    public void exportStop(QueryRequest request,@RequestBody Stop stop, HttpServletResponse response) throws BusApiException{
        try {
            List<Stop> stops = this.stopService.listStops(request, stop).getRecords();
            ExcelKit.$Export(Stop.class, response).downXlsx(stops, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @GetMapping("/{stopId}/student")
//    //@RequiresPermissions("stop:view")
    @ApiOperation(value = "站点的学生列表", notes = "站点的学生列表", tags = "路线关联", httpMethod = "GET")
    public List<Student> studentList(@PathVariable Long stopId) throws BusApiException {
        return studentService.listStudentByStopId(stopId);
    }

    @GetMapping("/{stopId}/check-bind-student")
//    //@RequiresPermissions("stop:view")
    @ApiOperation(value = "检查该站点是否绑定了学生", notes = "绑定，返回true，未绑定，返回false", tags = "路线关联", httpMethod = "GET")
    public BusApiResponse checkBindStudent(@PathVariable Long stopId) throws BusApiException {
        List<Student> students = studentService.listStudentByStopId(stopId);
        return new BusApiResponse().data(!students.isEmpty());
    }

}
