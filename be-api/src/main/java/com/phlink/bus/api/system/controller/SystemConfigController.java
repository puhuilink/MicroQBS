package com.phlink.bus.api.system.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.system.domain.SystemConfig;
import com.phlink.bus.api.system.service.ISystemConfigService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
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
@RequestMapping("/system-config")
@Api(tags ="系统配置")
public class SystemConfigController extends BaseController {

    @Autowired
    private ISystemConfigService systemConfigService;

    @GetMapping
    //@RequiresPermissions("system-config:view")
    public Map<String, Object> systemConfigList(QueryRequest queryRequest, SystemConfig systemConfig) {

        return getDataTable(systemConfigService.findSystemConfigs(queryRequest, systemConfig));
    }

    @Log("新增系统配置")
    @PostMapping
    //@RequiresPermissions("system-config:add")
    public void addDict(@RequestBody @Valid SystemConfig systemConfig) throws BusApiException {
        try {
            this.systemConfigService.createSystemConfig(systemConfig);
        } catch (Exception e) {
            String message = "新增系统配置成功";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }


    @Log("删除系统配置")
    @DeleteMapping("/{configIds}")
    //@RequiresPermissions("system-config:delete")
    public void deleteSystemConfigs(@NotBlank(message = "{required}") @PathVariable String configIds) throws BusApiException {
        try {
            String[] ids = configIds.split(StringPool.COMMA);
            this.systemConfigService.deleteSystemConfigs(ids);
        } catch (Exception e) {
            String message = "删除系统配置成功";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改系统配置")
    @PutMapping
    //@RequiresPermissions("system-config:update")
    public void updateDict(@RequestBody @Valid SystemConfig systemConfig) throws BusApiException {
        try {
            this.systemConfigService.updateSystemConfig(systemConfig);
        } catch (Exception e) {
            String message = "修改系统配置成功";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("excel")
    //@RequiresPermissions("system-config:export")
    public void export(QueryRequest request, @RequestBody @Valid SystemConfig systemConfig, HttpServletResponse response) throws BusApiException {
        try {
            List<SystemConfig> systemConfigList = this.systemConfigService.findSystemConfigs(request, systemConfig).getRecords();
            ExcelKit.$Export(SystemConfig.class, response).downXlsx(systemConfigList, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

}
