package com.phlink.bus.api.bus.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.phlink.bus.api.common.controller.BaseController;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.dao.CameraLocationMapper;
import com.phlink.bus.api.bus.domain.CameraLocation;
import com.phlink.bus.api.bus.service.ICameraLocationService;
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
@RequestMapping("/camera-location")
@Api(tags = "DVR摄像头安装位置", hidden = true)
public class CameraLocationController extends BaseController {

    @Autowired
    public ICameraLocationService cameraLocationService;

    @GetMapping
    @ApiOperation(value = "列表", notes = "列表", tags = "DVR摄像头安装位置", httpMethod = "GET")
    public List<CameraLocation> listCameraLocation() {
        return cameraLocationService.list();
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("cameraLocation:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "DVR摄像头安装位置", httpMethod = "GET", hidden = true)
    public CameraLocation detail(@PathVariable Long id) {
        return this.cameraLocationService.findById(id);
    }

    @Log("添加DVR摄像头安装位置")
    @PostMapping
//    //@RequiresPermissions("cameraLocation:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "DVR摄像头安装位置", httpMethod = "POST", hidden = true)
    public void addCameraLocation(@RequestBody @Valid CameraLocation cameraLocation) throws BusApiException {
        try {
            this.cameraLocationService.createCameraLocation(cameraLocation);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改DVR摄像头安装位置")
    @PutMapping
//    //@RequiresPermissions("cameraLocation:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "DVR摄像头安装位置", httpMethod = "PUT", hidden = true)
    public void updateCameraLocation(@RequestBody @Valid CameraLocation cameraLocation) throws BusApiException {
        try {
            this.cameraLocationService.modifyCameraLocation(cameraLocation);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除DVR摄像头安装位置")
    @DeleteMapping("/{cameraLocationIds}")
//    //@RequiresPermissions("cameraLocation:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "DVR摄像头安装位置", httpMethod = "DELETE", hidden = true)
    public void deleteCameraLocation(@NotBlank(message = "{required}") @PathVariable String cameraLocationIds) throws BusApiException {
        try {
            String[] ids = cameraLocationIds.split(StringPool.COMMA);
            this.cameraLocationService.deleteCameraLocations(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
//    //@RequiresPermissions("cameraLocation:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "DVR摄像头安装位置", httpMethod = "POST", hidden = true)
    public void exportCameraLocation(QueryRequest request, @RequestBody CameraLocation cameraLocation, HttpServletResponse response) throws BusApiException {
        try {
            List<CameraLocation> cameraLocations = this.cameraLocationService.listCameraLocations(request, cameraLocation).getRecords();
            ExcelKit.$Export(CameraLocation.class, response).downXlsx(cameraLocations, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
