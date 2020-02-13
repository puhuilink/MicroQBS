package com.phlink.bus.api.bus.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.phlink.bus.api.common.controller.BaseController;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.dao.BusWorkerMapper;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.BusWorker;
import com.phlink.bus.api.bus.service.IBusWorkerService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/bus-worker")
@Api(tags = "车辆司乘人员绑定信息")
public class BusWorkerController extends BaseController {

    @Autowired
    public IBusWorkerService busWorkerService;

    @GetMapping
    //@RequiresPermissions("busWorker:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "车辆司乘人员绑定信息", httpMethod = "GET")
    public Map<String, Object> ListBusWorker(QueryRequest request, BusWorker busWorker) {
        return getDataTable(this.busWorkerService.listBusWorkers(request, busWorker));
    }
    
    @GetMapping("/mybus")
//    //@RequiresPermissions("busWorker:view")
    @ApiOperation(value = "详情", notes = "详情", tags = "车辆司乘人员所绑定的车辆信息", httpMethod = "GET")
    public Bus findBusByUserId() {
        return this.busWorkerService.findMyBus();
    }


    @Log("添加车辆司乘人员绑定信息")
    @PostMapping
    //@RequiresPermissions("busWorker:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "车辆司乘人员绑定信息", httpMethod = "POST")
    public void addBusWorker(@Valid BusWorker busWorker) throws BusApiException {
        try {
            this.busWorkerService.createBusWorker(busWorker);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改车辆司乘人员绑定信息")
    @PutMapping
    //@RequiresPermissions("busWorker:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "车辆司乘人员绑定信息", httpMethod = "PUT")
    public void updateBusWorker(@Valid BusWorker busWorker) throws BusApiException {
        try {
            this.busWorkerService.modifyBusWorker(busWorker);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除车辆司乘人员绑定信息")
    @DeleteMapping("/{busWorkerIds}")
    //@RequiresPermissions("busWorker:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "车辆司乘人员绑定信息", httpMethod = "DELETE")
    public void deleteBusWorker(@NotBlank(message = "{required}") @PathVariable String busWorkerIds) throws BusApiException {
        try {
            String[] ids = busWorkerIds.split(StringPool.COMMA);
            this.busWorkerService.deleteBusWorkerIds(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("busWorker:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "车辆司乘人员绑定信息", httpMethod = "POST")
    public void exportBusWorker(QueryRequest request, BusWorker busWorker, HttpServletResponse response) throws BusApiException {
        try {
            List<BusWorker> busWorkers = this.busWorkerService.listBusWorkers(request, busWorker).getRecords();
            ExcelKit.$Export(BusWorker.class, response).downXlsx(busWorkers, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
