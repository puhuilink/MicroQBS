package com.phlink.bus.api.route.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.Route;
import com.phlink.bus.api.route.domain.vo.*;

import java.util.List;

/**
 * @author wen
 */
public interface IRouteService extends IService<Route> {


    /**
     * 获取详情
     */
    Route findById(Long id);

    /**
     * 查询列表
     *
     * @param request
     * @param routeViewVO
     * @return
     */
    IPage<Route> listRoutes(QueryRequest request, RouteViewVO routeViewVO);

    List<Route> listRoutes(RouteViewVO routeViewVO);

    /**
     * 新增
     *
     * @param route
     */
    void createRoute(Route route) throws BusApiException;

    /**
     * 修改
     *
     * @param route
     */
    void modifyRoute(Route route);

    /**
     * 批量删除
     *
     * @param routeIds
     */
    void deleteRoutes(String[] routeIds);

    /**
     * 根据路线名称获取路线信息
     *
     * @param routeName
     * @return
     */
    Route findByName(String routeName);

    /**
     * 路线车辆关系
     *
     * @return
     */
    List<RouteBusVO> listRouteBus();

    /**
     * 未绑定围栏的路线列表
     * @return
     * @param query
     */
    List<Route> listUnbindFence(String query);

    /**
     * 保存路线
     * @param routeVO
     * @return
     */
    Route saveRoute(SaveRouteVO routeVO, Long createUser) throws BusApiException;

    Route updateRoute(UpdateRouteVO routeVO, Route route, Long userId) throws BusApiException;

    RouteDetailVO getRouteDetail(Route route, Long routeId);

    List<Route> listBySchool(Long schoolId);
}
