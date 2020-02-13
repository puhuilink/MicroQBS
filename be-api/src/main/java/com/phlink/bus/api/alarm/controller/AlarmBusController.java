package com.phlink.bus.api.alarm.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.alarm.domain.AlarmBus;
import com.phlink.bus.api.alarm.domain.AlarmBusVO;
import com.phlink.bus.api.alarm.domain.enums.ProcessingStatusEnum;
import com.phlink.bus.api.alarm.service.IAlarmBusService;
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
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * @author zhouyi
 */
@Slf4j
@RestController
@RequestMapping("/alarm-bus")
@Api(tags = ApiTagsConstant.TAG_ALARM_BUS)
public class AlarmBusController extends BaseController {

    @Autowired
    public IAlarmBusService alarmBusService;
    @Autowired
    public IAlarmService alarmService;

    @GetMapping
    //@RequiresPermissions("alarmBus:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_ALARM_BUS, httpMethod = "GET")
    public Map<String, Object> listAlarmBus(QueryRequest request, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) AlarmBusVO alarmBusVO) {
        return getDataTable(this.alarmBusService.listAlarmBus(request, alarmBusVO));
    }

    @GetMapping("/{id}")
    //@RequiresPermissions("alarmBus:get")
    @ApiOperation(value = "详情", notes = "详情", tags = ApiTagsConstant.TAG_ALARM_BUS, httpMethod = "GET")
    public AlarmBus detail(@PathVariable Long id) {
        return this.alarmBusService.findById(id);
    }

    @Log("删除车辆告警")
    @DeleteMapping("/{alarmBusIds}")
    //@RequiresPermissions("alarmBus:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = ApiTagsConstant.TAG_ALARM_BUS, httpMethod = "DELETE")
    public void deleteAlarmBus(@NotBlank(message = "{required}") @PathVariable String alarmBusIds) throws BusApiException {
        try {
            String[] ids = alarmBusIds.split(StringPool.COMMA);
            this.alarmBusService.deleteAlarmBuss(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("alarmBus:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_ALARM_BUS, httpMethod = "POST")
    public void exportAlarmBus(@RequestBody AlarmBusVO alarmBus, HttpServletResponse response) throws BusApiException {
        try {
            List<AlarmBus> alarmBuss = this.alarmBusService.listAlarmBus(alarmBus);
            ExcelKit.$Export(AlarmBus.class, response).downXlsx(alarmBuss, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(LocalDate.class, new CustomDateEditor(dateFormat, true));
    }

    @Log("人工处理车辆告警")
    @PostMapping("/process/{alarmId}")
    //@RequiresPermissions("alarmDevice:process")
    @ApiOperation(value = "人工处理车辆告警", notes = "人工处理车辆告警", tags = ApiTagsConstant.TAG_ALARM, httpMethod = "POST")
    public Boolean processAlarm(@PathVariable Long alarmId) throws BusApiException {
        AlarmBus alarmBus = alarmBusService.findById(alarmId);
        if (alarmBus == null) {
            throw new BusApiException("该告警不存在");
        }
        alarmBus.setStatus(ProcessingStatusEnum.MANUAL);
        alarmBus.setProcessTime(LocalDateTime.now());
        LocalDateTime createTime = alarmBus.getCreateTime();
        LocalDateTime expireTime = createTime.plusHours(1);
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(expireTime)) {
            alarmBusService.pauseAlarm(alarmBus.getAlarmType(), alarmBus.getAlarmSubType(), alarmBus.getBusCode(), now.plusHours(1));
        }
        alarmService.removeWebMessage(alarmBus);
        return this.alarmBusService.updateById(alarmBus);
    }
}
