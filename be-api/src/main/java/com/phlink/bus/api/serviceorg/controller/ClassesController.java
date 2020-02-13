package com.phlink.bus.api.serviceorg.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnCheckID;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.serviceorg.domain.Classes;
import com.phlink.bus.api.serviceorg.domain.VO.ClassesViewVO;
import com.phlink.bus.api.serviceorg.service.IClassesService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


/**
 * @author zhouyi
 */
@Slf4j
@RestController
@RequestMapping("/classes")
@Api(tags = ApiTagsConstant.TAG_CLASSES)
public class ClassesController extends BaseController {

    @Autowired
    public IClassesService classesService;

    @GetMapping
    //@RequiresPermissions("classes:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_CLASSES, httpMethod = "GET")
    public Map<String, Object> listClasses(QueryRequest request, ClassesViewVO classesViewVO) {
        return  getDataTable(this.classesService.listClassess(request, classesViewVO));
    }

    @GetMapping("/{id}")
    //@RequiresPermissions("classes:get")
    @ApiOperation(value = "详情", notes = "详情", tags = ApiTagsConstant.TAG_CLASSES, httpMethod = "GET")
    public Classes detail(@NotBlank(message = "{required}") @RequestParam("id") Long id) {
        return this.classesService.findById(id);
    }

    @Log("添加班级")
    @Validated(value = {OnAdd.class})
    @PostMapping
    //@RequiresPermissions("classes:add")
    @ApiOperation(value = "添加", notes = "添加", tags = ApiTagsConstant.TAG_CLASSES, httpMethod = "POST")
    public void addClasses(@RequestBody @Valid Classes classes) throws BusApiException {
        this.classesService.createClasses(classes);
    }

    @Log("修改班级")
    @Validated(value = {OnUpdate.class, OnCheckID.class})
    @PutMapping
    //@RequiresPermissions("classes:update")
    @ApiOperation(value = "修改", notes = "修改", tags = ApiTagsConstant.TAG_CLASSES, httpMethod = "PUT")
    public void updateClasses(@RequestBody @Valid Classes classes) throws BusApiException {
        try {
            this.classesService.modifyClasses(classes);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除班级")
    @DeleteMapping("/{classIds}")
    //@RequiresPermissions("classes:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = ApiTagsConstant.TAG_CLASSES, httpMethod = "DELETE")
    public void deleteClasses(@NotBlank(message = "{required}") @PathVariable String classIds) throws BusApiException {
        try {
            String[] ids = classIds.split(StringPool.COMMA);
            this.classesService.deleteClassess(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("classes:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_CLASSES, httpMethod = "POST")
    public void exportClasses(@RequestBody @Valid ClassesViewVO classesViewVO, HttpServletResponse response) throws BusApiException {
        try {
            List<Classes> classess = this.classesService.listClassess(classesViewVO);
            ExcelKit.$Export(Classes.class, response).downXlsx(classess, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
