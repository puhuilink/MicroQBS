package com.phlink.bus.api.route.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.TripLog;
import com.phlink.bus.api.route.service.IStopAttendanceService;
import com.phlink.bus.api.route.service.ITripLogService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


/**
 * @author zhouyi
 */
@Slf4j
@RestController
@RequestMapping("/trip-log")
@Api(tags = "行程记录")
public class TripLogController extends BaseController {

    @Autowired
    public ITripLogService tripLogService;

    @Autowired
    public IStopAttendanceService stopAttendanceService;

    @GetMapping
    //@RequiresPermissions("tripLog:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "", httpMethod = "GET")
    public Map<String, Object> listTripLog(QueryRequest request, TripLog tripLog) {
        return getDataTable(this.tripLogService.listTripLogs(request, tripLog));
    }

    @GetMapping("detail")
    //@RequiresPermissions("tripLog:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "", httpMethod = "GET")
    public TripLog detail(@NotBlank(message = "{required}") @RequestParam(value = "id") Long id) {
        return this.tripLogService.findById(id);
    }

    @GetMapping("/for-now-log/{tripTime}")
//    //@RequiresPermissions("trip-log:view")
    @ApiOperation(value = "随车老师查看当前的行程状态（该方法已不用）", notes = "随车老师查看当前的行程状态", tags = "行程日志", httpMethod = "GET")
    public TripLog detailNowTripLog(@PathVariable String tripTime) throws BusApiException {
        return this.tripLogService.detailNowTripLog(tripTime);
    }

    @Log("删除行程记录")
    @DeleteMapping("/{tripLogIds}")
//    //@RequiresPermissions("tripLog:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "", httpMethod = "DELETE")
    public void deleteTripLog(@NotBlank(message = "{required}") @PathVariable String tripLogIds) throws BusApiException {
        try {
            String[] ids = tripLogIds.split(StringPool.COMMA);
            this.tripLogService.deleteTripLogs(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
//    //@RequiresPermissions("tripLog:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "", httpMethod = "POST")
    public void exportTripLog(QueryRequest request, @RequestBody TripLog tripLog, HttpServletResponse response) throws BusApiException {
        try {
            List<TripLog> tripLogs = this.tripLogService.listTripLogs(request, tripLog).getRecords();
            ExcelKit.$Export(TripLog.class, response).downXlsx(tripLogs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
