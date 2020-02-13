package com.phlink.bus.api.attendance.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.phlink.bus.api.common.controller.BaseController;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.attendance.dao.BusAttendanceMapper;
import com.phlink.bus.api.attendance.domain.BusAttendance;
import com.phlink.bus.api.attendance.domain.vo.BusAttendanceVO;
import com.phlink.bus.api.attendance.service.IBusAttendanceService;
import com.phlink.bus.api.bus.domain.VO.UserBusVO;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/bus-attendance")
@Api(tags ="司乘打卡")
public class BusAttendanceController extends BaseController {

    @Autowired
    public IBusAttendanceService busAttendanceService;

    @GetMapping("/list")
//    //@RequiresPermissions("busAttendance:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "司乘打卡", httpMethod = "GET")
    public Map<String, Object> listBusAttendance(HttpServletRequest req, QueryRequest request, BusAttendanceVO busAttendance) {
        //return  getDataTable(this.busAttendanceService.listBusAttendances(request, busAttendance));
    	String onlyLogin = req.getHeader(BusApiConstant.UNIQUE_LOGIN);
    	if(onlyLogin !=null) {
    		busAttendance.setSource("1");
    	}else {
    		busAttendance.setSource("2");
    	}
    	return this.busAttendanceService.listBusAttendances(busAttendance);
    }

    @GetMapping("/{id}")
    //@RequiresPermissions("busAttendance:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "司乘打卡", httpMethod = "GET")
    public BusAttendance detail(@PathVariable Long id) {
        return this.busAttendanceService.findById(id);
    }
    
    @GetMapping("/role/{type}")
//    //@RequiresPermissions("busAttendance:get")
    @ApiOperation(value = "根据角色类型获取绑定的司机或者老师信息", notes = "根据角色类型获取绑定的司机或者老师信息", tags = "司乘打卡", httpMethod = "GET")
    public Map<String, Object> workerList(@PathVariable String type) {
        List<UserBusVO> vo = busAttendanceService.findDetalByRoleType(type);
        if(vo.size() > 0) {
        	return returnSuccess(vo);
        }else {
        	return returnFail("找不到信息");
        }
    }
    
    @GetMapping("/myAttendance")
//    //@RequiresPermissions("busAttendance:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "司乘打卡", httpMethod = "GET")
    public Map<String, Object> getMyAttInToday() {
        boolean flag =  this.busAttendanceService.getMyAttendance();
        return returnSuccess(flag);
    }

    @Log("添加司乘打卡")
    @PostMapping("/add")
//    //@RequiresPermissions("busAttendance:add")
    @ApiOperation(value = "添加司乘打卡", notes = "添加司乘打卡", tags = "司乘打卡", httpMethod = "POST")
    public void addBusAttendance(@RequestBody @Valid BusAttendance busAttendance) throws BusApiException {
        try {
            this.busAttendanceService.createBusAttendance(busAttendance);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusApiException(e.getMessage());
        }
    }
    
    @Log("添加司乘打卡")
    @PostMapping("/platform/add")
//    //@RequiresPermissions("busAttendance:add")
    @ApiOperation(value = "添加司乘打卡", notes = "添加司乘打卡", tags = "司乘打卡", httpMethod = "POST")
    public void addBusAttendanceByPlatform(@RequestBody @Valid BusAttendance busAttendance) throws BusApiException {
        try {
            this.busAttendanceService.createBusAttendanceInPlatform(busAttendance);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusApiException(e.getMessage());
        }
    }

    @Log("修改司乘打卡")
    @PutMapping
//    //@RequiresPermissions("busAttendance:update")
    @ApiOperation(value = "修改司乘打卡", notes = "修改司乘打卡", tags = "司乘打卡", httpMethod = "PUT")
    public void updateBusAttendance(@RequestBody @Valid BusAttendance busAttendance) throws BusApiException{
        try {
            this.busAttendanceService.modifyBusAttendance(busAttendance);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除司乘打卡")
    @DeleteMapping("/{busAttendanceIds}")
//    //@RequiresPermissions("busAttendance:delete")
    @ApiOperation(value = "删除司乘打卡", notes = "删除司乘打卡", tags = "司乘打卡", httpMethod = "DELETE")
    public void deleteBusAttendance(@NotBlank(message = "{required}") @PathVariable String busAttendanceIds) throws BusApiException{
        try {
            String[] ids = busAttendanceIds.split(StringPool.COMMA);
            this.busAttendanceService.deleteBusAttendances(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
//    //@RequiresPermissions("busAttendance:export")
    @ApiOperation(value = "司乘打卡导出", notes = "司乘打卡导出", tags = "", httpMethod = "POST")
    public void exportBusAttendance(HttpServletRequest req, @RequestBody BusAttendanceVO busAttendance, HttpServletResponse response) throws BusApiException{
        try {
            String onlyLogin = req.getHeader(BusApiConstant.UNIQUE_LOGIN);
            if(onlyLogin !=null) {
                busAttendance.setSource("1");
            }else {
                busAttendance.setSource("2");
            }
            Map<String, Object> busAttendances = this.busAttendanceService.listBusAttendances(busAttendance);
            List<BusAttendanceVO> vos = (List<BusAttendanceVO>) busAttendances.get("rows");
            ExcelKit.$Export(BusAttendanceVO.class, response).downXlsx(vos, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
