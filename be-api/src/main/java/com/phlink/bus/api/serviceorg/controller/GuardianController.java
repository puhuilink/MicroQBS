package com.phlink.bus.api.serviceorg.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.domain.VO.GuardianIdVO;
import com.phlink.bus.api.serviceorg.domain.VO.SaveGuardianVO;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author wen
 */
@Slf4j
@RestController
@Validated
@RequestMapping("/guardian")
@Api(tags = ApiTagsConstant.TAG_GUARDIAN)
public class GuardianController extends BaseController {

    @Autowired
    public IGuardianService guardianService;
    @Autowired
    public IStudentService studentService;

    @Log("删除监护人信息")
    @DeleteMapping("/{guardianIds}")
//	//@RequiresPermissions("guardian:delete")
    @ApiOperation(value = "监护人信息删除", notes = "监护人信息删除", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "DELETE")
    public void deleteGuardian(HttpServletRequest request,
                               @NotBlank(message = "{required}") @PathVariable String guardianIds,
                               @NotNull(message = "{required}") @RequestParam Long studentId
    ) throws BusApiException {

        String onlyLogin = request.getHeader(BusApiConstant.UNIQUE_LOGIN);
        if (onlyLogin != null) {
            // app 登录时需要检查是否是主责
            this.guardianService.checkMainGuardian(studentId);
        }
        try {
            String[] ids = guardianIds.split(StringPool.COMMA);
            this.guardianService.removeGuardianIds(studentId, ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
//	//@RequiresPermissions("guardian:export")
    @ApiOperation(value = "监护人信息导出", notes = "监护人信息导出", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "POST")
    public void exportGuardian(@RequestBody @Valid QueryRequest request, @RequestBody @Valid Guardian guardian,
                               HttpServletResponse response) throws BusApiException {
        try {
            List<Guardian> guardians = this.guardianService.listGuardians(request, guardian).getRecords();
            ExcelKit.$Export(Guardian.class, response).downXlsx(guardians, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("添加共同监护人")
    @PostMapping("other")
//	//@RequiresPermissions("guardian:add")
    @ApiOperation(value = "添加共同监护人", notes = "添加共同监护人", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "POST")
    public void addOtherGuardian(@RequestBody @Valid SaveGuardianVO bean) throws Exception {
        this.guardianService.checkMainGuardian(bean.getStudentId());
        this.guardianService.addOtherGuardian(bean.getStudentId(), bean.getUsername(), bean.getMobile(), bean.getIdCard());
    }

    @Log("授予请假权限")
    @PostMapping("/{guardianId}/authorization-leave-permissions/{studentId}")
//	//@RequiresPermissions("guardian:update")
    @ApiOperation(value = "将某个学生的请假权限授予监护人", notes = "将某个学生的请假权限授予监护人，只有主责人有权限修改", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "POST")
    public void authorizationLeavePermissions(@PathVariable Long guardianId,
                                              @PathVariable Long studentId) throws BusApiException {
        this.guardianService.checkMainGuardian(studentId);
        this.guardianService.updateLeavePermissions(guardianId, studentId, true);

    }

    @Log("撤销请假权限")
    @PostMapping("/{guardianId}/revocation-leave-permissions/{studentId}")
//	//@RequiresPermissions("guardian:update")
    @ApiOperation(value = "撤销请假权限", notes = "撤销请假权限，只有主责人有权限修改", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "POST")
    public void revocationLeavePermissions(@PathVariable Long guardianId,
                                           @PathVariable Long studentId) throws BusApiException {
        this.guardianService.checkMainGuardian(studentId);
        this.guardianService.updateLeavePermissions(guardianId, studentId, false);
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("guardian:detail")
    @ApiOperation(value = "监护人详情", notes = "监护人详情", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "GET")
    public Guardian getGuardianDetailVO(@NotBlank(message = "{required}") @RequestParam("studentId") Long id) {
        return this.guardianService.getGuardianDetail(id);
    }

    @GetMapping("/student")
    //@RequiresPermissions("student:view")
    @ApiOperation(value = "监护人下的学生列表", notes = "监护人下的学生列表", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "GET")
    public List<Student> listStudent(@ApiParam(name = "guardianUserId", value = "监护人用户ID，不传则会获取当前登录用户的userId", required = false) @RequestParam(required = false) Long guardianUserId) {
        if (guardianUserId == null) {
            guardianUserId = BusApiUtil.getCurrentUser().getUserId();
        }
        return this.studentService.listStudentByGuardian(guardianUserId);
    }

    @GetMapping("/other")
//    //@RequiresPermissions("guardian:view")
    @ApiOperation(value = "其他监护人列表", notes = "其他监护人列表", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "GET")
    public List<Guardian> listOther(@ApiParam(name = "guardianUserId", value = "监护人用户ID，不传则会获取当前登录用户的userId", required = false) @RequestParam(required = false) Long guardianUserId,
                                    @ApiParam(name = "studentId", value = "学生ID", required = true) @RequestParam Long studentId) throws BusApiException {
        // 检查该用户是否可看该学生
        if (guardianUserId == null) {
            guardianUserId = BusApiUtil.getCurrentUser().getUserId();
        }
        Guardian guardian = guardianService.getByGuardianId(guardianUserId);
        if (guardian == null) {
            throw new BusApiException("无法查看该学生的信息");
        }
        List<Long> studentIds = Arrays.asList(guardian.getStudentId());
        if (!studentIds.contains(studentId)) {
            throw new BusApiException("无法查看该学生的信息");
        }
        return this.guardianService.listOtherGuardian(studentId);
    }

    @Log("监护人账号冻结")
    @PutMapping("/invalidate")
    //@RequiresPermissions("guardian:invalidate")
    @ApiOperation(value = "监护人账号冻结", notes = "监护人账号冻结", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "PUT")
    public void invalidate(@Valid @RequestBody GuardianIdVO guardianIdVO) throws BusApiException {
        this.guardianService.invalidate(guardianIdVO.getGuardianId());
    }

    @Log("监护人账号恢复")
    @PutMapping("/effective")
    //@RequiresPermissions("guardian:effective")
    @ApiOperation(value = "监护人账号恢复", notes = "监护人账号恢复", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "PUT")
    public void effective(@Valid @RequestBody GuardianIdVO guardianIdVO) throws BusApiException {
        this.guardianService.effective(guardianIdVO.getGuardianId());
    }

    @GetMapping("/{userId}/check")
    @ApiOperation(value = "检查用户身份是不是家长", notes = "检查用户身份是不是家长", tags = ApiTagsConstant.TAG_GUARDIAN, httpMethod = "GET")
    public BusApiResponse check(@PathVariable("userId") Long userId) {
        Guardian guardian = this.guardianService.getByUserId(userId);
        boolean result = false;
        if (guardian != null) {
            result = true;
        }
        return new BusApiResponse().data(result);
    }
}
