package com.phlink.bus.api.route.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.route.domain.Route;
import com.phlink.bus.api.route.domain.RouteOperation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.bus.api.route.domain.vo.RouteOperationDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wen
 */
public interface RouteOperationMapper extends BaseMapper<RouteOperation> {

    IPage<RouteOperation> listRouteOperations(Page<RouteOperation> page, @Param("entity") RouteOperation routeOperation);

    RouteOperation getByBusCode(@Param("busCode") String busCode);

    List<Route> listUnbindRoutes(@Param("routeId") Long routeId);

    RouteOperation getRouteByBusId(@Param("numberPlate") String numberPlate);

    RouteOperationDetailVO getDetailVO(@Param("routeOperationId") Long routeOperationId, @Param("direction") Integer direction);

    RouteOperation getByWorkerId(@Param("userId") Long userId);
}
