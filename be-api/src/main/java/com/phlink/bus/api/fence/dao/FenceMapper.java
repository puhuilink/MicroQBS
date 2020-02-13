package com.phlink.bus.api.fence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.alarm.domain.CodeFence;
import com.phlink.bus.api.fence.domain.Fence;
import com.phlink.bus.api.fence.domain.FenceEntitysVO;
import com.phlink.bus.api.fence.domain.FenceViewVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhouyi
 */
public interface FenceMapper extends BaseMapper<Fence> {


    void appendJob(@Param("fenceId") Long fenceId, @Param("jobId") Long jobId);

    void removeJob(@Param("fenceId") Long fenceId, @Param("jobId") Long jobId);

    List<String> listStopFences(@Param("routeId") Long routeId);

    Page<Fence> listFecnce(Page page, @Param("fenceViewVO") FenceViewVO fenceViewVO);

    FenceEntitysVO getFenceEntitys(@Param("rounteId") Long rounteId);

    List<CodeFence> listBusRouteFence();

    List<CodeFence> listBusStopFence();

    List<CodeFence> listSchoolFence();

    List<Fence> listByIdsIncludeDelete(@Param("ids") Long[] ids);
}
