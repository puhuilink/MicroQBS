package com.phlink.bus.api.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.system.domain.SysLog;
import com.phlink.bus.api.system.service.LogService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
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

@Slf4j
@Validated
@RestController
@RequestMapping("log")
@Api(tags ="系统日志")
public class LogController extends BaseController {

    @Autowired
    private LogService logService;

    @GetMapping
    //@RequiresPermissions("log:view")
    public Map<String, Object> logList(QueryRequest request, SysLog sysLog) {
        return getDataTable(logService.findLogs(request, sysLog));
    }

    @Log("删除系统日志")
    @DeleteMapping("/{ids}")
    //@RequiresPermissions("log:delete")
    public void deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) throws BusApiException {
        try {
            String[] logIds = ids.split(StringPool.COMMA);
            this.logService.deleteLogs(logIds);
        } catch (Exception e) {
            String message = "删除日志失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("excel")
    //@RequiresPermissions("log:export")
    public void export(QueryRequest request, @RequestBody @Valid SysLog sysLog, HttpServletResponse response) throws BusApiException {
        try {
            List<SysLog> sysLogs = this.logService.findLogs(request, sysLog).getRecords();
            ExcelKit.$Export(SysLog.class, response).downXlsx(sysLogs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
