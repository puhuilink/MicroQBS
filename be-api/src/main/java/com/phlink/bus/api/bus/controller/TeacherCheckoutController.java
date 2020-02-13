package com.phlink.bus.api.bus.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.dao.BusMapper;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.TeacherCheckout;
import com.phlink.bus.api.bus.service.ITeacherCheckoutService;
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
@RequestMapping("/teacher-checkout")
@Api(tags = "老师晨检")
public class TeacherCheckoutController extends BaseController {

    @Autowired
    public ITeacherCheckoutService teacherCheckoutService;

    @Autowired
    private BusMapper busMapper;

    @GetMapping("/get")
//    //@RequiresPermissions("teacherCheckout:view")
    @ApiOperation(value = "查询当前用户今天的晨检", notes = "查询当前用户今天的晨检", tags = "", httpMethod = "GET")
    public TeacherCheckout listTeacherCheckout() {
        //return  getDataTable(this.teacherCheckoutService.listTeacherCheckouts(request, teacherCheckout));
        Long userId = BusApiUtil.getCurrentUser().getUserId();
        return this.teacherCheckoutService.findIsCheckout(userId);
    }

    @GetMapping("/list")
//  //@RequiresPermissions("teacherCheckout:view")
    @ApiOperation(value = "查询列表", notes = "查询列表", tags = "", httpMethod = "GET")
    public Map<String, Object> listAllTeacherCheckout(QueryRequest request, TeacherCheckout teacherCheckout) {
        return getDataTable(this.teacherCheckoutService.listTeacherCheckouts(request, teacherCheckout));
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("teacherCheckout:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "", httpMethod = "GET")
    public TeacherCheckout detail(@PathVariable Long id) {
        return this.teacherCheckoutService.findById(id);
    }

    @Log("添加老师晨检")
    @PostMapping("/add")
//    //@RequiresPermissions("teacherCheckout:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "", httpMethod = "POST")
    public BusApiResponse addTeacherCheckout(@RequestBody @Valid TeacherCheckout teacherCheckout) throws BusApiException {
        Long userId = BusApiUtil.getCurrentUser().getUserId();
        //查询当前用户今天是否已经进行过晨检
        TeacherCheckout check = this.teacherCheckoutService.findIsCheckout(userId);
        if (check != null) {
            String message = "当前用户已完成今日晨检";
            log.info(message);
            throw new BusApiException(message);
        }
        //根据当前用户查询到绑定的车牌号
        Bus bus = busMapper.findBusByUserId(userId);
        if (bus == null) {
            String message = "未找到该用户绑定的车辆信息";
            log.info(message);
            throw new BusApiException(message);
        }

//        if(AttachmentTypeEnum.IMAGE.equals(teacherCheckout.getAttachmentType())) {
//            if(teacherCheckout.getImagePath() == null || teacherCheckout.getImagePath().length == 0 ) {
//                String message = "图片不能为空";
//                throw new BusApiException(message);
//            }
//        }
//
//        if(AttachmentTypeEnum.VIDEO.equals(teacherCheckout.getAttachmentType())) {
//            if(StringUtils.isBlank(teacherCheckout.getVideoPath()) ) {
//                String message = "视频不能为空";
//                throw new BusApiException(message);
//            }
//        }

        String mobile = BusApiUtil.getCurrentUser().getMobile();
        String username = BusApiUtil.getCurrentUser().getRealname();
        teacherCheckout.setMobile(mobile);
        teacherCheckout.setUserId(userId);
        teacherCheckout.setUserName(username);
        if(teacherCheckout.getTime() == null) {
            teacherCheckout.setTime(LocalDate.now());
        }else{
            teacherCheckout.setTime(teacherCheckout.getTime());
        }
        teacherCheckout.setNumberPlate(bus.getNumberPlate());
        this.teacherCheckoutService.createTeacherCheckout(teacherCheckout);
        return new BusApiResponse().message("success");
    }

    @Log("管理平台添加老师晨检")
    @PostMapping("/platform/add")
//    //@RequiresPermissions("teacherCheckout:add")
    @ApiOperation(value = "管理平台添加", notes = "管理平台添加", tags = "", httpMethod = "POST")
    public Map<String, Object> addTeacherCheckoutOnPlateform(@RequestBody @Valid TeacherCheckout teacherCheckout) {
        boolean flag = this.teacherCheckoutService.createTeacherCheckoutOnPlatform(teacherCheckout);
        if (flag) {
            return returnSuccess("添加成功");
        } else {
            return returnFail("添加失败");
        }
    }

    @Log("修改老师晨检")
    @PutMapping("/update/{id}")
//    //@RequiresPermissions("teacherCheckout:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "", httpMethod = "PUT")
    public void updateTeacherCheckout(@RequestBody @Valid TeacherCheckout teacherCheckout) throws BusApiException {
        try {
            this.teacherCheckoutService.modifyTeacherCheckout(teacherCheckout);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除老师晨检")
    @DeleteMapping("/{teacherCheckoutIds}")
//    //@RequiresPermissions("teacherCheckout:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "", httpMethod = "DELETE")
    public void deleteTeacherCheckout(@NotBlank(message = "{required}") @PathVariable String teacherCheckoutIds) throws BusApiException {
        try {
            String[] ids = teacherCheckoutIds.split(StringPool.COMMA);
            this.teacherCheckoutService.deleteTeacherCheckouts(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("teacherCheckout:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "", httpMethod = "POST")
    public void exportTeacherCheckout(@RequestBody TeacherCheckout teacherCheckout, HttpServletResponse response) throws BusApiException {
        try {
            List<TeacherCheckout> teacherCheckouts = this.teacherCheckoutService.listTeacherCheckouts(teacherCheckout);
            ExcelKit.$Export(TeacherCheckout.class, response).downXlsx(teacherCheckouts, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
