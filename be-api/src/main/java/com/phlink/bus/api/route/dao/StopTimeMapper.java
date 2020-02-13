package com.phlink.bus.api.route.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.bus.api.route.domain.StopTime;
import com.phlink.bus.api.route.domain.vo.TripStopTimeListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wen
 */
public interface StopTimeMapper extends BaseMapper<StopTime> {

    List<TripStopTimeListVO> listTripStopTimeListVO(@Param("routeId") Long routeId, @Param("busTeacherId") Long busTeacherId);

    StopTime getStopTimeByFenceId(@Param("stopFenceId") String stopFenceId);

    StopTime getFirstStopOnTrip(@Param("tripId") Long tripId);

    StopTime getNextStopTime(@Param("stopId") Long stopId, @Param("tripId") Long tripId);
}
