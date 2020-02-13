package com.phlink.bus.api.route.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.route.domain.Route;
import com.phlink.bus.api.route.domain.RouteOperation;
import com.phlink.bus.api.route.domain.vo.RouteOperationDetailVO;
import com.phlink.bus.api.route.domain.vo.SaveRouteOperationVO;

import java.util.List;

/**
 * @author wen
 */
public interface IRouteOperationService extends IService<RouteOperation> {


    /**
    * 获取详情
    */
    RouteOperation findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param routeOperation
    * @return
    */
    IPage<RouteOperation> listRouteOperations(QueryRequest request, RouteOperation routeOperation);

    List<RouteOperation> listRouteOperations(RouteOperation routeOperation);

    /**
    * 新增
    * @param routeOperation
    */
    void createRouteOperation(RouteOperation routeOperation) throws RedisConnectException, BusApiException;

    /**
    * 修改
    * @param routeOperation
    */
    void modifyRouteOperation(RouteOperation routeOperation);

    RouteOperation createRouteOperation(SaveRouteOperationVO routeOperation) throws BusApiException;
    RouteOperation updateRouteOperation(SaveRouteOperationVO routeOperationVO) throws BusApiException;

    /**
    * 批量删除
    * @param routeAssociateIds
    */
    void deleteRouteOperations(String[] routeAssociateIds);

    /**
     * 查看车辆绑定的路线
     * @param busCode
     * @return
     */
    RouteOperation getByBusCode(String busCode);

    /**
     * 查询未绑定路线列表
     * @param routeId
     * @return
     */
    List<Route> listUnbindRoutes(Long routeId);

    /**
     * 根据ID获得路线关联的详细信息
     * @param id
     * @return
     */
    RouteOperationDetailVO getDetailVO(Long id);

    /**
     * 根据车辆ID获取路线关联信息
     * @param busId
     * @return
     */
    RouteOperation getByBusId(Long busId);

    /**
     * 根据司机或随车老师ID获取路线关联信息
     * @param userId
     * @return
     */
    RouteOperation getByWorkerId(Long userId);

    /**
     * 根据路线ID列表获取所有的路线关联信息
     * @param routeIds
     * @return
     */
    List<RouteOperation> listByRouteIds(List<Long> routeIds);
}
