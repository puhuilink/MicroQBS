package com.phlink.bus.api.bus.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.domain.DriverCheckoutEnd;
import com.phlink.bus.api.bus.service.IDriverCheckoutEndService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/driver-checkout-end")
@Api(tags ="司机收车检查")
public class DriverCheckoutEndController extends BaseController {
    @Autowired
    public IDriverCheckoutEndService driverCheckoutEndService;
    @Autowired
    public UserService userService;

    @GetMapping
//    //@RequiresPermissions("driverCheckoutEnd:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "司机收车检查", httpMethod = "GET")
    public Map<String, Object> listDriverCheckoutEnd(QueryRequest request,
                                                     @RequestParam(required = false) String realname,
                                                     @RequestParam(required = false) String dateStart,
                                                     @RequestParam(required = false) String dateEnd,
                                                     @RequestParam(required = false) String mobile) {


        return getDataTable(this.driverCheckoutEndService.listDriverCheckoutEnds(request, realname, dateStart, dateEnd, mobile));
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("driverCheckoutEnd:get")
    @ApiOperation(value = "司机收车检查详情", notes = "司机收车检查详情", tags = "司机收车检查", httpMethod = "GET")
    public DriverCheckoutEnd detail(@PathVariable Long id) {
        return this.driverCheckoutEndService.findById(id);
    }

    @Log("添加司机收车检查")
    @PostMapping
//    //@RequiresPermissions("driverCheckoutEnd:add")
    @ApiOperation(value = "添加司机收车检查", notes = "添加司机收车检查", tags = "司机收车检查", httpMethod = "POST")
    public void addDriverCheckoutEnd(HttpServletRequest request,
                                     @RequestBody @Valid DriverCheckoutEnd driverCheckoutEnd) throws BusApiException {
        String onlyLogin = request.getHeader(BusApiConstant.UNIQUE_LOGIN);
        User user = null;
        if(StringUtils.isNotBlank(onlyLogin)) {
            // app
            user = BusApiUtil.getCurrentUser();
        }else{
            // 平台
            user = userService.getById(driverCheckoutEnd.getUserId());
        }
        if(user == null) {
            throw new BusApiException("用户信息不存在");
        }
        this.driverCheckoutEndService.createDriverCheckoutEnd(driverCheckoutEnd, user);
    }

    @Log("修改司机收车检查")
    @PutMapping
//    //@RequiresPermissions("driverCheckoutEnd:update")
    @ApiOperation(value = "修改司机收车检查", notes = "修改司机收车检查", tags = "司机收车检查", httpMethod = "PUT")
    public void updateDriverCheckoutEnd(@RequestBody @Valid DriverCheckoutEnd driverCheckoutEnd){
        this.driverCheckoutEndService.modifyDriverCheckoutEnd(driverCheckoutEnd);
    }

    @Log("删除司机收车检查")
    @DeleteMapping("/{driverCheckoutEndIds}")
//    //@RequiresPermissions("driverCheckoutEnd:delete")
    @ApiOperation(value = "删除司机收车检查", notes = "删除司机收车检查", tags = "司机收车检查", httpMethod = "DELETE")
    public void deleteDriverCheckoutEnd(@NotBlank(message = "{required}") @PathVariable String driverCheckoutEndIds) throws BusApiException{
        String[] ids = driverCheckoutEndIds.split(StringPool.COMMA);
        this.driverCheckoutEndService.deleteDriverCheckoutEnds(ids);
    }

    @PostMapping("export")
//    //@RequiresPermissions("driverCheckoutEnd:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "司机收车检查", httpMethod = "POST")
    public void exportDriverCheckoutEnd(@RequestParam(required = false) String realname,
                                        @RequestParam(required = false) String dateStart,
                                        @RequestParam(required = false) String dateEnd,
                                        @RequestParam(required = false) String mobile, HttpServletResponse response) throws BusApiException{
        List<DriverCheckoutEnd> driverCheckoutEnds = this.driverCheckoutEndService.listDriverCheckoutEnds(null, realname, dateStart, dateEnd, mobile).getRecords();
        ExcelKit.$Export(DriverCheckoutEnd.class, response).downXlsx(driverCheckoutEnds, false);
    }
}
