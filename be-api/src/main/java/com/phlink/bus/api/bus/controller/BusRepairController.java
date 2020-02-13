package com.phlink.bus.api.bus.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.phlink.bus.api.common.controller.BaseController;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.dao.BusRepairMapper;
import com.phlink.bus.api.bus.domain.BusRepair;
import com.phlink.bus.api.bus.service.IBusRepairService;
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
@RequestMapping("/bus-repair")
@Api(tags = "车辆报修信息")
public class BusRepairController extends BaseController {

    @Autowired
    public IBusRepairService busRepairService;

    @GetMapping
    //@RequiresPermissions("busRepair:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "", httpMethod = "GET")
    public Map<String, Object> ListBusRepair(QueryRequest request, BusRepair busRepair) {
        return getDataTable(this.busRepairService.listBusRepairs(request, busRepair));
    }

    @Log("添加车辆报修信息")
    @PostMapping
    //@RequiresPermissions("busRepair:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "", httpMethod = "POST")
    public void addBusRepair(@Valid BusRepair busRepair) throws BusApiException {
        try {
            this.busRepairService.createBusRepair(busRepair);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改车辆报修信息")
    @PutMapping
    //@RequiresPermissions("busRepair:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "", httpMethod = "PUT")
    public void updateBusRepair(@Valid BusRepair busRepair) throws BusApiException {
        try {
            this.busRepairService.modifyBusRepair(busRepair);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除车辆报修信息")
    @DeleteMapping("/{busRepairIds}")
    //@RequiresPermissions("busRepair:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "", httpMethod = "DELETE")
    public void deleteBusRepair(@NotBlank(message = "{required}") @PathVariable String busRepairIds) throws BusApiException {
        try {
            String[] ids = busRepairIds.split(StringPool.COMMA);
            this.busRepairService.deleteBusRepairIds(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("busRepair:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "", httpMethod = "POST")
    public void exportBusRepair(QueryRequest request, BusRepair busRepair, HttpServletResponse response) throws BusApiException {
        try {
            List<BusRepair> busRepairs = this.busRepairService.listBusRepairs(request, busRepair).getRecords();
            ExcelKit.$Export(BusRepair.class, response).downXlsx(busRepairs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
