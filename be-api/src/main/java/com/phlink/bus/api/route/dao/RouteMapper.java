package com.phlink.bus.api.route.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.route.domain.Route;
import com.phlink.bus.api.route.domain.vo.RouteBusVO;
import com.phlink.bus.api.route.domain.vo.RouteViewVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wen
 */
public interface RouteMapper extends BaseMapper<Route> {

    Page<Route> listRoutes(Page page, @Param("routeViewVO") RouteViewVO routeViewVO);

    List<RouteBusVO> listRouteBus();

    List<Route> listUnbindFence(@Param("query") String query);
}
