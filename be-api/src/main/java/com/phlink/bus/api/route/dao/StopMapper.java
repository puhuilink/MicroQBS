package com.phlink.bus.api.route.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.route.domain.vo.FenceStopVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wen
 */
public interface StopMapper extends BaseMapper<Stop> {

    List<FenceStopVO> listUnbindFenceStopInfo(@Param("routeId") Long routeId);
    
    Stop getLastStopByUserId(@Param("userId") Long userId);

    List<Stop> listStopByBindBus(@Param("busId") Long busId);

    List<Stop> listByRouteIds(@Param("routeIds") Long[] routeIds);
}
