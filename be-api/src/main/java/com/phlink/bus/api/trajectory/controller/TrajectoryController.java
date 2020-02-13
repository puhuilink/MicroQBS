package com.phlink.bus.api.trajectory.controller;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import com.phlink.bus.api.trajectory.domain.TrajectoryDeleteVO;
import com.phlink.bus.api.trajectory.domain.TrajectoryVO;
import com.phlink.bus.api.trajectory.domain.vo.TrajectorySaveVO;
import com.phlink.bus.api.trajectory.service.ITrajectoryService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


/**
 * @author zhouyi
 */
@Slf4j
@RestController
@RequestMapping("/trajectory")
@Api(tags = ApiTagsConstant.TAG_TRAJECTORY)
public class TrajectoryController extends BaseController {

    @Autowired
    public ITrajectoryService trajectoryService;

    @GetMapping
    //@RequiresPermissions("trajectory:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_TRAJECTORY, httpMethod = "GET")
    public Map<String, Object> listTrajectory(QueryRequest request, TrajectoryVO trajectoryVO) {
        return getDataTable(this.trajectoryService.listTrajectorys(request, trajectoryVO));
    }

    @GetMapping("detail")
    //@RequiresPermissions("trajectory:get")
    @ApiOperation(value = "轨迹详情（包括高德坐标点）", notes = "轨迹详情", tags = ApiTagsConstant.TAG_TRAJECTORY, httpMethod = "GET")
    public Trajectory detail(@Valid @NotNull(message = "{required}") @RequestParam(value = "id") Long id,
                             @RequestParam(value = "simple", defaultValue = "false") Boolean simple) throws BusApiException {
        //todo:查询轨迹数据库中数据
        return this.trajectoryService.detail(id, simple);
    }

    @Log("生成轨迹(保存)")
    @PostMapping
    //@RequiresPermissions("trajectory:add")
    @ApiOperation(value = "生成轨迹(保存)", notes = "生成轨迹", tags = ApiTagsConstant.TAG_TRAJECTORY, httpMethod = "POST")
    public void addTrajectory(@RequestBody @Valid TrajectorySaveVO vo) throws BusApiException {
        if (vo.getEndTimestamp() <= vo.getStartTimestamp()) {
            throw new BusApiException("开始时间要早于结束时间");
        }
        this.trajectoryService.createTrajectory(vo.getBusId(), vo.getTrname(), vo.getStartTimestamp(), vo.getEndTimestamp());
    }

    @Log("删除轨迹")
    @DeleteMapping
    //@RequiresPermissions("trajectory:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = ApiTagsConstant.TAG_TRAJECTORY, httpMethod = "DELETE")
    public void deleteTrajectory(@NotBlank(message = "{required}") @RequestBody List<TrajectoryDeleteVO> trajectoryDeleteVOList) throws BusApiException {
        try {
            this.trajectoryService.deleteTrajectorys(trajectoryDeleteVOList);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("trajectory:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_TRAJECTORY, httpMethod = "POST")
    public void exportTrajectory(QueryRequest request, @RequestBody TrajectoryVO trajectoryVO, HttpServletResponse response) throws BusApiException {
        try {
            List<Trajectory> trajectorys = this.trajectoryService.listTrajectorys(request, trajectoryVO).getRecords();
            ExcelKit.$Export(Trajectory.class, response).downXlsx(trajectorys, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @GetMapping("/list")
    //@RequiresPermissions("trajectory:view")
    @ApiOperation(value = "车辆生成的轨迹列表", notes = "车辆生成的轨迹列表", tags = ApiTagsConstant.TAG_TRAJECTORY, httpMethod = "GET")
    public List<Trajectory> listTrajectory(@RequestParam(name = "busId") Long busId) {
        return trajectoryService.listByBusId(busId);
    }
}
