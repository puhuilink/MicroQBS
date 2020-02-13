package com.phlink.bus.api.route.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.StopTime;
import com.phlink.bus.api.route.service.IStopTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * @author wen
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/stop-time")
@Api(tags = "站点时刻")
public class StopTimeController extends BaseController {

    @Autowired
    public IStopTimeService stopTimeService;

    @GetMapping("/{id}")
    //@RequiresPermissions("stopTime:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "站点时刻", httpMethod = "GET")
    public StopTime detail(@NotBlank(message = "{required}") @PathVariable Long id) {
        return this.stopTimeService.findById(id);
    }

    @Log("添加站点时刻")
    @PostMapping
    @Validated(value = {OnAdd.class})
    //@RequiresPermissions("stopTime:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "站点时刻", httpMethod = "POST")
    public void addStopTime(@RequestBody @Valid StopTime stopTime) throws BusApiException {
        try {
            this.stopTimeService.createStopTime(stopTime);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("批量添加站点时刻")
    @PostMapping("batch")
    @Validated(value = {OnAdd.class})
    //@RequiresPermissions("stopTime:add")
    @ApiOperation(value = "批量添加", notes = "批量添加", tags = "站点时刻", httpMethod = "POST")
    public void batchAddStopTime(@RequestBody @Valid List<StopTime> stopTime) throws BusApiException {
        try {
            this.stopTimeService.batchCreateStopTime(stopTime);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改站点时刻")
    @PutMapping
    @Validated(value = {OnUpdate.class})
    //@RequiresPermissions("stopTime:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "站点时刻", httpMethod = "PUT")
    public void updateStopTime(@RequestBody @Valid StopTime stopTime) throws BusApiException {
        try {
            this.stopTimeService.modifyStopTime(stopTime);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("批量修改站点时刻")
    @PutMapping("/batch")
    @Validated(value = {OnAdd.class})
    //@RequiresPermissions("stopTime:update")
    @ApiOperation(value = "批量修改", notes = "批量修改", tags = "站点时刻", httpMethod = "PUT")
    public void batchUpdateStopTime(@RequestBody @Valid List<StopTime> stopTimeList) throws BusApiException {
        this.stopTimeService.batchModifyStopTime(stopTimeList);
    }

    @Log("删除站点时刻")
    @DeleteMapping("/{stopTimeIds}")
    //@RequiresPermissions("stopTime:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "站点时刻", httpMethod = "DELETE")
    public void deleteStopTime(@NotBlank(message = "{required}") @PathVariable String stopTimeIds) throws BusApiException {
        try {
            String[] ids = stopTimeIds.split(StringPool.COMMA);
            this.stopTimeService.deleteStopTimes(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

//    @PostMapping("export")
//    //@RequiresPermissions("stopTime:export")
//    @ApiOperation(value = "导出", notes = "导出", tags = "站点时刻", httpMethod = "POST")
//    public void exportStopTime(QueryRequest request,@RequestBody StopTime stopTime, HttpServletResponse response) throws BusApiException{
//        try {
//            List<StopTime> stopTimes = this.stopTimeService.listStopTimes(request, stopTime).getRecords();
//            ExcelKit.$Export(StopTime.class, response).downXlsx(stopTimes, false);
//        } catch (Exception e) {
//            String message = "导出Excel失败";
//            log.error(message, e);
//            throw new BusApiException(message);
//        }
//    }
}
