package com.phlink.bus.api.device.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.DistributedLock;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.device.domain.VO.BindingDeviceVO;
import com.phlink.bus.api.device.domain.VO.DeviceCountAll;
import com.phlink.bus.api.device.domain.VO.DeviceViewVO;
import com.phlink.bus.api.device.manager.DeviceManager;
import com.phlink.bus.api.device.manager.IotManager;
import com.phlink.bus.api.device.service.IDeviceService;
import com.phlink.bus.api.map.service.IMapService;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.domain.User;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

/**
 * @author zy
 */
@Slf4j
@RestController
@RequestMapping("/device")
@Api(tags = "设备管理")
@Validated
public class DeviceController extends BaseController {

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IMapService mapService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IotManager iotManager;
    @Autowired
    private IGuardianService guardianService;
    @Autowired
    private DeviceManager deviceManager;

    @GetMapping
    //@RequiresPermissions("device:view")
    @ApiOperation(value = "设备列表", notes = "设备列表", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "GET")
    public Map<String, Object> listDevice(QueryRequest request, @Valid DeviceViewVO deviceViewVO) {
        return getDataTable(this.deviceService.listDevices(request, deviceViewVO));
    }

    @DistributedLock(prefix = "addDevice")
    @Log("添加设备")
    @PostMapping
    //@RequiresPermissions("device:add")
    @ApiOperation(value = "添加设备", notes = "添加设备", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "POST")
    public void addDevice(@RequestBody @Valid Device device) throws BusApiException, RedisConnectException {
        this.deviceService.createDevice(device);
    }

    @DistributedLock(prefix = "bindingDevice")
    @Log("设备绑定")
    @PostMapping("binding")
    //@RequiresPermissions("device:bind")
    @ApiOperation(value = "绑定设备", notes = "绑定设备", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "POST")
    public void bindingDevice(@RequestBody @Valid BindingDeviceVO bindingDeviceVO) throws BusApiException {
        //检查设备是否存在
        Device device = deviceService.findByDeviceCode(bindingDeviceVO.getDeviceCode());
        if (device == null) {
            throw new BusApiException("设备不存在");
        }
        this.deviceService.bindingDevice(bindingDeviceVO);
    }

    @Log("设备解绑")
    @PostMapping("unbinding")
    //@RequiresPermissions("device:bind")
    @ApiOperation(value = "解绑设备", notes = "解绑设备", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "POST")
    public void unbindingDevice(@RequestBody @Valid BindingDeviceVO bindingDeviceVO) throws BusApiException {
        try {
            this.deviceService.unbindingDevice(bindingDeviceVO);
        } catch (Exception e) {
            String message = "解绑设备失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改设备")
    @PutMapping
    //@RequiresPermissions("device:update")
    @ApiOperation(value = "修改设备", notes = "修改设备", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "PUT")
    public void updateDevice(@RequestBody @Valid Device device) throws BusApiException {
        try {
            this.deviceService.modifyDevice(device);
        } catch (Exception e) {
            String message = "修改设备失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除设备")
    @DeleteMapping("/{deviceIds}")
    //@RequiresPermissions("device:delete")
    @ApiOperation(value = "删除设备", notes = "删除设备", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "DELETE")
    public void deleteDevice(@NotBlank(message = "{required}") @PathVariable String deviceIds) throws BusApiException {
        try {
            String[] ids = deviceIds.split(StringPool.COMMA);
            this.deviceService.deleteDeviceIds(ids);
        } catch (Exception e) {
            String message = "删除手环设备失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("device:export")
    @ApiOperation(value = "导出设备", notes = "导出设备", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "POST")
    public void exportDevice(@RequestBody @Valid DeviceViewVO deviceViewVO, HttpServletResponse response) throws BusApiException {
        try {
            List<Device> devices = this.deviceService.listDevices(deviceViewVO);
            ExcelKit.$Export(Device.class, response).downXlsx(devices, false);
            this.mapService.batchCreateDeviceEntity(devices);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
/*    @ApiOperation(value = "导入设备", notes = "导入设备", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "POST")
    @GetMapping
    public List<Device> importDevice(QueryRequest request, Device device) {
        return this.deviceService.listDevices(request, device);
    }*/

    @Log("设备侦听")
    @PostMapping("/call-from-student/{studentId}")
    @ApiOperation(value = "设备侦听，设备拨打电话到当前登录用户的手机号", notes = "设备拨打电话到当前登录用户的手机号", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "POST")
    public BusApiResponse callFromDevice(@PathVariable Long studentId) throws BusApiException {
        Student student = studentService.getById(studentId);
        if (student == null) {
            throw new BusApiException("学生不存在");
        }
        // 检查当前用户是不是学生监护人
        User user = BusApiUtil.getCurrentUser();
        guardianService.checkGuardian(studentId, user.getUserId());
        Device device = deviceService.getByStudentId(studentId);
        if (device == null) {
            throw new BusApiException("该学生未绑定手环设备");
        }
        return new BusApiResponse().data(iotManager.call(device.getDeviceCode(), user.getMobile()));
    }

    @Log("修改设备上报频率")
    @PutMapping("/change-upload-frequency")
    @ApiOperation(value = "修改设备上报数据频率", notes = "修改设备上报数据频率", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "PUT")
    public BusApiResponse changeUploadFrequency(@RequestParam(name = "studentId") Long studentId,
                                                @RequestParam(name = "frequency", defaultValue = "60") Integer frequency)
            throws BusApiException {
        Student student = studentService.getById(studentId);
        if (student == null) {
            throw new BusApiException("学生不存在");
        }
        Device device = deviceService.getByStudentId(studentId);
        if (device == null) {
            throw new BusApiException("该学生未绑定手环设备");
        }
        if(frequency < 60) {
            throw new BusApiException("不能设置小于60秒");
        }
        return new BusApiResponse().data(iotManager.changeUploadFrequency(device.getDeviceCode(), frequency));
    }

    @GetMapping("/count/all")
    @ApiOperation(value = "首页-设备统计", notes = "首页-设备统计", tags = ApiTagsConstant.TAG_DEVICE, httpMethod = "GET")
    public DeviceCountAll countAll() throws BusApiException {
        return deviceManager.countAll();
    }
}
