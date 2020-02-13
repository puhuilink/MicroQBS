package com.phlink.bus.api.route.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.StopAttendance;
import com.phlink.bus.api.route.domain.vo.StudentAttendanceVO;
import com.phlink.bus.api.route.service.IStopAttendanceService;
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
@RequestMapping("/stop-attendance")
@Api(tags = "站点考勤")
public class StopAttendanceController extends BaseController {

    @Autowired
    public IStopAttendanceService stopAttendanceService;

    @GetMapping
//    //@RequiresPermissions("stopAttendance:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "站点考勤", httpMethod = "GET")
    public Map<String, Object> listStopAttendance(QueryRequest request, StopAttendance stopAttendance) {
        return getDataTable(this.stopAttendanceService.listStopAttendances(request, stopAttendance));
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("stopAttendance:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "站点考勤", httpMethod = "GET")
    public StopAttendance detail(@NotBlank(message = "{required}") @PathVariable Long id) {
        return this.stopAttendanceService.findById(id);
    }

    @Log("添加站点考勤")
    @PostMapping("/add")
//    //@RequiresPermissions("stopAttendance:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "站点考勤", httpMethod = "POST")
    public void addStopAttendance(@RequestBody @Valid StopAttendance stopAttendance) throws BusApiException {
        this.stopAttendanceService.createStopAttendance(stopAttendance);
    }

    @Log("学生使用手环打卡机打卡")
    @PostMapping("/student")
//    //@RequiresPermissions("stopAttendance:add")
    @ApiOperation(value = "学生打卡机打卡", notes = "学生打卡机打卡", tags = "站点考勤", httpMethod = "POST")
    public void studentAttendance(@RequestBody @Valid StudentAttendanceVO studentAttendanceVO) throws BusApiException {
        this.stopAttendanceService.studentAttendance(studentAttendanceVO);
    }
    
    @Log("添加学校站点考勤")
    @PostMapping("/school/add")
//    //@RequiresPermissions("stopAttendance:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "站点考勤", httpMethod = "POST")
    public void addSchoolAttendance(@RequestBody @Valid StopAttendance stopAttendance) throws BusApiException {
        this.stopAttendanceService.createSchoolAttendance(stopAttendance);
    }

    @Log("修改站点考勤")
    @PutMapping
//    //@RequiresPermissions("stopAttendance:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "站点考勤", httpMethod = "PUT")
    public void updateStopAttendance(@RequestBody @Valid StopAttendance stopAttendance) throws BusApiException {
        this.stopAttendanceService.modifyStopAttendance(stopAttendance);
    }

    @Log("删除站点考勤")
    @DeleteMapping("/stopAttendanceIds")
    //@RequiresPermissions("stopAttendance:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "站点考勤", httpMethod = "DELETE")
    public void deleteStopAttendance(@NotBlank(message = "{required}") @PathVariable String stopAttendanceIds) throws BusApiException {
        try {
            String[] ids = stopAttendanceIds.split(StringPool.COMMA);
            this.stopAttendanceService.deleteStopAttendances(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("stopAttendance:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "站点考勤", httpMethod = "POST")
    public void exportStopAttendance(QueryRequest request, @RequestBody StopAttendance stopAttendance, HttpServletResponse response) throws BusApiException {
        try {
            List<StopAttendance> stopAttendances = this.stopAttendanceService.listStopAttendances(request, stopAttendance).getRecords();
            ExcelKit.$Export(StopAttendance.class, response).downXlsx(stopAttendances, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
