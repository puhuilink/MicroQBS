package com.phlink.bus.api.fence.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.alarm.domain.CodeFence;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.domain.Point;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.CoordinateTransformUtils;
import com.phlink.bus.api.common.utils.CronUtil;
import com.phlink.bus.api.fence.dao.FenceMapper;
import com.phlink.bus.api.fence.domain.*;
import com.phlink.bus.api.fence.domain.enums.ConditionEnum;
import com.phlink.bus.api.fence.domain.enums.FenceTypeEnum;
import com.phlink.bus.api.fence.domain.enums.RelationTypeEnum;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.fence.service.IFenceTimeService;
import com.phlink.bus.api.job.domain.Job;
import com.phlink.bus.api.job.service.JobService;
import com.phlink.bus.api.map.response.AmapResultEntity;
import com.phlink.bus.api.map.response.BaiduFenceListResultEntity;
import com.phlink.bus.api.map.response.BaiduFencePointsResultEntity;
import com.phlink.bus.api.map.response.BaiduPointResultEntity;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.map.service.IMapBaiduService;
import com.phlink.bus.api.notify.event.FenceCreateEvent;
import com.phlink.bus.api.notify.event.FenceDeleteEvent;
import com.phlink.bus.api.notify.event.FenceUpdateEvent;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.route.domain.vo.FenceStopVO;
import com.phlink.bus.api.route.service.IStopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouyi
 */
@Service
public class FenceServiceImpl extends ServiceImpl<FenceMapper, Fence> implements IFenceService {

    @Autowired
    private IMapBaiduService mapBaiduService;
    @Lazy
    @Autowired
    private IMapAmapService mapAmapService;
    @Autowired
    private JobService jobService;
    @Autowired
    private IBusService busService;
    @Autowired
    private IStopService stopService;
    @Autowired
    private IFenceTimeService fenceTimeService;
    @Autowired
    private ApplicationContext context;

    @Override
    public FenceVO findById(Long id) throws BusApiException {
        Fence fence = this.getById(id);
        if(fence == null) {
            throw new BusApiException("围栏不存在");
        }
        FenceVO vo = new FenceVO();
        BeanUtils.copyProperties(fence, vo);
        // 获取围栏生效时间段
        List<FenceTime> fenceTimes = fenceTimeService.listByFenceId(id);
        vo.setMonitorTime(fenceTimes);
        if(!FenceTypeEnum.POLYLINE.equals(fence.getFenceType())) {
            // 高德
            AmapResultEntity resultEntity = mapAmapService.getFence(fence.getFenceId());
            if(resultEntity != null) {
                LinkedHashMap data = (LinkedHashMap) resultEntity.getData();
                Integer total = (Integer) data.get("total_record");
                if(total >= 1) {
                    ArrayList result = (ArrayList) data.get("rs_list");
                    LinkedHashMap content = (LinkedHashMap) result.get(0);
                    if(FenceTypeEnum.POLYGON.equals(vo.getFenceType())) {
                        String points = (String) content.get("points");
                        String[] pointsArray = points.split(";");
                        Double[][] pointsArray2 = new Double[pointsArray.length][];
                        for(int i=0;i<pointsArray.length;i++) {
                            Double[] doublePoints = new Double[2];
                            String[] strPoings = pointsArray[i].split(",");
                            doublePoints[0] = Double.parseDouble(strPoings[0]);
                            doublePoints[1] = Double.parseDouble(strPoings[1]);
                            pointsArray2[i] = doublePoints;
                        }
                        vo.setVertexes(pointsArray2);
                    }
                    if(FenceTypeEnum.POLYGON.equals(vo.getFenceType())) {
                        vo.setCenter((String) content.get("center"));
                        vo.setRadius(((Double) content.get("radius")).intValue());
                    }
                }
            }
        }else{
            // 百度
            BaiduFenceListResultEntity resultEntity = mapBaiduService.getFence(fence.getFenceId());
            if(resultEntity != null) {
                if(resultEntity.getTotal() != null && resultEntity.getTotal() == 1) {
                    List<BaiduFencePointsResultEntity> pointsResultEntities = resultEntity.getFences();
                    List<BaiduPointResultEntity> pointResultEntity = pointsResultEntities.get(0).getVertexes();
                    Double[][] points = new Double[pointResultEntity.size()][];
                    for (int i=0;i<pointResultEntity.size();i++) {
                        Double[] p = new Double[2];
                        // 转为高德系坐标
                        Point bd09ToGcj02 = CoordinateTransformUtils.bd09ToGcj02(pointResultEntity.get(i).getLongitude(), pointResultEntity.get(i).getLatitude());
                        p[0] = bd09ToGcj02.getLng();
                        p[1] = bd09ToGcj02.getLat();
                        points[i] = p;
                    }
                    vo.setVertexes(points);
                }
            }
        }

        return vo;
    }

    @Override
    public IPage<Fence> listFences(QueryRequest request, FenceViewVO fenceViewVO) {
        Page<Fence> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.baseMapper.listFecnce(page, fenceViewVO);
    }

    @Override
    @Transactional
    public Fence createFence(FenceVO fenceVO) throws BusApiException {
        Fence fence = new Fence();
        fence.setCreateTime(LocalDateTime.now());
        fence.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        //默认全部触发
        fence.setAlertCondition(ConditionEnum.ENTERORLEAVE);
        fence.setDeleted(false);
        if(FenceTypeEnum.POLYLINE.equals(fenceVO.getFenceType())) {
            // 路线围栏
            if(fenceVO.getVertexes().length > 100) {
                throw new BusApiException("坐标点数量不能超过100");
            }
        }
        saveFence(fenceVO, fence);
        // 给后续更新操作传ID
        fenceVO.setId(fence.getId());
        // 异步执行围栏其他操作
        context.publishEvent(new FenceCreateEvent(this, fenceVO));
        return fence;
    }

    @Override
    @Transactional
    public void modifyFence(FenceVO fenceVO) throws BusApiException {
        Fence fence = baseMapper.selectById(fenceVO.getId());
        if(fence == null) {
            throw new BusApiException("围栏不存在");
        }
        if(FenceTypeEnum.POLYLINE.equals(fence.getFenceType())) {
            // 路线围栏
            if(fenceVO.getVertexes().length > 100) {
                throw new BusApiException("路线围栏坐标点数量不能超过100");
            }
            fence.setVertexes(JSON.toJSONString(fenceVO.getVertexes()));
            fence.setVertexesNum(fenceVO.getVertexes().length);
        }
        if(FenceTypeEnum.CIRCLE.equals(fence.getFenceType())) {
            // 圆形围栏
            fence.setCenter(fenceVO.getCenter());
            fence.setRadius(fenceVO.getRadius());
        }else {
            fence.setCenter(null);
            fence.setRadius(null);
        }
        if(FenceTypeEnum.POLYGON.equals(fence.getFenceType())) {
            // 多边形围栏
            if(fenceVO.getVertexes() != null) {
                fence.setVertexes(JSON.toJSONString(fenceVO.getVertexes()));
                fence.setVertexesNum(fenceVO.getVertexes().length);
            }
        }else {
            fence.setVertexes(null);
            fence.setVertexesNum(0);
        }
        fence.setModifyTime(LocalDateTime.now());
        fence.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        // 更新围栏基本信息
        fence.setAlertCondition(fenceVO.getAlertCondition());
        fence.setFenceName(fenceVO.getFenceName());
        fence.setFenceType(fenceVO.getFenceType());
        fence.setOffsets(fenceVO.getOffsets());
        fence.setRelationId(fenceVO.getRelationId());
        fence.setRelationType(fenceVO.getRelationType());
        fence.setStopFence(fenceVO.getStopFence());
        this.saveOrUpdate(fence);
        // 更新fenceTime
        if(fenceVO.getMonitorTime() != null) {
            saveOrUpdateFenceTime(fenceVO.getMonitorTime(), fence.getId());
        }
        // 设置围栏ID
        fenceVO.setFenceId(fence.getFenceId());
        // 异步执行围栏其他操作
        context.publishEvent(new FenceUpdateEvent(this, fenceVO));
    }

    @Override
    @Transactional
    public void deleteFences(String[] fids) {
        //删除电子围栏
        List<Long> fenceIds = Stream.of(fids)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(fenceIds);
        // 异步执行删除围栏操作
        context.publishEvent(new FenceDeleteEvent(this, fenceIds));
    }

    @Override
    public List<FenceCronJob> saveFenceJob(String fenceJob, String fenceId) {
        List<FenceCronJob> fenceCronJobList = JSON.parseArray(fenceJob, FenceCronJob.class);
        List<FenceCronJob> newFenceCronList = new ArrayList<>();
        // 删除该围栏的旧job
        this.jobService.deleteJobByParam(fenceId);
        for (FenceCronJob cron : fenceCronJobList) {
            Job startJob = new Job();
            startJob.setCreateTime(new Date());
            startJob.setStatus("0");
            startJob.setBeanName("task");
            startJob.setCronExpression(CronUtil.localTimeToCron(cron.getStartCron()));
            startJob.setMethodName("runTask");
            startJob.setParams(fenceId);
            startJob.setRemark("电子围栏启动任务，终端id(地图)：" + fenceId);
            this.jobService.saveJob(startJob, cron.getDeleted());
            cron.setStartJobId(startJob.getJobId());
            Job stopJob = new Job();
            stopJob.setCreateTime(new Date());
            stopJob.setStatus("0");
            stopJob.setBeanName("task");
            stopJob.setCronExpression(CronUtil.localTimeToCron(cron.getStopCron()));
            stopJob.setMethodName("stopTask");
            stopJob.setParams(fenceId);
            stopJob.setRemark("电子围栏停止任务，终端id(地图)：" + fenceId);
            this.jobService.saveJob(stopJob, cron.getDeleted());
            cron.setStopJobId(stopJob.getJobId());
            newFenceCronList.add(cron);
        }
        return newFenceCronList;
    }

    @Deprecated
    private JSONArray createStopFenceJob(String fenceJob, String fenceId) {
        List<FenceCronJob> fenceCronJobList = JSON.parseArray(fenceJob, FenceCronJob.class);
        List<FenceCronJob> newFenceCronList = new ArrayList<>();
        for (FenceCronJob cron : fenceCronJobList) {
            Job startJob = new Job();
            startJob.setCreateTime(new Date());
            startJob.setStatus("0");
            startJob.setBeanName("task");
            startJob.setCronExpression(CronUtil.localTimeToCron(cron.getStartCron()));
            startJob.setMethodName("runTask");
            startJob.setParams(fenceId);
            startJob.setRemark("电子围栏启动任务，终端id(地图)：" + fenceId);
            this.jobService.createJob(startJob);
            cron.setStartJobId(startJob.getJobId());
            Job stopJob = new Job();
            stopJob.setCreateTime(new Date());
            stopJob.setStatus("0");
            stopJob.setBeanName("task");
            stopJob.setCronExpression(CronUtil.localTimeToCron(cron.getStopCron()));
            stopJob.setMethodName("stopTask");
            stopJob.setParams(fenceId);
            stopJob.setRemark("电子围栏停止任务，终端id(地图)：" + fenceId);
            this.jobService.createJob(stopJob);
            cron.setStopJobId(stopJob.getJobId());
            newFenceCronList.add(cron);
        }
        return JSONArray.parseArray(JSON.toJSONString(newFenceCronList));
    }

    private void saveOrUpdateFenceTime(List<FenceTime> fenceTimes, Long fenceId) {
        fenceTimeService.deleteByFenceId(fenceId);
        for (FenceTime fenceTime : fenceTimes) {
            fenceTime.setFenceId(fenceId);
            this.fenceTimeService.save(fenceTime);
        }
    }

    private void saveFence(FenceVO fenceVO, Fence fence) {
        fence.setDeleted(fenceVO.getDeleted());
        fence.setId(fenceVO.getId());
        fence.setCenter(fenceVO.getCenter());
        fence.setAlertCondition(fenceVO.getAlertCondition());
        fence.setFenceName(fenceVO.getFenceName());
        fence.setFenceType(fenceVO.getFenceType());
        fence.setOffsets(fenceVO.getOffsets());
        fence.setRadius(fenceVO.getRadius());
        fence.setRelationId(fenceVO.getRelationId());
        fence.setRelationType(fenceVO.getRelationType());
        fence.setStopFence(fenceVO.getStopFence());
        if(fenceVO.getVertexes() != null) {
            fence.setVertexes(JSON.toJSONString(fenceVO.getVertexes()));
            fence.setVertexesNum(fenceVO.getVertexes().length);
        }
        this.saveOrUpdate(fence);
        // 保存时间段
        if(fenceVO.getMonitorTime() != null) {
            saveOrUpdateFenceTime(fenceVO.getMonitorTime(), fence.getId());
        }
    }

    @Deprecated
    @Override
    @Transactional
    public void createStopFence(FenceStopVO fenceStopVO, Long routeId) {
        FenceVO fenceVO = new FenceVO();
        fenceVO.setCenter(fenceStopVO.getCenter());
        //默认全部
        fenceVO.setAlertCondition(ConditionEnum.ENTERORLEAVE);
        //默认半径100米
        fenceVO.setRadius(100);
        fenceVO.setFenceName(fenceStopVO.getFenceName());
        fenceVO.setRelationId(fenceStopVO.getId());
        fenceVO.setRelationType(RelationTypeEnum.STOP);
        //圆形围栏
        fenceVO.setFenceType(FenceTypeEnum.CIRCLE);
        fenceVO.setDeleted(false);
        fenceVO.setCreateTime(LocalDateTime.now());
        fenceVO.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        try {
            fenceVO.setFenceId(this.mapAmapService.createFence(fenceVO));
        } catch (Exception e) {
            log.error("创建站点围栏失败，路线id：" + routeId + ",站点名称：" + fenceStopVO.getFenceName() + e.toString());
        }
        Fence fence = new Fence();
        fence.setCreateTime(LocalDateTime.now());
        fence.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        fence.setDeleted(false);
        //创建定时任务
        QueryWrapper<Fence> query = new QueryWrapper<>();
        query.lambda().eq(Fence::getRelationId, routeId);
        query.lambda().eq(Fence::getRelationType, RelationTypeEnum.ROUTE);
        Fence newfence = this.getOne(query);
        if(newfence.getJobs() != null) {
            JSONArray jobCron = this.createStopFenceJob(newfence.getJobs().toJSONString(), String.valueOf(fenceStopVO.getId()));
            fence.setJobs(jobCron);
        }
        saveFence(fenceVO, fence);
    }

    @Override
    @Transactional
    public void createStopFence(FenceStopVO fenceStopVO, FenceVO routeFenceVO) {
        FenceVO fenceVO = new FenceVO();
        fenceVO.setCenter(fenceStopVO.getCenter());
        //默认全部
        fenceVO.setAlertCondition(ConditionEnum.ENTERORLEAVE);
        //默认半径100米
        fenceVO.setRadius(100);
        fenceVO.setFenceName(fenceStopVO.getFenceName());
        fenceVO.setRelationId(fenceStopVO.getId());
        fenceVO.setRelationType(RelationTypeEnum.STOP);
        //圆形围栏
        fenceVO.setFenceType(FenceTypeEnum.CIRCLE);
        fenceVO.setDeleted(false);
        fenceVO.setCreateTime(LocalDateTime.now());
        fenceVO.setCreateBy(routeFenceVO.getCreateBy());
        try {
            fenceVO.setFenceId(this.mapAmapService.createFence(fenceVO));
        } catch (Exception e) {
            log.error("创建站点围栏失败，路线id：" + routeFenceVO.getRelationId() + ",站点名称：" + fenceStopVO.getFenceName() + e.toString());
        }
        Fence fence = new Fence();
        fence.setCreateTime(LocalDateTime.now());
        fence.setCreateBy(routeFenceVO.getCreateBy());
        fence.setDeleted(false);
        //创建定时任务
        if(routeFenceVO.getJobs() != null) {
            JSONArray jobCron = this.createStopFenceJob(routeFenceVO.getJobs().toJSONString(), String.valueOf(fenceStopVO.getId()));
            fence.setJobs(jobCron);
        }
        saveFence(fenceVO, fence);
    }

    @Override
    @Transactional
    public void deleteStopFences(List<Long> stopIds) {
        for (Long stopId : stopIds) {
            deleteStopFence(stopId);
        }
    }

    @Override
    @Transactional
    public void deleteStopFence(Long stopId) {
        QueryWrapper<Fence> query = new QueryWrapper<>();
        query.lambda().eq(Fence::getRelationType, RelationTypeEnum.STOP);
        query.lambda().eq(Fence::getRelationId, stopId);
        Fence fence = this.getOne(query);
        //删除站点定时任务
        this.jobService.deleteJobByParam(fence.getFenceId());
        this.removeById(fence.getId());

    }

    @Override
    public List<Fence> getRouteFence() {
        QueryWrapper<Fence> query = new QueryWrapper<>();
        query.lambda().eq(Fence::getDeleted, false);
        query.lambda().eq(Fence::getRelationType, RelationTypeEnum.ROUTE);
        return this.list(query);
    }

    @Override
    public List<CodeFence> getBusRouteFence() {
        return this.baseMapper.listBusRouteFence();
    }

    @Override
    public List<CodeFence> getBusStopFence() {
        return this.baseMapper.listBusStopFence();
    }

    @Override
    public List<CodeFence> getSchoolFence() {
        return this.baseMapper.listSchoolFence();
    }

    @Override
    public Boolean checkFenceByRelation(Long relationId, RelationTypeEnum relationType) {
        QueryWrapper<Fence> query = new QueryWrapper<>();
        query.lambda().eq(Fence::getDeleted, false);
        query.lambda().eq(Fence::getRelationType, relationType);
        query.lambda().eq(Fence::getRelationId, relationId);
        int count = this.count(query);
        if (count == 0) {
            return false;
        }
        return true;
    }

    @Override
    public Long getRelationId(String fenceId, RelationTypeEnum relationType) {
        QueryWrapper<Fence> query = new QueryWrapper<>();
        query.lambda().eq(Fence::getDeleted, false);
        query.lambda().eq(Fence::getRelationType, relationType);
        query.lambda().eq(Fence::getFenceId, fenceId);
        Fence fence = this.getOne(query);
        return fence.getRelationId();
    }

    @Override
    public Fence getByName(String fenceName) {
        QueryWrapper<Fence> query = new QueryWrapper<>();
        query.lambda().eq(Fence::getFenceName, fenceName);
        return this.getOne(query);
    }

    @Override
    public List<Fence> listByIdsIncludeDelete(List<Long> fenceIds) {
        if(fenceIds == null || fenceIds.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return this.baseMapper.listByIdsIncludeDelete(fenceIds.toArray(new Long[0]));
    }

    @Override
    @Transactional
    public void updateFenceJob(Long id, List<FenceCronJob> jobs) {
        if(jobs == null || jobs.isEmpty()) {
            return;
        }
        UpdateWrapper<Fence> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(Fence::getId, id);
        wrapper.lambda().set(Fence::getJobs, JSONArray.parseArray(JSON.toJSONString(jobs)));
        this.update(wrapper);
    }

    @Override
    @Transactional
    public void updateFenceId(Long id, String fenceId) {
        UpdateWrapper<Fence> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(Fence::getId, id);
        wrapper.lambda().set(Fence::getFenceId, fenceId);
        this.update(wrapper);
    }

    @Override
    @Transactional
    public void deleteStopFenceByRouteIds(List<Long> routeIds) {
        List<Stop> stops = stopService.listStopByRouteIds(routeIds);
        if(stops == null || stops.isEmpty()) {
            return;
        }
        List<Long> relationIds = stops.stream().map(Stop::getId).collect(Collectors.toList());
        if(relationIds.isEmpty()) {
            return;
        }
        UpdateWrapper<Fence> updatewrapper = new UpdateWrapper<>();
        updatewrapper.lambda().in(Fence::getRelationId, relationIds);
        updatewrapper.lambda().eq(Fence::getRelationType, RelationTypeEnum.STOP);
        updatewrapper.lambda().set(Fence::getDeleted, false);
        this.update(updatewrapper);
    }

    @Override
    @Transactional
    public void updateFenceEnableStatus(@NotBlank String gdFenceId, boolean success) {
        UpdateWrapper<Fence> updatewrapper = new UpdateWrapper<>();
        updatewrapper.lambda().eq(Fence::getFenceId, gdFenceId);
        updatewrapper.lambda().set(Fence::getEnabled, success);
        this.update(updatewrapper);
    }

    @Override
    public List<String> getStopFences(Long routeId) {
        return this.baseMapper.listStopFences(routeId);
    }

    @Override
    @Transactional
    public void modifyFenceEntitys(Long routeId) {
        List<String> list = this.busService.findEntitysByRouteId(routeId);
        String[] entitys = list.toArray(new String[0]);
//        FenceEntitysVO fenceEntitysVO = this.baseMapper.getFenceEntitys(routeId);
        UpdateWrapper<Fence> updatewrapper = new UpdateWrapper<>();
        updatewrapper.lambda().eq(Fence::getRelationId, routeId);
        updatewrapper.lambda().eq(Fence::getRelationType, RelationTypeEnum.ROUTE);
        updatewrapper.lambda().set(Fence::getEntityNames, entitys);
        this.update(updatewrapper);
    }
}
