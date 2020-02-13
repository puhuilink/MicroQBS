package com.phlink.bus.api.bus.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.VO.*;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.annotation.DistributedLock;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnCheckID;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.excel.ExcelUtil;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.map.domain.enums.MapTypeEnum;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.map.service.IMapBaiduService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wen
 */
@Slf4j
@RestController
@RequestMapping("/bus")
@Api(tags = ApiTagsConstant.TAG_BUS)
@Validated
public class
BusController extends BaseController {

    @Autowired
    public IBusService busService;
    @Autowired
    private IMapBaiduService mapBaiduService;
    @Autowired
    private IMapAmapService mapAmapService;

    @GetMapping(value = "/list")
    //@RequiresPermissions("bus:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_BUS, httpMethod = "GET")
    public Map<String, Object> listBus(
            QueryRequest request,
            @ApiParam(name = "车辆对象", value = "传入json格式", required = true) BusViewVO busViewVO) {
        return getDataTable(this.busService.listBus(request, busViewVO));
    }

    @GetMapping("/{id}")
    //@RequiresPermissions("bus:get")
    @ApiOperation(value = "详情", notes = "详情", tags = ApiTagsConstant.TAG_BUS, httpMethod = "GET")
    public Bus getDetail(@PathVariable Long id) {
        return this.busService.findById(id);
    }

    @DistributedLock(prefix = "addBus")
    @Log("添加车辆")
    @Validated({OnAdd.class})
    @PostMapping(value = "/addBus")
    //@RequiresPermissions("bus:add")
    @ApiOperation(value = "添加车辆", notes = "添加车辆", tags = ApiTagsConstant.TAG_BUS, httpMethod = "POST")
    public void addBus(
            @RequestBody @Valid @ApiParam(name = "车辆对象", value = "传入json格式", required = true) Bus bus)
            throws BusApiException{
        this.busService.createBus(bus);
    }

    @Deprecated
    @Log("添加高德终端信息")
    @PutMapping(value = "/addAmapTerminal")
    //@RequiresPermissions("bus:addAmap")
    @ApiOperation(value = "添加高德终端信息", notes = "添加高德终端信息", tags = ApiTagsConstant.TAG_BUS, httpMethod = "PUT")
    public void addAmapTerminal(@RequestBody @Valid @ApiParam(name = "BusMapVO对象", value = "传入json格式", required = true)
                                        BusMapVO busMapVO)
            throws BusApiException {
        String busCode = busMapVO.getBusCode();
        Bus bus = busService.getByBusCode(busCode);
        if(bus == null) {
            throw new BusApiException("车辆不存在");
        }
        String numberPlate = busMapVO.getNumberPlate();
        // 注册高德终端
        long tid = this.mapAmapService.createAmapEntity(numberPlate, bus.getId().toString());
        this.busService.updateBusTerminalId(busCode, MapTypeEnum.AMAP, tid);
    }

    @Deprecated
    @Log("添加百度终端信息")
    @PutMapping(value = "/addBaiduTerminal")
    //@RequiresPermissions("bus:addBaidu")
    @ApiOperation(value = "添加百度终端信息", notes = "添加百度终端信息", tags = ApiTagsConstant.TAG_BUS, httpMethod = "PUT")
    public void addBaiduTerminal(
            @RequestBody @Valid @ApiParam(name = "BusMapVO对象", value = "传入json格式", required = true)
                    BusMapVO busMapVO)
            throws BusApiException {
        try {
            String busCode = busMapVO.getBusCode();
            String numberPlate = busMapVO.getNumberPlate();
            // 注册百度终端
            boolean bl = this.mapBaiduService.createBaiduEntity(numberPlate, busCode);
            if (!bl) {
                throw new BusApiException("添加百度");
            }
            this.busService.updateBusTerminalId(busCode, MapTypeEnum.BAIDU, 0);
        } catch (Exception e) {
            String message = "添加百度终端信息失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改车辆")
    @PutMapping
    @Validated({OnCheckID.class, OnUpdate.class})
    //@RequiresPermissions("bus:update")
    @ApiOperation(value = "修改车辆", notes = "修改车辆", tags = ApiTagsConstant.TAG_BUS, httpMethod = "PUT")
    public void updateBus(
            @RequestBody @Valid @ApiParam(name = "车辆对象", value = "传入json格式", required = true) Bus bus) throws BusApiException {
        Bus busCheck = busService.getById(bus.getId());
        if(busCheck == null) {
            throw new BusApiException("该车辆不存在");
        }
        this.busService.modifyBus(bus);
    }

    @Log("删除车辆")
    @DeleteMapping("/{busIds}")
    //@RequiresPermissions("bus:delete")
    @ApiOperation(value = "删除车辆", notes = "删除车辆", tags = ApiTagsConstant.TAG_BUS, httpMethod = "DELETE")
    public void deleteBus(@NotBlank(message = "{required}") @PathVariable String busIds)
            throws BusApiException {
        try {
            String[] ids = busIds.split(StringPool.COMMA);
            this.busService.deleteBusIds(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("bus:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_BUS, httpMethod = "POST")
    public void exportBus(
            @RequestBody @Valid @ApiParam(name = "车辆对象", value = "传入json格式", required = true) BusViewVO busViewVO,
            HttpServletResponse response)
            throws BusApiException {
        try {
            List<Bus> buss = this.busService.listBus(busViewVO);
            ExcelKit.$Export(Bus.class, response).downXlsx(buss, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @RequestMapping(value = "/down-template", method = RequestMethod.GET)
    public void downTemplate(HttpServletResponse response) {
        List<Bus> userList = busService.list();
        ExcelKit.$Export(Bus.class, response).downXlsx(userList, true);
    }

    @PostMapping("/import")
    //@RequiresPermissions("bus:add")
    @ApiOperation(value = "导入车辆信息", notes = "导入车辆信息", tags = ApiTagsConstant.TAG_BUS, httpMethod = "POST")
    public void importBus(@RequestParam(name = "file") MultipartFile file) {
        try {
            List<Bus> list = ExcelUtil.readExcel(Objects.requireNonNull(file.getOriginalFilename()), file.getInputStream(), Bus.class.getName(), 1);
            busService.batchCreateBus(list);
        }catch (Exception e) {
            log.error("导入失败", e);
        }
//        mapService.batchCreateBusEntity(list);
    }

    @GetMapping("/checkNumberPlate")
    //@RequiresPermissions("bus:checkNumberPlate")
    @ApiOperation(value = "车牌号唯一验证", notes = "车牌号唯一验证", tags = ApiTagsConstant.TAG_BUS, httpMethod = "GET")
    public boolean checkNumberPlate(@NotBlank(message = "{required}") @RequestParam(name = "numberPlate") String numberPlate) {
        return busService.checkNumberPlate(numberPlate);
    }

    @GetMapping("/school-bus-list")
    //@RequiresPermissions("bus:view")
    @ApiOperation(value = "学校车辆列表", notes = "学校车辆列表", tags = ApiTagsConstant.TAG_BUS, httpMethod = "GET")
    public List<SchoolBusListVO> listSchoolBus(@RequestParam(name = "numberPlate", required = false) String numberPlate) {
        return busService.listSchoolBus(numberPlate);
    }

    @GetMapping("/school-route-bus-list")
    //@RequiresPermissions("bus:view")
    @ApiOperation(value = "学校车辆列表", notes = "学校车辆列表", tags = ApiTagsConstant.TAG_BUS, httpMethod = "GET")
    public List<SchoolRouteBusListVO> listSchoolRouteBus(@RequestParam(name = "numberPlate", required = false) String numberPlate,
                                                         @RequestParam(name = "busCode", required = false) String busCode) {
        return busService.listSchoolRouteBus(numberPlate, busCode);
    }

    @GetMapping("/getUserAndBus")
    @ApiOperation(value = "用户和车辆信息", notes = "用户和车辆信息", tags = ApiTagsConstant.TAG_BUS, httpMethod = "GET")
    public Map<String, Object> getUserAndBus(@RequestParam(name = "mobile") String mobile) {
        UserBusVO vo = busService.getUserAndBus(mobile);
        if (vo != null) {
            return returnSuccess(vo);
        } else {
            return returnFail("查不到该用户的信息");
        }
    }

}
