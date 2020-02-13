package com.phlink.bus.api.bus.service;

import com.phlink.bus.api.bus.domain.DvrLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wen
 */
public interface IDvrLocationService extends IService<DvrLocation> {


    /**
    * 获取详情
    */
    DvrLocation findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param dvrLocation
    * @return
    */
    IPage<DvrLocation> listDvrLocations(QueryRequest request, DvrLocation dvrLocation);

    /**
    * 新增
    * @param dvrLocation
    */
    void createDvrLocation(DvrLocation dvrLocation);

    /**
    * 修改
    * @param dvrLocation
    */
    void modifyDvrLocation(DvrLocation dvrLocation);

    /**
    * 批量删除
    * @param dvrLocationIds
    */
    void deleteDvrLocations(String[] dvrLocationIds);

    /**
     * 根据车辆和起止时间获得坐标点数量
     * @param busId
     * @param startTime
     * @param endTime
     * @return
     */
    List<DvrLocation> listByBusId(Long busId, LocalDateTime startTime, LocalDateTime endTime);

    List<DvrLocation> listByBusId(Long busId, Long startTime, Long endTime);
}
