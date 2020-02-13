package com.phlink.bus.api.bus.controller;


import com.phlink.bus.api.bus.domain.CameraLocation;
import com.phlink.bus.api.bus.domain.VO.DvrBusLocationInfoVO;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.phlink.bus.api.common.controller.BaseController;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.dao.DvrCameraConfigMapper;
import com.phlink.bus.api.bus.domain.DvrCameraConfig;
import com.phlink.bus.api.bus.service.IDvrCameraConfigService;
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
@Validated
@RestController
@Api(tags = "DVR摄像头配置信息")
public class DvrCameraConfigController extends BaseController {

    @Autowired
    public IDvrCameraConfigService dvrCameraConfigService;

    @GetMapping("/bus/{busId}/dvr-camera-config")
    @ApiOperation(value = "车辆的DVR配置列表", notes = "车辆的DVR配置列表", tags = "DVR摄像头配置信息", httpMethod = "GET")
    public List<DvrBusLocationInfoVO> listDvrCameraConfig(@PathVariable Long busId) {
        return this.dvrCameraConfigService.listDvrBusLocationInfo(busId);
    }

    @GetMapping("/bus/{busId}/guardian-dvr-camera")
    @ApiOperation(value = "家长可看的车辆的DVR摄像头信息", notes = "家长可看的车辆的DVR摄像头信息", tags = "DVR摄像头配置信息", httpMethod = "GET")
    public DvrBusLocationInfoVO getDvrCameraConfig(@PathVariable Long busId) {
        return this.dvrCameraConfigService.getDvrBusLocationInfoByGuardian(busId);
    }

    @GetMapping("/dvr-camera-config/{id}")
//    //@RequiresPermissions("dvrCameraConfig:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "DVR摄像头配置信息", httpMethod = "GET", hidden = true)
    public DvrCameraConfig detail(@PathVariable Long id) {
        return this.dvrCameraConfigService.findById(id);
    }

    @Log("添加DVR摄像头配置信息")
    @Validated(OnAdd.class)
    @PostMapping("/dvr-camera-config")
//    //@RequiresPermissions("dvrCameraConfig:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "DVR摄像头配置信息", httpMethod = "POST")
    public void addDvrCameraConfig(@RequestBody @Valid DvrCameraConfig dvrCameraConfig) throws BusApiException {
        this.dvrCameraConfigService.createDvrCameraConfig(dvrCameraConfig);
    }

    @Log("修改DVR摄像头配置信息")
    @PutMapping("/dvr-camera-config")
//    //@RequiresPermissions("dvrCameraConfig:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "DVR摄像头配置信息", httpMethod = "PUT")
    public void updateDvrCameraConfig(@RequestBody @Valid DvrCameraConfig dvrCameraConfig) throws BusApiException {
        try {
            this.dvrCameraConfigService.modifyDvrCameraConfig(dvrCameraConfig);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除DVR摄像头配置信息")
    @DeleteMapping("/dvr-camera-config/{dvrCameraConfigIds}")
//    //@RequiresPermissions("dvrCameraConfig:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "DVR摄像头配置信息", httpMethod = "DELETE")
    public void deleteDvrCameraConfig(@NotBlank(message = "{required}") @PathVariable String dvrCameraConfigIds) throws BusApiException {
        try {
            String[] ids = dvrCameraConfigIds.split(StringPool.COMMA);
            this.dvrCameraConfigService.deleteDvrCameraConfigs(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @GetMapping("/dvr-camera-config/{dvrCode}/channelCode-idle")
//    //@RequiresPermissions("dvrCameraConfig:get")
    @ApiOperation(value = "DVR设备闲置的通道", notes = "DVR设备闲置的通道", tags = "DVR摄像头配置信息", httpMethod = "GET")
    public List<Integer> channelCodeIdle(@PathVariable String dvrCode) throws BusApiException {
        return this.dvrCameraConfigService.listChannelCodeIdle(dvrCode);
    }

    @GetMapping("/dvr-camera-config/{dvrCode}/cameraLocation-idle")
//    //@RequiresPermissions("dvrCameraConfig:get")
    @ApiOperation(value = "DVR设备闲置的位置", notes = "DVR设备闲置的位置", tags = "DVR摄像头配置信息", httpMethod = "GET")
    public List<CameraLocation> cameraLocationIdle(@PathVariable String dvrCode) throws BusApiException {
        return this.dvrCameraConfigService.listCameraLocationIdle(dvrCode);
    }

}
