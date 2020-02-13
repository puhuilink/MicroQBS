package com.phlink.bus.api.bus.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.dao.BusMapper;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DriverCheckout;
import com.phlink.bus.api.bus.service.IDriverCheckoutService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


/**
* @author wen
*/
@Slf4j
@RestController
@RequestMapping("/driver-checkout")
@Api(tags ="司机晨检")
public class DriverCheckoutController extends BaseController {

    @Autowired
    public IDriverCheckoutService driverCheckoutService;
    
    @Autowired
    private BusMapper busMapper;

    @GetMapping("/get")
//    //@RequiresPermissions("driverCheckout:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "", httpMethod = "GET")
    public DriverCheckout ListDriverCheckout() {
        //return  getDataTable(this.driverCheckoutService.listDriverCheckouts(request, driverCheckout));
    	Long userId = BusApiUtil.getCurrentUser().getUserId();
    	return this.driverCheckoutService.findById(userId);
    }
    
    @GetMapping("/list")
//  //@RequiresPermissions("teacherCheckout:view")
    @ApiOperation(value = "查询列表", notes = "查询列表", tags = "", httpMethod = "GET")
	public Map<String, Object> listAllTeacherCheckout( QueryRequest request,DriverCheckout driverCheckout) {
	    return  getDataTable(this.driverCheckoutService.listDriverCheckouts(request, driverCheckout));
	}

    @GetMapping("/{id}")
//    //@RequiresPermissions("driverCheckout:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "", httpMethod = "GET")
    public DriverCheckout detail(@PathVariable Long id) {
        return this.driverCheckoutService.findById(id);
    }

    @Log("添加司机晨检")
    @PostMapping("/add")
//    @RequiresPermissions("driverCheckout:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "", httpMethod = "POST")
    public BusApiResponse addDriverCheckout(@RequestBody @Valid DriverCheckout driverCheckout) throws BusApiException {
        Long userId = BusApiUtil.getCurrentUser().getUserId();
        //查询当前用户今天是否已经进行过晨检
        DriverCheckout check = this.driverCheckoutService.findIsCheckout(userId);
        if(check != null) {
            String message = "当前用户已完成今日晨检";
            log.info(message);
            throw new BusApiException(message);
        }
        //根据当前用户查询到绑定的车牌号
        Bus bus = busMapper.getBusByWorkerId(userId);
        if(bus == null) {
            String message = "未找到该用户绑定的车辆信息";
            log.info(message);
            throw new BusApiException(message);
        }
        String mobile = BusApiUtil.getCurrentUser().getMobile();
        String username = BusApiUtil.getCurrentUser().getRealname();
        driverCheckout.setMobile(mobile);
        driverCheckout.setUserId(userId);
        driverCheckout.setUserName(username);

        if(driverCheckout.getTime() == null) {
            driverCheckout.setTime(LocalDate.now());
        }else{
            driverCheckout.setTime(driverCheckout.getTime());
        }
        driverCheckout.setNumberPlate(bus.getNumberPlate());
        this.driverCheckoutService.createDriverCheckout(driverCheckout);
        return new BusApiResponse().message("success");
    }
    
    @Log("管理平台添加司机晨检")
    @PostMapping("/platform/add")
//    @RequiresPermissions("driverCheckout:add")
    @ApiOperation(value = "管理平台添加", notes = "管理平台添加", tags = "", httpMethod = "POST")
    public Map<String, Object> addTeacherCheckoutOnPlateform(@RequestBody @Valid DriverCheckout driverCheckout){
    	boolean flag = this.driverCheckoutService.createDriverCheckoutOnPlatform(driverCheckout);
        if(flag) {
        	return returnSuccess("添加成功");
        }else {
        	return returnFail("添加失败");
        }
    }

    @Log("修改司机晨检")
    @PutMapping("/update/{id}")
//    //@RequiresPermissions("driverCheckout:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "", httpMethod = "PUT")
    public void updateDriverCheckout(@RequestBody @Valid DriverCheckout driverCheckout) throws BusApiException{
        try {
            this.driverCheckoutService.modifyDriverCheckout(driverCheckout);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除司机晨检")
    @DeleteMapping("/{driverCheckoutIds}")
//    //@RequiresPermissions("driverCheckout:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "", httpMethod = "DELETE")
    public void deleteDriverCheckout(@NotBlank(message = "{required}") @PathVariable String driverCheckoutIds) throws BusApiException{
        try {
            String[] ids = driverCheckoutIds.split(StringPool.COMMA);
            this.driverCheckoutService.deleteDriverCheckouts(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("driverCheckout:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "", httpMethod = "POST")
    public void exportDriverCheckout(@RequestBody DriverCheckout driverCheckout, HttpServletResponse response) throws BusApiException{
        try {
            List<DriverCheckout> driverCheckouts = this.driverCheckoutService.listDriverCheckouts(driverCheckout);
            ExcelKit.$Export(DriverCheckout.class, response).downXlsx(driverCheckouts, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
