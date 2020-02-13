package com.phlink.bus.api.alarm.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.domain.InvalidDateVO;
import com.phlink.bus.api.alarm.domain.RouteSwitchVO;
import com.phlink.bus.api.alarm.service.IAlarmRouteRulesService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


/**
 * @author ZHOUY
 */
@Slf4j
@RestController
@RequestMapping("/alarm-route-rules")
@Api(tags = ApiTagsConstant.TAG_ALARM_ROUTE_RULES)
@Validated
public class AlarmRouteRulesController extends BaseController {

    @Autowired
    public IAlarmRouteRulesService alarmRouteRulesService;

    @GetMapping
    //@RequiresPermissions("alarmRouteRules:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_ALARM_ROUTE_RULES, httpMethod = "GET")
    public Map<String, Object> listAlarmRouteRules(QueryRequest request, AlarmRouteRules alarmRouteRules) {
        return getDataTable(this.alarmRouteRulesService.listAlarmRouteRuless(request, alarmRouteRules));
    }

    @GetMapping("/{id}")
    //@RequiresPermissions("alarmRouteRules:get")
    @ApiOperation(value = "详情", notes = "详情", tags = ApiTagsConstant.TAG_ALARM_ROUTE_RULES, httpMethod = "GET")
    public AlarmRouteRules detail(@PathVariable Long id) {
        return this.alarmRouteRulesService.findById(id);
    }

    @Log("添加路线告警规则")
    @PostMapping
    //@RequiresPermissions("alarmRouteRules:add")
    @ApiOperation(value = "添加", notes = "添加", tags = ApiTagsConstant.TAG_ALARM_ROUTE_RULES, httpMethod = "POST")
    public void addAlarmRouteRules(@RequestBody @Valid AlarmRouteRules alarmRouteRules) throws BusApiException {
        try {
            this.alarmRouteRulesService.createAlarmRouteRules(alarmRouteRules);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改路线告警规则")
    @PutMapping
    //@RequiresPermissions("alarmRouteRules:update")
    @ApiOperation(value = "修改", notes = "修改", tags = ApiTagsConstant.TAG_ALARM_ROUTE_RULES, httpMethod = "PUT")
    public void updateAlarmRouteRules(@RequestBody @Valid AlarmRouteRules alarmRouteRules) throws BusApiException {
        try {
            this.alarmRouteRulesService.modifyAlarmRouteRules(alarmRouteRules);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("取消告警失效时间")
    @PutMapping("/{alarmId}/cancel-invalid-date")
    //@RequiresPermissions("alarmSchoolRules:update")
    @ApiOperation(value = "取消告警失效时间", notes = "取消告警失效时间", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "PUT")
    public BusApiResponse cancelInvalidDate(@PathVariable Long alarmId){
        boolean isSuccess = this.alarmRouteRulesService.cancelALarmInvalidDate(alarmId);
        return new BusApiResponse().data(isSuccess);
    }

    @Log("删除路线告警规则")
    @DeleteMapping("/{alarmRouteRulesIds}")
    //@RequiresPermissions("alarmRouteRules:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = ApiTagsConstant.TAG_ALARM_ROUTE_RULES, httpMethod = "DELETE")
    public void deleteAlarmRouteRules(@NotBlank(message = "{required}") @PathVariable String alarmRouteRulesIds) throws BusApiException {
        try {
            String[] ids = alarmRouteRulesIds.split(StringPool.COMMA);
            this.alarmRouteRulesService.deleteAlarmRouteRuless(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("批量设置路线告警规则开关")
    @PutMapping("switch")
    //@RequiresPermissions("alarmRouteRules:switch")
    @ApiOperation(value = "批量设置开关", notes = "批量设置开关", tags = ApiTagsConstant.TAG_ALARM_ROUTE_RULES, httpMethod = "PUT")
    public void switchAlarmRouteRules(@RequestBody @Valid List<RouteSwitchVO> routeSwitchVOList) throws BusApiException {
        try {
            routeSwitchVOList.stream().forEach(routeSwitchVO -> this.alarmRouteRulesService.switchAlarmRouteRules(routeSwitchVO));
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("一键设置路线告警规则周末开关")
    @PutMapping("weekendSwitch")
    //@RequiresPermissions("alarmRouteRules:weekendSwitch")
    @ApiOperation(value = "一键设置周末开关", notes = "一键设置周末开关", tags = ApiTagsConstant.TAG_ALARM_ROUTE_RULES, httpMethod = "PUT")
    public void batchWeekendSwitch(Boolean weekendSwitch) throws BusApiException {
        try {
            if (weekendSwitch == null) {
                throw new BusApiException("weekendSwitch参数不能为空.");
            }
            this.alarmRouteRulesService.batchWeekendSwitch(weekendSwitch);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("一键设置路线告警规则失效时间")
    @PutMapping("invalidDate")
    //@RequiresPermissions("alarmRouteRules:invalidDate")
    @ApiOperation(value = "一键设置失效时间", notes = "一键设置失效时间", tags = ApiTagsConstant.TAG_ALARM_ROUTE_RULES, httpMethod = "PUT")
    public void batchWeekendSwitch(@RequestBody @Valid InvalidDateVO invalidDateVO) throws BusApiException {
        try {
            this.alarmRouteRulesService.batchInvalidDate(invalidDateVO);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    //@RequiresPermissions("alarmRouteRules:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_ALARM_ROUTE_RULES, httpMethod = "POST")
    public void exportAlarmRouteRules(QueryRequest request, @RequestBody AlarmRouteRules alarmRouteRules, HttpServletResponse response) throws BusApiException {
        try {
            List<AlarmRouteRules> alarmRouteRuless = this.alarmRouteRulesService.listAlarmRouteRuless(request, alarmRouteRules).getRecords();
            ExcelKit.$Export(AlarmRouteRules.class, response).downXlsx(alarmRouteRuless, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
