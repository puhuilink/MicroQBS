package com.phlink.bus.api.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.system.domain.Dict;
import com.phlink.bus.api.system.service.DictService;
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
@RequestMapping("dict")
@Api(tags ="系统字典")
public class DictController extends BaseController {

    @Autowired
    private DictService dictService;

    @GetMapping
    //@RequiresPermissions("dict:view")
    public Map<String, Object> DictList(QueryRequest request, Dict dict) {
        return getDataTable(this.dictService.findDicts(request, dict));
    }

    @Log("新增字典")
    @PostMapping
    //@RequiresPermissions("dict:add")
    public void addDict(@RequestBody @Valid Dict dict) throws BusApiException {
        try {
            this.dictService.createDict(dict);
        } catch (Exception e) {
            String message = "新增字典成功";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除字典")
    @DeleteMapping("/{dictIds}")
    //@RequiresPermissions("dict:delete")
    public void deleteDicts(@NotBlank(message = "{required}") @PathVariable String dictIds) throws BusApiException {
        try {
            String[] ids = dictIds.split(StringPool.COMMA);
            this.dictService.deleteDicts(ids);
        } catch (Exception e) {
            String message = "删除字典成功";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改字典")
    @PutMapping
    //@RequiresPermissions("dict:update")
    public void updateDict(@RequestBody @Valid Dict dict) throws BusApiException {
        try {
            this.dictService.updateDict(dict);
        } catch (Exception e) {
            String message = "修改字典成功";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("excel")
    //@RequiresPermissions("dict:export")
    public void export(QueryRequest request, @RequestBody @Valid Dict dict, HttpServletResponse response) throws BusApiException {
        try {
            List<Dict> dicts = this.dictService.findDicts(request, dict).getRecords();
            ExcelKit.$Export(Dict.class, response).downXlsx(dicts, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
