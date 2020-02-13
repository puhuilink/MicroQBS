package com.phlink.bus.api.serviceorg.controller;


import org.springframework.web.bind.annotation.RequestMapping;

    import org.springframework.web.bind.annotation.RestController;
    import com.phlink.bus.api.common.controller.BaseController;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.serviceorg.dao.CalendarMapper;
import com.phlink.bus.api.serviceorg.domain.Calendar;
import com.phlink.bus.api.serviceorg.service.ICalendarService;
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
@RequestMapping("/calendar")
@Api(tags ="每周日程表")
public class CalendarController extends BaseController {

    @Autowired
    public ICalendarService calendarService;

    @GetMapping
    //@RequiresPermissions("calendar:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "每周日程表", httpMethod = "GET")
    public Map<String, Object> ListCalendar(QueryRequest request,Calendar calendar) {
        return  getDataTable(this.calendarService.listCalendars(request, calendar));
    }

    @GetMapping("/{id}")
    //@RequiresPermissions("calendar:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "每周日程表", httpMethod = "GET")
    public Calendar detail(@NotBlank(message = "{required}") @PathVariable Long id) {
        return this.calendarService.findById(id);
    }

    @Log("添加每周日程表")
    @PostMapping
    //@RequiresPermissions("calendar:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "每周日程表", httpMethod = "POST")
    public void addCalendar(@RequestBody @Valid Calendar calendar) throws BusApiException {
        try {
            this.calendarService.createCalendar(calendar);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改每周日程表")
    @PutMapping
    //@RequiresPermissions("calendar:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "每周日程表", httpMethod = "PUT")
    public void updateCalendar(@RequestBody @Valid Calendar calendar) throws BusApiException{
        try {
            this.calendarService.modifyCalendar(calendar);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除每周日程表")
    @DeleteMapping("/calendarIds")
    //@RequiresPermissions("calendar:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "", httpMethod = "DELETE")
    public void deleteCalendar(@NotBlank(message = "{required}") @PathVariable String calendarIds) throws BusApiException{
        try {
            String[] ids = calendarIds.split(StringPool.COMMA);
    this.calendarService.deleteCalendars(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("calendar:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "每周日程表", httpMethod = "POST")
    public void exportCalendar(QueryRequest request,@RequestBody Calendar calendar, HttpServletResponse response) throws BusApiException{
        try {
            List<Calendar> calendars = this.calendarService.listCalendars(request, calendar).getRecords();
            ExcelKit.$Export(Calendar.class, response).downXlsx(calendars, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
