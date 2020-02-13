package com.phlink.bus.api.device.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.device.domain.DeviceRepair;
import com.phlink.bus.api.device.service.IDeviceRepairService;
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
 * @author zhouyi
 */
@Slf4j
@RestController
@RequestMapping("/device-repair")
@Api(tags = "设备维修表")
public class DeviceRepairController extends BaseController {

    @Autowired
    public IDeviceRepairService deviceRepairService;

    @GetMapping
    //@RequiresPermissions("deviceRepair:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "设备维修表", httpMethod = "GET")
    public Map<String, Object> ListDeviceRepair(QueryRequest request, DeviceRepair deviceRepair) {
        return getDataTable(this.deviceRepairService.listDeviceRepairs(request, deviceRepair));
    }

    @Log("添加设备维修")
    @PostMapping
    //@RequiresPermissions("deviceRepair:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "设备维修表", httpMethod = "POST")
    public void addDeviceRepair(@Valid DeviceRepair deviceRepair) throws BusApiException {
        try {
            this.deviceRepairService.createDeviceRepair(deviceRepair);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改设备维修")
    @PutMapping
    //@RequiresPermissions("deviceRepair:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "设备维修表", httpMethod = "PUT")
    public void updateDeviceRepair(@Valid DeviceRepair deviceRepair) throws BusApiException {
        try {
            this.deviceRepairService.modifyDeviceRepair(deviceRepair);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除设备维修")
    @DeleteMapping("/{deviceRepairIds}")
    //@RequiresPermissions("deviceRepair:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "设备维修表", httpMethod = "DELETE")
    public void deleteDeviceRepair(@NotBlank(message = "{required}") @PathVariable String deviceRepairIds) throws BusApiException {
        try {
            String[] ids = deviceRepairIds.split(StringPool.COMMA);
            this.deviceRepairService.deleteDeviceRepairIds(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("deviceRepair:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "设备维修表", httpMethod = "POST")
    public void exportDeviceRepair(QueryRequest request, DeviceRepair deviceRepair, HttpServletResponse response) throws BusApiException {
        try {
            List<DeviceRepair> deviceRepairs = this.deviceRepairService.listDeviceRepairs(request, deviceRepair).getRecords();
            ExcelKit.$Export(DeviceRepair.class, response).downXlsx(deviceRepairs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
