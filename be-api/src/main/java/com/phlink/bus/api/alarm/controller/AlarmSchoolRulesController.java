package com.phlink.bus.api.alarm.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.alarm.domain.AlarmSchoolRules;
import com.phlink.bus.api.alarm.domain.InvalidDateVO;
import com.phlink.bus.api.alarm.domain.SchoolSwitchVO;
import com.phlink.bus.api.alarm.service.IAlarmSchoolRulesService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.utils.BusApiUtil;
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
@RequestMapping("/alarm-school-rules")
@Api(tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES)
@Validated
public class AlarmSchoolRulesController extends BaseController {

    @Autowired
    public IAlarmSchoolRulesService alarmSchoolRulesService;

    @GetMapping
    //@RequiresPermissions("alarmSchoolRules:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "GET")
    public Map<String, Object> listAlarmSchoolRules(QueryRequest request, AlarmSchoolRules alarmSchoolRules) {
        return getDataTable(this.alarmSchoolRulesService.listAlarmSchoolRuless(request, alarmSchoolRules));
    }

    @GetMapping("/{id}")
    //@RequiresPermissions("alarmSchoolRules:get")
    @ApiOperation(value = "详情", notes = "详情", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "GET")
    public AlarmSchoolRules detail(@PathVariable Long id) {
        return this.alarmSchoolRulesService.findById(id);
    }

    @Log("添加学校告警规则")
    @PostMapping
    //@RequiresPermissions("alarmSchoolRules:add")
    @ApiOperation(value = "添加", notes = "添加", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "POST")
    public void addAlarmSchoolRules(@RequestBody @Valid AlarmSchoolRules alarmSchoolRules) throws BusApiException {
        try {
            alarmSchoolRules.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
            this.alarmSchoolRulesService.createAlarmSchoolRules(alarmSchoolRules);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改学校告警规则")
    @PutMapping
    //@RequiresPermissions("alarmSchoolRules:update")
    @ApiOperation(value = "修改", notes = "修改", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "PUT")
    public void updateAlarmSchoolRules(@RequestBody @Valid AlarmSchoolRules alarmSchoolRules) throws BusApiException, RedisConnectException {
        this.alarmSchoolRulesService.modifyAlarmSchoolRules(alarmSchoolRules);
    }

    @Log("取消告警失效时间")
    @PutMapping("/{alarmId}/cancel-invalid-date")
    //@RequiresPermissions("alarmSchoolRules:update")
    @ApiOperation(value = "取消告警失效时间", notes = "取消告警失效时间", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "PUT")
    public BusApiResponse cancelInvalidDate(@PathVariable Long alarmId){
        boolean isSuccess = this.alarmSchoolRulesService.cancelALarmInvalidDate(alarmId);
        return new BusApiResponse().data(isSuccess);
    }

    @Log("删除学校告警规则")
    @DeleteMapping("/{alarmSchoolRulesIds}")
    //@RequiresPermissions("alarmSchoolRules:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "DELETE")
    public void deleteAlarmSchoolRules(@NotBlank(message = "{required}") @PathVariable String alarmSchoolRulesIds) throws BusApiException {
        try {
            String[] ids = alarmSchoolRulesIds.split(StringPool.COMMA);
            this.alarmSchoolRulesService.deleteAlarmSchoolRuless(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("批量设置学校告警规则开关")
    @PutMapping("switch")
    //@RequiresPermissions("alarmSchoolRules:switch")
    @ApiOperation(value = "批量设置开关", notes = "批量设置开关", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "PUT")
    public void switchAlarmRouteRules(@RequestBody @Valid List<SchoolSwitchVO> schoolSwitchVOList) throws BusApiException {
        try {
            schoolSwitchVOList.stream().forEach(schoolSwitchVO -> this.alarmSchoolRulesService.switchAlarmRouteRules(schoolSwitchVO));
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("一键设置学校告警规则周末开关")
    @PutMapping("weekendSwitch")
    //@RequiresPermissions("alarmSchoolRules:weekendSwitch")
    @ApiOperation(value = "一键设置周末开关", notes = "一键设置周末开关", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "PUT")
    public void batchWeekendSwitch(Boolean weekendSwitch) throws BusApiException {
        try {
            if (weekendSwitch == null) {
                throw new BusApiException("weekendSwitch参数不能为空.");
            }
            this.alarmSchoolRulesService.batchWeekendSwitch(weekendSwitch);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("一键设置学校告警规则失效时间")
    @PutMapping("invalidDate")
    //@RequiresPermissions("alarmSchoolRules:invalidDate")
    @ApiOperation(value = "一键设置失效时间", notes = "一键设置失效时间", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "PUT")
    public void batchWeekendSwitch(@RequestBody @Valid InvalidDateVO invalidDateVO) throws BusApiException {
        try {
            this.alarmSchoolRulesService.batchInvalidDate(invalidDateVO);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("alarmSchoolRules:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_ALARM_SCHOOL_RULES, httpMethod = "POST")
    public void exportAlarmSchoolRules(QueryRequest request, @RequestBody AlarmSchoolRules alarmSchoolRules, HttpServletResponse response) throws BusApiException {
        try {
            List<AlarmSchoolRules> alarmSchoolRuless = this.alarmSchoolRulesService.listAlarmSchoolRuless(request, alarmSchoolRules).getRecords();
            ExcelKit.$Export(AlarmSchoolRules.class, response).downXlsx(alarmSchoolRuless, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
