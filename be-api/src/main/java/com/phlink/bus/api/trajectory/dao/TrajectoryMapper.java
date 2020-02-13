package com.phlink.bus.api.trajectory.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import com.phlink.bus.api.trajectory.domain.TrajectoryVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhouyi
 */
public interface TrajectoryMapper extends BaseMapper<Trajectory> {

    Page<Trajectory> listTrajectorys(Page page, @Param("trajectoryVO") TrajectoryVO trajectoryVO);

    Trajectory getByBusIdOnTrip(@Param("busId") Long busId);

    Trajectory getByBusIdOnBindRoute(@Param("busId") Long busId);
}
