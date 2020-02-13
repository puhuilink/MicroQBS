package com.phlink.bus.api.route.service;

import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.Stop;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.route.domain.vo.FenceStopVO;

import java.util.List;

/**
 * @author wen
 */
public interface IStopService extends IService<Stop> {


    /**
    * 获取详情
    */
    Stop findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param stop
    * @return
    */
    IPage<Stop> listStops(QueryRequest request, Stop stop);

    /**
    * 新增
    * @param stop
    */
    void createStop(Stop stop) throws BusApiException;

    /**
    * 修改
    * @param stop
    */
    void modifyStop(Stop stop);

    /**
     * 批量修改
     * @param stopList
     */
    void batchModifyStop(List<Stop> stopList);

    /**
    * 批量删除
    * @param stopIds
    */
    void deleteStops(String[] stopIds);

    /**
     * 批量添加站点
     * @param stops
     */
    void batchCreateStop(List<Stop> stops);

    /**
     * 路线上的站点列表
     * @param routeId
     * @return
     */
    List<Stop> listStopByRoute(Long routeId);

    /**
     * 根据站点名称获取站点信息
     * @param stopName
     * @param routeId
     * @return
     */
    Stop findByName(String stopName, Long routeId);

    /**
     * 根据路线id获取未创建电子围栏的站点相关信息
     * @param routeId
     * @return
     */
    List<FenceStopVO> listUnbindFenceStopInfo(Long routeId);

    /**
     * 根据绑定的车辆获取站点信息
     * @param busId
     * @return
     */
    List<Stop> listStopByBindBus(Long busId);

    /**
     * 根据路线删除站点
     * @param routeIds
     */
    void deleteStopsByRouteIds(List<Long> routeIds);

    List<Stop> listStopByRouteIds(List<Long> routeIds);
}
