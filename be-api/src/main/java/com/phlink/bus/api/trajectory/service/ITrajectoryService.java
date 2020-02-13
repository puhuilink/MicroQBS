package com.phlink.bus.api.trajectory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import com.phlink.bus.api.trajectory.domain.TrajectoryDeleteVO;
import com.phlink.bus.api.trajectory.domain.TrajectoryVO;

import java.util.List;

/**
 * @author zhouyi
 */
public interface ITrajectoryService extends IService<Trajectory> {


    /**
     * 获取详情
     */
    Trajectory detail(Long id, Boolean simple) throws BusApiException;

    /**
     * 查询列表
     *
     * @param request
     * @param trajectoryVO
     * @return
     */
    IPage<Trajectory> listTrajectorys(QueryRequest request, TrajectoryVO trajectoryVO);

    /**
     * 生成轨迹
     *
     * @param trajectory
     */
    void createTrajectory(Trajectory trajectory);

    /**
     * 修改
     *
     * @param trajectory
     */
    void modifyTrajectory(Trajectory trajectory);

    /**
     * 批量删除
     *
     * @param trajectoryDeleteVOList
     */
    void deleteTrajectorys(List<TrajectoryDeleteVO> trajectoryDeleteVOList) throws BusApiException;

    /**
     * 查询在行程中的轨迹
     * @param busId
     * @return
     */
    Trajectory getByBusIdOnTrip(Long busId);

    /**
     * 创建轨迹并且保存到高德地图
     * @param trajectory
     */
    void createAndUpToMap(Trajectory trajectory) throws BusApiException;

    /**
     * 根据busId获得绑定的轨迹
     * @param busId
     * @return
     */
    Trajectory getByBusId(Long busId);

    /**
     * 根据行程ID获取轨迹
     * @param id
     * @return
     */
    Trajectory getByTripLogId(Long id);

    /**
     * 保存轨迹，并将该时间段内的坐标点上传到高德
     * @param busId
     * @param trname
     * @param startTimestamp
     * @param endTimestamp
     */
    void createTrajectory(Long busId, String trname, Long startTimestamp, Long endTimestamp) throws BusApiException;

    /**
     * 根据车辆ID查询生成的轨迹列表
     * @param busId
     * @return
     */
    List<Trajectory> listByBusId(Long busId);
}
