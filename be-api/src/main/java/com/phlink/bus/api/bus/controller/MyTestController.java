package com.phlink.bus.api.bus.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.domain.MyTest;
import com.phlink.bus.api.bus.service.IMyTestService;
import com.phlink.bus.api.common.annotation.DistributedLock;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.service.FdfsStorageService;
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
import java.util.concurrent.TimeUnit;


/**
 * @author wen
 */
@Slf4j
@RestController
@RequestMapping("/my-test")
@Api(tags = "")
public class MyTestController extends BaseController {

    @Autowired
    public IMyTestService myTestService;
    @Autowired
    public FdfsStorageService fdfsStorageService;

    @GetMapping()
    //@RequiresPermissions("myTest:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "", httpMethod = "GET")
    public Map<String, Object> listMyTest(QueryRequest request, MyTest myTest) {
        return getDataTable(this.myTestService.listMyTests(request, myTest));
    }

    @GetMapping("/{id}")
    //@RequiresPermissions("myTest:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "", httpMethod = "GET")
    public MyTest detail(@NotBlank(message = "{required}") @RequestParam("id") Long id) {
        return this.myTestService.findById(id);
    }

    @DistributedLock(prefix = "addMyTest")
    @Log("添加")
    @PostMapping()
    //@RequiresPermissions("myTest:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "", httpMethod = "POST")
    public void addMyTest(@RequestBody @Valid MyTest myTest) throws BusApiException {
        try {
            TimeUnit.SECONDS.sleep(10);
            this.myTestService.createMyTest(myTest);
            log.info("ccccccccerrrrrrrreeeeaaaaattttt...........");
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改")
    @PutMapping()
    //@RequiresPermissions("myTest:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "", httpMethod = "PUT")
    public void updateMyTest(@RequestBody @Valid MyTest myTest) throws BusApiException {
        try {
            this.myTestService.modifyMyTest(myTest);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除")
    @DeleteMapping("/myTestIds")
    //@RequiresPermissions("myTest:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "", httpMethod = "DELETE")
    public void deleteMyTest(@NotBlank(message = "{required}") @PathVariable String myTestIds) throws BusApiException {
        try {
            String[] ids = myTestIds.split(StringPool.COMMA);
            this.myTestService.deleteMyTestIds(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("myTest:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "", httpMethod = "POST")
    public void exportMyTest(QueryRequest request, @RequestBody MyTest myTest, HttpServletResponse response) throws BusApiException {
        try {
            List<MyTest> myTests = this.myTestService.listMyTests(request, myTest).getRecords();
            ExcelKit.$Export(MyTest.class, response).downXlsx(myTests, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
