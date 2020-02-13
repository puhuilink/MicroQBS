package com.phlink.bus.api.bus.controller;

import com.phlink.bus.api.bus.domain.VO.CheckoutSpecialVO;
import com.phlink.bus.api.bus.manager.CheckoutSpecialManager;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/checkout/special")
@Validated
@Api(tags = "特殊事项管理", hidden = true)
public class CheckoutSpecialController extends BaseController {

    @Autowired
    private CheckoutSpecialManager checkoutSpecialManager;

    @GetMapping("/morning")
//    //@RequiresPermissions("driverCheckoutEnd:view")
    @ApiOperation(value = "晨检特殊事项列表", notes = "晨检特殊事项列表", tags = "特殊事项", httpMethod = "GET")
    public Map<String, Object> listMorningCheckout(QueryRequest request, CheckoutSpecialVO checkoutSpecialVO) {
        return getDataTable(this.checkoutSpecialManager.listMorning(request, checkoutSpecialVO));
    }

    @Log("添加晨检特殊事项")
    @PostMapping("/morning")
//    //@RequiresPermissions("driverCheckoutEnd:view")
    @ApiOperation(value = "添加晨检特殊事项", notes = "晨检特殊事项列表", tags = "特殊事项", httpMethod = "POST")
    public void addMorningCheckout(@Valid @RequestBody CheckoutSpecialVO checkoutSpecialVO) throws BusApiException {
        checkoutSpecialManager.addMorningCheckout(checkoutSpecialVO);
    }

    @GetMapping("/evening")
//    //@RequiresPermissions("driverCheckoutEnd:view")
    @ApiOperation(value = "收车特殊事项列表", notes = "收车特殊事项列表", tags = "特殊事项", httpMethod = "GET")
    public Map<String, Object> listEveningCheckout(QueryRequest request, CheckoutSpecialVO checkoutSpecialVO) {
        return getDataTable(this.checkoutSpecialManager.listEvening(request, checkoutSpecialVO));
    }

    @Log("添加收车特殊事项")
    @PostMapping("/evening")
//    //@RequiresPermissions("driverCheckoutEnd:view")
    @ApiOperation(value = "添加收车特殊事项", notes = "添加收车特殊事项", tags = "特殊事项", httpMethod = "POST")
    public void addEveningCheckout(@Valid @RequestBody CheckoutSpecialVO checkoutSpecialVO) throws BusApiException {
        checkoutSpecialManager.addEveningCheckout(checkoutSpecialVO);
    }

}
