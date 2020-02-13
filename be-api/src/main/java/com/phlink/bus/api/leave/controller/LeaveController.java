package com.phlink.bus.api.leave.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.leave.domain.Leave;
import com.phlink.bus.api.leave.domain.vo.LeaveDetailVO;
import com.phlink.bus.api.leave.domain.vo.LeaveVO;
import com.phlink.bus.api.leave.domain.vo.StudentByDayLeaveInfoVO;
import com.phlink.bus.api.leave.service.ILeaveService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


/**
 * @author zhouyi
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/leave")
@Api(tags = "请假管理")
public class LeaveController extends BaseController {

    @Autowired
    public ILeaveService leaveService;

    @GetMapping
//    //@RequiresPermissions("leave:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "请假管理", httpMethod = "GET")
    public Map<String, Object> listLeave(QueryRequest request, LeaveVO leaveVO) {
        return getDataTable(this.leaveService.listLeaves(request, leaveVO));
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("leave:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "请假管理", httpMethod = "GET")
    public Leave detail(@NotBlank(message = "{required}") @PathVariable Long id) {
        return this.leaveService.findById(id);
    }

    @Log("添加请假")
    @PostMapping("/add")
//    //@RequiresPermissions("leave:add")
    @ApiOperation(value = "添加请假", notes = "添加请假", tags = "请假管理", httpMethod = "POST")
    public Map<String, Object> addLeave(@RequestBody @Valid Leave leave) {
        LocalDate endDate = leave.getLeaveDateEnd();
        if (endDate == null) {
            leave.setLeaveDateEnd(leave.getLeaveDateStart());
        }
        Map<String, Object> map = this.leaveService.createLeave(leave);
        boolean flag = (boolean) map.get("flag");
        if (flag) {
            return returnSuccess("操作成功");
        } else {
            return returnFail(map.get("message"));
        }
    }

    @Log("修改请假")
    @PutMapping
//    //@RequiresPermissions("leave:update")
    @ApiOperation(value = "修改请假", notes = "修改请假", tags = "请假管理", httpMethod = "PUT")
    public void updateLeave(@RequestBody @Valid Leave leave) throws BusApiException {
        try {
            this.leaveService.modifyLeave(leave);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除请假")
    @DeleteMapping("/leaveIds")
//    //@RequiresPermissions("leave:delete")
    @ApiOperation(value = "删除请假", notes = "删除请假", tags = "请假管理", httpMethod = "DELETE")
    public void deleteLeave(@PathVariable String leaveIds) throws BusApiException {
        try {
            String[] ids = leaveIds.split(StringPool.COMMA);
            this.leaveService.deleteLeaves(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @GetMapping("/day/bus-time-info")
//    //@RequiresPermissions("leave:view")
    @ApiOperation(value = "查看学生某一日请假班次列表", notes = "查看学生某一日请假班次列表", tags = "请假管理", httpMethod = "GET")
    public List<StudentByDayLeaveInfoVO> listBusTimeInfo(@Valid @ApiParam(example = "2019-09-22", required = true) @NotBlank(message = "{required}") @RequestParam(name = "day") String day,
                                                         @Valid @ApiParam(required = true) @NotNull(message = "{required}") @RequestParam(name = "studentId") Long studentId) {
        return this.leaveService.listBusTimeInfo(day, studentId);
    }

    @GetMapping("/day/list")
//    //@RequiresPermissions("leave:view")
    @ApiOperation(value = "查看学生一段时间内请假日期", notes = "查看学生一段时间内请假日期", tags = "请假管理", httpMethod = "GET")
    public List<LocalDate> listDay(@Valid @ApiParam(example = "2019-09-01", required = true) @NotBlank(message = "{required}") @RequestParam(name = "dayStart") String dayStart,
                                   @Valid @ApiParam(example = "2019-09-22", required = true) @NotBlank(message = "{required}") @RequestParam(name = "dayEnd") String dayEnd,
                                   @Valid @ApiParam(required = true) @NotNull(message = "{required}") @RequestParam(name = "studentId") Long studentId) {
        return this.leaveService.listDay(dayStart, dayEnd, studentId);
    }

    @PostMapping("export")
//    //@RequiresPermissions("leave:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "", httpMethod = "POST")
    public void exportLeave(@RequestBody LeaveVO leaveVO, HttpServletResponse response) throws BusApiException {
        try {
            List<LeaveDetailVO> result = this.leaveService.listLeaves(leaveVO);
            ExcelKit.$Export(LeaveDetailVO.class, response).downXlsx(result, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
