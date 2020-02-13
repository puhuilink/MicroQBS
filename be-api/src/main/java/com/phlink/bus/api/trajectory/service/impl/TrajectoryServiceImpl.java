package com.phlink.bus.api.trajectory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goebl.simplify.Point;
import com.goebl.simplify.Simplify;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.bus.service.IDvrLocationService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.map.domain.Points;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.trajectory.dao.TrajectoryMapper;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import com.phlink.bus.api.trajectory.domain.TrajectoryDeleteVO;
import com.phlink.bus.api.trajectory.domain.TrajectoryPoint;
import com.phlink.bus.api.trajectory.domain.TrajectoryVO;
import com.phlink.bus.api.trajectory.service.ITrajectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhouyi
 */
@Service
public class TrajectoryServiceImpl extends ServiceImpl<TrajectoryMapper, Trajectory> implements ITrajectoryService {

    @Autowired
    private IMapAmapService mapAmapService;
    @Autowired
    private IBusService busService;
    @Autowired
    private IDvrLocationService dvrLocationService;

    @Override
    public Trajectory detail(Long id, Boolean simple) throws BusApiException {
        Trajectory trajectory = this.getById(id);
        if (trajectory == null) {
            throw new BusApiException("该轨迹不存在");
        }
        //查询高德轨迹
        this.mapAmapService.getTrajectory(trajectory, true);
        if (simple) {
            // 转换points
            if(trajectory.getPoints() == null) {
                return trajectory;
            }
            List<Points> pointsList = trajectory.getPoints().toJavaList(Points.class);
            if(pointsList.size() == 0) {
                return trajectory;
            }
            Point[] allPoints = new Point[pointsList.size()];
            for (int i = 0; i < pointsList.size(); i++) {
                String[] pointStr = pointsList.get(i).getLocation().split(",");
                TrajectoryPoint tp = new TrajectoryPoint(Float.parseFloat(pointStr[0]), Float.parseFloat(pointStr[1]));
                allPoints[i] = tp;
            }
            Simplify<Point> simplify = new Simplify<>(new TrajectoryPoint[0]);

            double tolerance = 0.0001;
            boolean highQuality = false;

            Point[] lessPoints = simplify.simplify(allPoints, tolerance, highQuality);

            double[][] simplePoints = new double[lessPoints.length][];
            for (int i = 0; i < lessPoints.length; i++) {
                double[] p = new double[2];
                p[0] = lessPoints[i].getX();
                p[1] = lessPoints[i].getY();
                simplePoints[i] = p;
            }

            trajectory.setSimplePoints(simplePoints);
            trajectory.setPoints(null);
        }
        return trajectory;
    }

    @Override
    public IPage<Trajectory> listTrajectorys(QueryRequest request, TrajectoryVO trajectoryVO) {
        Page<Trajectory> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.baseMapper.listTrajectorys(page, trajectoryVO);
    }

    @Override
    @Transactional
    public void createTrajectory(Trajectory trajectory) {
        trajectory.setCreateTime(LocalDateTime.now());
//        trajectory.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(trajectory);
    }

    @Override
    @Transactional
    public void createTrajectory(Long busId, String trname, Long startTimestamp, Long endTimestamp) throws BusApiException {
        Bus bus = busService.getById(busId);
        if (bus == null) {
            throw new BusApiException("车辆不存在");
        }
        // 获得坐标点
        List<DvrLocation> dvrLocations = dvrLocationService.listByBusId(busId, startTimestamp, endTimestamp);
        if (dvrLocations == null || dvrLocations.isEmpty()) {
            throw new BusApiException("该时间段内没有坐标点无法生成轨迹");
        }
        Trajectory trajectory = new Trajectory();
        trajectory.setName(trname);
        trajectory.setStartTime(LocalDateTime.ofEpochSecond(startTimestamp / 1000, 0, ZoneOffset.ofHours(8)));
        trajectory.setEndTime(LocalDateTime.ofEpochSecond(endTimestamp / 1000, 0, ZoneOffset.ofHours(8)));
        trajectory.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        // 车辆相关信息
        trajectory.setTid(bus.getTid());
        trajectory.setBusId(bus.getId());
        trajectory.setBusCode(bus.getBusCode());
        trajectory.setEngineModel(bus.getEngineModel());
        trajectory.setNumberPlate(bus.getNumberPlate());

        // 保存时间
        int pointSize = dvrLocations.size();
        if (pointSize > 1) {
            Long startTime = dvrLocations.get(0).getGpstime();
            Long endTime = dvrLocations.get(pointSize - 1).getGpstime();
            trajectory.setDuration((endTime - startTime) / 1000);
        }

        // 上传坐标点
        trajectory.setCounts((long) pointSize);

        createAndUpToMap(trajectory);
        List<Points> pointsList = dvrLocations.stream().map(l -> {

            Points points = new Points();
            points.setLocation(String.format("%s,%s", l.getGlon(), l.getGlat()));
            points.setSpeed(l.getSpeed().doubleValue());
            points.setDirection(l.getDirection().doubleValue());
            points.setLocatetime(l.getGpstime());
            return points;
        }).collect(Collectors.toList());

        mapAmapService.uploadTrajectory(trajectory.getTid(), trajectory.getTrid(), pointsList);
    }

    @Override
    public List<Trajectory> listByBusId(Long busId) {
        LambdaQueryWrapper<Trajectory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Trajectory::getBusId, busId);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional
    public void modifyTrajectory(Trajectory trajectory) {
        this.updateById(trajectory);
    }

    @Override
    @Transactional
    public void deleteTrajectorys(List<TrajectoryDeleteVO> trajectoryDeleteVOList) throws BusApiException {
        List<Long> idList = new ArrayList<>();
        for (TrajectoryDeleteVO trajectoryDeleteVO : trajectoryDeleteVOList) {
            idList.add(trajectoryDeleteVO.getId());
            mapAmapService.deleteTrajectory(trajectoryDeleteVO.getTid(), trajectoryDeleteVO.getTrid());
        }
        this.removeByIds(idList);
    }

    @Override
    public Trajectory getByBusIdOnTrip(Long busId) {
        return baseMapper.getByBusIdOnTrip(busId);
    }

    @Override
    @Transactional
    public void createAndUpToMap(Trajectory trajectory) throws BusApiException {
        Long trid = mapAmapService.createTrajectory(trajectory.getTid());
        trajectory.setTrid(trid);
        createTrajectory(trajectory);
    }

    @Override
    public Trajectory getByBusId(Long busId) {
        return baseMapper.getByBusIdOnBindRoute(busId);
    }

    @Override
    public Trajectory getByTripLogId(Long tripLogId) {
        LambdaQueryWrapper<Trajectory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Trajectory::getTripLogId, tripLogId);
        return this.getOne(queryWrapper);
    }
}
