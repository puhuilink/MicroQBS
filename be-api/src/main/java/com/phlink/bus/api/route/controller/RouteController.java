package com.phlink.bus.api.route.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnCheckID;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.route.domain.Route;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.route.domain.Trip;
import com.phlink.bus.api.route.domain.enums.RouteTypeEnum;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import com.phlink.bus.api.route.domain.enums.TripTimeEnum;
import com.phlink.bus.api.route.domain.vo.*;
import com.phlink.bus.api.route.service.IRouteService;
import com.phlink.bus.api.route.service.IStopService;
import com.phlink.bus.api.route.service.IStopTimeService;
import com.phlink.bus.api.serviceorg.domain.VO.StudentViewVO;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import com.phlink.bus.api.trajectory.service.ITrajectoryService;
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
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author wen
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/route")
@Api(tags = "路线")
public class RouteController extends BaseController {

    @Autowired
    public IRouteService routeService;
    @Autowired
    public IStopService stopService;
    @Autowired
    public IStopTimeService stopTimeService;
    @Autowired
    public IStudentService studentService;
    @Autowired
    public ITrajectoryService trajectoryService;

    @GetMapping
    //@RequiresPermissions("route:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "路线", httpMethod = "GET")
    public Map<String, Object> listRoute(QueryRequest request, RouteViewVO routeViewVO) {
        return getDataTable(this.routeService.listRoutes(request, routeViewVO));
    }

    @GetMapping("/{routeId}/stop")
    //@RequiresPermissions("route:view")
    @ApiOperation(value = "路线下的站点列表（不包括时间）", notes = "路线下的站点列表", tags = "路线", httpMethod = "GET")
    public List<Stop> listStop(@PathVariable Long routeId) {
        return this.stopService.listStopByRoute(routeId);
    }

    @GetMapping("/{routeId}/stop-time")
    //@RequiresPermissions("route:view")
    @ApiOperation(value = "路线下的站点时刻列表", notes = "路线下的站点时刻列表", tags = "路线", httpMethod = "GET")
    public List<TripStopTimeListVO> listStopTime(@PathVariable Long routeId) {
        return this.stopTimeService.listTripStopTimeListVO(routeId);
    }

    @GetMapping("/{routeId}/student")
    //@RequiresPermissions("route:view")
    @ApiOperation(value = "路线的学生分页列表", notes = "路线的学生分页列表", tags = "路线", httpMethod = "GET")
    public Map<String, Object> listStudent(QueryRequest request, @PathVariable Long routeId, StudentViewVO studentViewVO) {
        return getDataTable(this.studentService.listPageStudentByRouteId(request, routeId, studentViewVO));
    }

    @GetMapping("/{id}")
    //@RequiresPermissions("route:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "路线", httpMethod = "GET")
    public Route detail(@PathVariable Long id) {
        return this.routeService.findById(id);
    }

    @GetMapping("/route-type/{routeType}/trip")
    @ApiOperation(value = "根据路线类型获得行程列表", notes = "根据路线类型获得行程列表", tags = "路线", httpMethod = "GET")
    public List<Trip> listTripByRouteType(@PathVariable String routeType) throws BusApiException {
        RouteTypeEnum routeTypeEnum = RouteTypeEnum.of(routeType);
        if (routeTypeEnum == null) {
            throw new BusApiException("路线类型不存在");
        }
        // 创建trip
        List<TripTimeEnum> tripTimeEnums = routeTypeEnum.getTripTime();
        return tripTimeEnums.stream().map(tripTimeEnum -> {
            Trip trip = new Trip();
            trip.setDirectionId(tripTimeEnum.getDirectionId());
            trip.setTripTime(tripTimeEnum);
            return trip;
        }).collect(Collectors.toList());
    }

    @Log("添加路线")
    @Validated(OnAdd.class)
    @PostMapping
    //@RequiresPermissions("route:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "路线", httpMethod = "POST")
    public Route addRoute(@RequestBody @Valid Route route) throws BusApiException {
        this.routeService.createRoute(route);
        return route;
    }

    @GetMapping("/check/{routeName}")
    @ApiOperation(value = "检查路线名称是否重复", notes = "检查路线名称是否重复", tags = "路线", httpMethod = "GET")
    public boolean checkRouteName(@NotBlank(message = "{required}") @PathVariable String routeName,
                                  @RequestParam(name = "routeId", required = false) Long routeId) {
        Route route = this.routeService.findByName(routeName);
        if (route == null) {
            return true;
        }
        return route.getId().equals(routeId);
    }

    @Log("修改路线")
    @Validated(value = {OnUpdate.class, OnCheckID.class})
    @PutMapping
    //@RequiresPermissions("route:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "路线", httpMethod = "PUT")
    public void updateRoute(@RequestBody @Valid Route route) throws BusApiException {
        try {
            this.routeService.modifyRoute(route);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除路线")
    @DeleteMapping("/{routeIds}")
    //@RequiresPermissions("route:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "路线", httpMethod = "DELETE")
    public void deleteRoute(@NotBlank(message = "{required}") @PathVariable String routeIds) throws BusApiException {
        try {
            String[] ids = routeIds.split(StringPool.COMMA);
            this.routeService.deleteRoutes(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("route:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "路线", httpMethod = "POST")
    public void exportRoute(@RequestBody RouteViewVO routeViewVO, HttpServletResponse response) throws BusApiException {
        try {
            List<Route> routes = this.routeService.listRoutes(routeViewVO);
            ExcelKit.$Export(Route.class, response).downXlsx(routes, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @GetMapping("route-type")
    @ApiOperation(value = "路线类型", notes = "路线类型，1，2，4", tags = "路线", httpMethod = "GET")
    public List routeType() {
        return Arrays.stream(RouteTypeEnum.class.getEnumConstants()).map(RouteTypeEnum::toMap).collect(Collectors.toList());
    }

    @GetMapping("/unbind-fence")
    @ApiOperation(value = "未绑定围栏路线列表", notes = "未绑定围栏路线列表", tags = "路线", httpMethod = "GET")
    public List<Route> listUnbindFence(@RequestParam(name = "query", required = false) String query) {
        return routeService.listUnbindFence(query);
    }

    /*
     *
     *
     *
     * 路线创建、编辑V2
     *
     *
     *
     */
    @Log("添加路线")
    @Validated(OnAdd.class)
    @PostMapping("/v2/save")
    //@RequiresPermissions("route:add")
    @ApiOperation(value = "保存路线", notes = "保存路线", tags = "路线V2.0", httpMethod = "POST")
    public BusApiResponse saveRoute(@RequestBody @Valid SaveRouteVO routeVO) throws BusApiException {
        User currentUser = BusApiUtil.getCurrentUser();
        Trajectory trajectory = trajectoryService.getById(routeVO.getTrajectoryId());
        if(trajectory == null) {
            throw new BusApiException("绑定的轨迹不存在");
        }
        checkStopTime(routeVO.getStops());
        Route route = this.routeService.saveRoute(routeVO, currentUser.getUserId());
        return new BusApiResponse().data(route);
    }

    @Log("更新路线")
    @Validated(OnAdd.class)
    @PutMapping("/v2/update")
    //@RequiresPermissions("route:add")
    @ApiOperation(value = "更新路线", notes = "更新路线", tags = "路线V2.0", httpMethod = "PUT")
    public BusApiResponse updateRoute(@RequestBody @Valid UpdateRouteVO routeVO) throws BusApiException {
        Route route = routeService.getById(routeVO.getRouteId());
        if(route == null) {
            throw new BusApiException("更新的路线不存在");
        }
        Trajectory trajectory = trajectoryService.getById(routeVO.getTrajectoryId());
        if(trajectory == null) {
            throw new BusApiException("绑定的轨迹不存在");
        }
        checkStopTime(routeVO.getStops());
        User currentUser = BusApiUtil.getCurrentUser();
        route = this.routeService.updateRoute(routeVO, route, currentUser.getUserId());
        return new BusApiResponse().data(route);
    }

    private void checkStopTime(List<SaveRouteStopVO> stops) throws BusApiException {
        Map<TripTimeEnum, List<SaveRouteStopVO>> map = stops.stream().collect(Collectors.groupingBy(SaveRouteStopVO::getTripTime));
        for (Map.Entry<TripTimeEnum, List<SaveRouteStopVO>> entry : map.entrySet()) {
            TripTimeEnum tripTime = entry.getKey();
            List<SaveRouteStopVO> voList = entry.getValue();
            if (TripRedirectEnum.GO.equals(tripTime.getDirectionId())) {
                // 逐步增加
                LocalTime last = null;
                for (SaveRouteStopVO vo : voList) {
                    LocalTime curr = vo.getArrivalTime();
                    if (last == null) {
                        last = curr;
                        continue;
                    }
                    if (curr.isBefore(last) || curr.equals(last)) {
                        throw new BusApiException("站点时间配置有误");
                    }
                }
            }
            if (TripRedirectEnum.BACK.equals(tripTime.getDirectionId())) {
                // 逐步减小
                LocalTime last = null;
                for (SaveRouteStopVO vo : voList) {
                    LocalTime curr = vo.getArrivalTime();
                    if (last == null) {
                        last = curr;
                        continue;
                    }
                    if (curr.isAfter(last) || curr.equals(last)) {
                        throw new BusApiException("站点时间配置有误");
                    }
                }
            }
        }

    }

    @Log("路线详情")
    @Validated(OnAdd.class)
    @GetMapping("/v2/{routeId}/detail")
    //@RequiresPermissions("route:add")
    @ApiOperation(value = "路线详情", notes = "路线详情", tags = "路线V2.0", httpMethod = "PUT")
    public BusApiResponse getRouteDetail(@PathVariable Long routeId) throws BusApiException {
        Route route = routeService.getById(routeId);
        if(route == null) {
            throw new BusApiException("路线不存在");
        }
        RouteDetailVO routeVO = this.routeService.getRouteDetail(route, routeId);
        return new BusApiResponse().data(routeVO);
    }
}
