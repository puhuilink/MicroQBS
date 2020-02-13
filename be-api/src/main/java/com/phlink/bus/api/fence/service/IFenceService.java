package com.phlink.bus.api.fence.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.alarm.domain.CodeFence;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.fence.domain.Fence;
import com.phlink.bus.api.fence.domain.FenceCronJob;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.fence.domain.FenceViewVO;
import com.phlink.bus.api.fence.domain.enums.RelationTypeEnum;
import com.phlink.bus.api.route.domain.vo.FenceStopVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhouyi
 */
public interface IFenceService extends IService<Fence> {


    /**
     * 获取详情
     */
    FenceVO findById(Long id) throws BusApiException;

    /**
     * 查询列表
     *
     * @param request
     * @param fenceViewVO
     * @return
     */
    IPage<Fence> listFences(QueryRequest request, FenceViewVO fenceViewVO);

    /**
     * 新增
     *
     * @param fenceVO
     * @throws BusApiException
     * @return
     */
    Fence createFence(FenceVO fenceVO) throws BusApiException;

    /**
     * 修改
     *
     * @param fenceVO
     * @throws BusApiException
     */
    void modifyFence(FenceVO fenceVO) throws BusApiException;

    /**
     * 批量删除
     *
     * @param fids
     * @throws BusApiException
     */
    void deleteFences(String[] fids);

    /**
     * 查询路线的站点电子围栏id
     *
     * @param routeId
     * @return
     */
    List<String> getStopFences(Long routeId);

    /**
     * 根据路线id修改路线围栏的设备
     *
     * @return
     */
    void modifyFenceEntitys(Long routeId);

    @Deprecated
    List<FenceCronJob> saveFenceJob(String fenceJob, String fenceId);

    /**
     * 创建站点围栏
     *
     * @param fenceStopVO
     * @param routeId
     */
    @Deprecated
    void createStopFence(FenceStopVO fenceStopVO, Long routeId);

    @Deprecated
    @Transactional
    void createStopFence(FenceStopVO fenceStopVO, FenceVO routeFenceVO);

    /**
     * 批量删除站点围栏
     *
     * @param stopIds
     */
    @Deprecated
    void deleteStopFences(List<Long> stopIds);

    /**
     * 根据站点id删除站点围栏
     *
     * @param stopId
     */
    @Deprecated
    void deleteStopFence(Long stopId);

    /**
     * 获取路线围栏
     *
     * @return
     */
    List<Fence> getRouteFence();

    /**
     * 获取路线围栏与车辆
     *
     * @return
     */
    List<CodeFence> getBusRouteFence();

    /**
     * 获取站点围栏fence_id与车辆id
     *
     * @return
     */
    List<CodeFence> getBusStopFence();

    /**
     * 获取学校围栏fence_id与手环id
     *
     * @return
     */
    List<CodeFence> getSchoolFence();

    /**
     * 判断该围栏是否存在
     *
     * @param relationId
     * @param relationType
     * @return
     */
    Boolean checkFenceByRelation(Long relationId, RelationTypeEnum relationType);

    /**
     * 根据围栏id获取关联id（路线。站点，学校）
     *
     * @param fenceId
     * @param relationType
     * @return
     */
    Long getRelationId(String fenceId, RelationTypeEnum relationType);

    Fence getByName(String fenceName);

    /**
     * 根据ID查询，包括被删除的数据
     * @param fenceIds
     * @return
     */
    List<Fence> listByIdsIncludeDelete(List<Long> fenceIds);

    @Deprecated
    void updateFenceJob(Long id, List<FenceCronJob> jobs);

    void updateFenceId(Long id, String fenceId);

    void deleteStopFenceByRouteIds(List<Long> routeIds);

    /**
     * 根据高德围栏ID更新enable状态
     * @param gdFenceId
     * @param success
     */
    void updateFenceEnableStatus(String gdFenceId, boolean success);
}
