package com.phlink.bus.api.bus.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.domain.TeacherCheckoutEnd;
import com.phlink.bus.api.bus.service.ITeacherCheckoutEndService;
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
@RequestMapping("/teacher-checkout-end")
@Api(tags ="随车老师收车检查")
public class TeacherCheckoutEndController extends BaseController {
    @Autowired
    public ITeacherCheckoutEndService teacherCheckoutEndService;
    @Autowired
    public UserService userService;

    @GetMapping
//    //@RequiresPermissions("teacherCheckoutEnd:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "随车老师收车检查", httpMethod = "GET")
    public Map<String, Object> listTeacherCheckoutEnd(QueryRequest request,
                                                      @RequestParam(required = false) String realname,
                                                      @RequestParam(required = false) String dateStart,
                                                      @RequestParam(required = false) String dateEnd,
                                                      @RequestParam(required = false) String mobile) {
        return getDataTable(this.teacherCheckoutEndService.listTeacherCheckoutEnds(request, realname, dateStart, dateEnd, mobile));
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("teacherCheckoutEnd:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "随车老师收车检查", httpMethod = "GET")
    public TeacherCheckoutEnd detail(@PathVariable Long id) {
        return this.teacherCheckoutEndService.findById(id);
    }

    @Log("添加随车老师收车检查")
    @PostMapping
//    //@RequiresPermissions("teacherCheckoutEnd:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "随车老师收车检查", httpMethod = "POST")
    public void addTeacherCheckoutEnd(HttpServletRequest request,
                                      @RequestBody @Valid TeacherCheckoutEnd teacherCheckoutEnd) throws BusApiException {

        String onlyLogin = request.getHeader(BusApiConstant.UNIQUE_LOGIN);
        User user = null;
        if(StringUtils.isNotBlank(onlyLogin)) {
            // app
            user = BusApiUtil.getCurrentUser();
        }else{
            // 平台
            user = userService.getById(teacherCheckoutEnd.getUserId());
        }
        if(user == null) {
            throw new BusApiException("用户信息不存在");
        }
        this.teacherCheckoutEndService.createTeacherCheckoutEnd(teacherCheckoutEnd, user);
    }

    @Log("修改随车老师收车检查")
    @PutMapping
//    //@RequiresPermissions("teacherCheckoutEnd:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "随车老师收车检查", httpMethod = "PUT")
    public void updateTeacherCheckoutEnd(@RequestBody @Valid TeacherCheckoutEnd teacherCheckoutEnd) throws BusApiException{
        this.teacherCheckoutEndService.modifyTeacherCheckoutEnd(teacherCheckoutEnd);
    }

    @Log("删除随车老师收车检查")
    @DeleteMapping("/{teacherCheckoutEndIds}")
//    //@RequiresPermissions("teacherCheckoutEnd:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "随车老师收车检查", httpMethod = "DELETE")
    public void deleteTeacherCheckoutEnd(@NotBlank(message = "{required}") @PathVariable String teacherCheckoutEndIds) throws BusApiException{
        String[] ids = teacherCheckoutEndIds.split(StringPool.COMMA);
        this.teacherCheckoutEndService.deleteTeacherCheckoutEnds(ids);
    }

    @PostMapping("export")
//    //@RequiresPermissions("teacherCheckoutEnd:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "随车老师收车检查", httpMethod = "POST")
    public void exportTeacherCheckoutEnd(@RequestParam(required = false) String realname,
                                         @RequestParam(required = false) String dateStart,
                                         @RequestParam(required = false) String dateEnd,
                                         @RequestParam(required = false) String mobile, HttpServletResponse response) throws BusApiException{
        List<TeacherCheckoutEnd> teacherCheckoutEnds = this.teacherCheckoutEndService.listTeacherCheckoutEnds(null, realname, dateStart, dateEnd, mobile).getRecords();
        ExcelKit.$Export(TeacherCheckoutEnd.class, response).downXlsx(teacherCheckoutEnds, false);
    }
}
