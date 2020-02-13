package com.phlink.bus.api.device.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.device.domain.DeviceRelation;
import com.phlink.bus.api.device.service.IDeviceRelationService;
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
@RequestMapping("/device-relation")
@Api(tags = "设备信息关系表")
public class DeviceRelationController extends BaseController {

    @Autowired
    public IDeviceRelationService deviceRelationService;

    @GetMapping
    //@RequiresPermissions("deviceRelation:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "设备信息关系表", httpMethod = "GET")
    public Map<String, Object> ListDeviceRelation(QueryRequest request, DeviceRelation deviceRelation) {
        return getDataTable(this.deviceRelationService.listDeviceRelations(request, deviceRelation));
    }

    @Log("添加设备关系")
    @PostMapping
    //@RequiresPermissions("deviceRelation:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "设备信息关系表", httpMethod = "POST")
    public void addDeviceRelation(@Valid DeviceRelation deviceRelation) throws BusApiException {
        try {
            this.deviceRelationService.createDeviceRelation(deviceRelation);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改设备关系")
    @PutMapping
    //@RequiresPermissions("deviceRelation:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "设备信息关系表", httpMethod = "PUT")
    public void updateDeviceRelation(@Valid DeviceRelation deviceRelation) throws BusApiException {
        try {
            this.deviceRelationService.modifyDeviceRelation(deviceRelation);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除设备关系")
    @DeleteMapping("/{deviceRelationIds}")
    //@RequiresPermissions("deviceRelation:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "设备信息关系表", httpMethod = "DELETE")
    public void deleteDeviceRelation(@NotBlank(message = "{required}") @PathVariable String deviceRelationIds) throws BusApiException {
        try {
            String[] ids = deviceRelationIds.split(StringPool.COMMA);
            this.deviceRelationService.deleteDeviceRelationIds(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("deviceRelation:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "设备信息关系表", httpMethod = "POST")
    public void exportDeviceRelation(QueryRequest request, DeviceRelation deviceRelation, HttpServletResponse response) throws BusApiException {
        try {
            List<DeviceRelation> deviceRelations = this.deviceRelationService.listDeviceRelations(request, deviceRelation).getRecords();
            ExcelKit.$Export(DeviceRelation.class, response).downXlsx(deviceRelations, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
