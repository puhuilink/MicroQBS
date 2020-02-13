package com.phlink.bus.api.alarm.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.alarm.domain.AlarmDevice;
import com.phlink.bus.api.alarm.domain.AlarmDeviceVO;
import com.phlink.bus.api.alarm.domain.enums.ProcessingStatusEnum;
import com.phlink.bus.api.alarm.service.IAlarmDeviceService;
import com.phlink.bus.api.alarm.service.IAlarmService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
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
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * @author zhouyi
 */
@Slf4j
@RestController
@RequestMapping("/alarm-device")
@Api(tags = ApiTagsConstant.TAG_ALARM_DEVICE)
public class AlarmDeviceController extends BaseController {

    @Autowired
    public IAlarmDeviceService alarmDeviceService;
    @Autowired
    public IAlarmService alarmService;

    @GetMapping
    //@RequiresPermissions("alarmDevice:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_ALARM_DEVICE, httpMethod = "GET")
    public Map<String, Object> listAlarmDevice(QueryRequest request, AlarmDeviceVO alarmDeviceVO) {
        return getDataTable(this.alarmDeviceService.listAlarmDevices(request, alarmDeviceVO));
    }

    @GetMapping("/{id}")
    //@RequiresPermissions("alarmDevice:get")
    @ApiOperation(value = "详情", notes = "详情", tags = ApiTagsConstant.TAG_ALARM_DEVICE, httpMethod = "GET")
    public AlarmDevice detail(@PathVariable Long id) {
        return this.alarmDeviceService.findById(id);
    }

    /*@Log("添加")
    @PostMapping
    //@RequiresPermissions("alarmDevice:add")
    @ApiOperation(value = "添加", notes = "添加", tags = ApiTagsConstant.TAG_ALARM_DEVICE, httpMethod = "POST")
    public void addAlarmDevice(@RequestBody @Valid AlarmDevice alarmDevice) throws BusApiException {
        try {
            this.alarmDeviceService.createAlarmDevice(alarmDevice);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改")
    @PutMapping
    //@RequiresPermissions("alarmDevice:update")
    @ApiOperation(value = "修改", notes = "修改", tags = ApiTagsConstant.TAG_ALARM_DEVICE, httpMethod = "PUT")
    public void updateAlarmDevice(@RequestBody @Valid AlarmDevice alarmDevice) throws BusApiException{
        try {
            this.alarmDeviceService.modifyAlarmDevice(alarmDevice);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }*/

    @Log("删除手环告警")
    @DeleteMapping("/{alarmDeviceIds}")
    //@RequiresPermissions("alarmDevice:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = ApiTagsConstant.TAG_ALARM_DEVICE, httpMethod = "DELETE")
    public void deleteAlarmDevice(@NotBlank(message = "{required}") @PathVariable String alarmDeviceIds) throws BusApiException {
        try {
            String[] ids = alarmDeviceIds.split(StringPool.COMMA);
            this.alarmDeviceService.deleteAlarmDevices(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("alarmDevice:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_ALARM_DEVICE, httpMethod = "POST")
    public void exportAlarmDevice(@RequestBody AlarmDeviceVO alarmDevice, HttpServletResponse response) throws BusApiException{
        try {
            List<AlarmDevice> alarmDevices = this.alarmDeviceService.listAlarmDevices(alarmDevice);
            ExcelKit.$Export(AlarmDevice.class, response).downXlsx(alarmDevices, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("人工处理设备告警")
    @PostMapping("/process/{alarmId}")
    //@RequiresPermissions("alarmDevice:process")
    @ApiOperation(value = "处理告警", notes = "处理告警", tags = ApiTagsConstant.TAG_ALARM, httpMethod = "POST")
    public Boolean processAlarm(@PathVariable Long alarmId) throws BusApiException {
        AlarmDevice alarmDevice = alarmDeviceService.findById(alarmId);
        if(alarmDevice == null) {
            throw new BusApiException("该告警不存在");
        }
        alarmDevice.setStatus(ProcessingStatusEnum.MANUAL);
        alarmDevice.setProcessTime(LocalDateTime.now());
        LocalDateTime createTime = alarmDevice.getCreateTime();
        LocalDateTime expireTime = createTime.plusHours(1);
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(expireTime)) {
            alarmDeviceService.pauseAlarm(alarmDevice.getAlarmType(), alarmDevice.getAlarmSubType(), alarmDevice.getDeviceCode(), now.plusHours(1));
        }
        alarmService.removeWebMessage(alarmDevice);
        return this.alarmDeviceService.updateById(alarmDevice);
    }
}
