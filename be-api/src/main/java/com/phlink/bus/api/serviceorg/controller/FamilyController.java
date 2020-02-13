package com.phlink.bus.api.serviceorg.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.phlink.bus.api.common.controller.BaseController;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.serviceorg.dao.FamilyMapper;
import com.phlink.bus.api.serviceorg.domain.Family;
import com.phlink.bus.api.serviceorg.service.IFamilyService;
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
 * 暂不提供服务
* @author wen
*/
@Slf4j
//@RestController
//@RequestMapping("/family")
//@Api(tags ="家庭信息")
public class FamilyController extends BaseController {

    @Autowired
    public IFamilyService familyService;

    @GetMapping("/{id}")
    //@RequiresPermissions("family:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "家庭信息", httpMethod = "GET")
    public Family detail(@PathVariable Long id) {
        return this.familyService.findById(id);
    }

    @Log("添加")
    @PostMapping
    //@RequiresPermissions("family:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "家庭信息", httpMethod = "POST")
    public void addFamily(@RequestBody @Valid Family family) throws BusApiException {
        try {
            this.familyService.createFamily(family);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改")
    @PutMapping
    //@RequiresPermissions("family:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "家庭信息", httpMethod = "PUT")
    public void updateFamily(@RequestBody @Valid Family family) throws BusApiException{
        try {
            this.familyService.modifyFamily(family);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除")
    @DeleteMapping("/{familyIds}")
    //@RequiresPermissions("family:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "家庭信息", httpMethod = "DELETE")
    public void deleteFamily(@NotBlank(message = "{required}") @PathVariable String familyIds) throws BusApiException{
        try {
            String[] ids = familyIds.split(StringPool.COMMA);
            this.familyService.deleteFamilys(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
