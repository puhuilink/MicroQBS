package com.phlink.bus.api.bus.manager;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.alarm.service.IBusAlarmService;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.Dvr;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.domain.DvrServer;
import com.phlink.bus.api.bus.response.DvrRtmpResponse;
import com.phlink.bus.api.bus.response.DvrStatusResponse;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.bus.service.IDvrLocationService;
import com.phlink.bus.api.bus.service.IDvrServerService;
import com.phlink.bus.api.bus.service.IDvrService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.lisenter.service.CacheDeviceInfoService;
import com.phlink.bus.api.common.properties.BusApiProperties;
import com.phlink.bus.api.common.utils.SpringContextUtil;
import com.phlink.bus.api.map.domain.Points;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.route.domain.TripLog;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.route.service.ITripLogService;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import com.phlink.bus.api.trajectory.service.ITrajectoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ZaDvrManager {

    private static final String LOGIN_URL = "/web/login";
    private static final String DVR_STATUS_URL = "/web/doAction/getDevStatus";
    private static final String DVR_RTMP_URL = "/web/doAction/sendCommand";
    private static final Integer DIVIDE_SCALE = 6;
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BusApiProperties busApiProperties;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IDvrLocationService dvrLocationService;
    @Autowired
    private CacheDeviceInfoService cacheDeviceInfoService;
    @Autowired
    private IDvrService dvrService;
    @Autowired
    private IDvrServerService dvrServerService;
    @Autowired
    private IBusAlarmService busAlarmService;
    @Autowired
    private IBusService busService;
    @Autowired
    private ITrajectoryService trajectoryService;
    @Autowired
    private IMapAmapService mapAmapService;
    @Lazy
    @Autowired
    private ITripService tripService;
    @Autowired
    private ITripLogService tripLogService;

    @Async
    @Scheduled(cron = "${bus-api.dvr.cron}")
    public void loginToDvrServer() {
        if(SpringContextUtil.isPro()) {
            List<DvrServer> dvrServers = dvrServerService.list();
            dvrServers.forEach(server -> {
                loginToDvrServer(server.getHost(), server.getPort());
            });
        }
    }

    @Async
    public void loginToDvrServer(String host, String port) {
        String loginUrl = String.format("http://%s:%s%s", host, port, LOGIN_URL);
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("account", busApiProperties.getDvr().getAccount());
        postParameters.add("password", busApiProperties.getDvr().getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", CONTENT_TYPE);
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(postParameters, headers);

        ResponseEntity<String> responseEntity1 = this.restTemplate.postForEntity(loginUrl, formEntity, String.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        String token = responseEntity1.getBody();
        log.info("post token: {}", token);
        log.info("post statusCode: {}", statusCode);
        redissonClient.getBucket(getCacheTokenKey(host, port), new StringCodec()).set(token);
    }

    private String getCacheTokenKey(String host, String port) {
        return String.format("%s.%s.%s", BusApiConstant.DVR_TOKEN_PREFIX, host, port);
    }

    /**
     * @Description: 获取dvr信息
     * @Param: []
     * @Return: java.lang.String
     * @Author wen
     * @Date 2019-09-19 11:57
     */
    @Async
    @Scheduled(cron = "*/${bus-api.dvr.frequency} * * * * ?")
    public void fetchDvrStatus() {
        if(SpringContextUtil.isPro()) {
            List<DvrServer> dvrServers = dvrServerService.list();
            dvrServers.forEach(server -> {
                fetchDvrStatus(server.getHost(), server.getPort());
            });
        }
    }

    @Async
    public List<DvrLocation> fetchDvrStatus(String host, String port) {
        String token = getDvrServerToken(host, port);
        if (token == null) return null;

        String statusUrl = String.format("http://%s:%s%s?jsession=%s", host, port, DVR_STATUS_URL, token);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", CONTENT_TYPE);
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(headers);

        ResponseEntity<DvrStatusResponse[]> responseEntity1 = this.restTemplate.getForEntity(statusUrl, DvrStatusResponse[].class, formEntity);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        DvrStatusResponse[] entityBody = responseEntity1.getBody();
        log.info("post entityBody: {}", entityBody);
        log.info("post statusCode: {}", statusCode);
        if (entityBody == null) {
            log.error("DVR 服务器返回空");
            return Collections.EMPTY_LIST;
        }
        List<DvrLocation> dvrLocations = Arrays.stream(entityBody).map(d -> {
            if(d.getGlat() < 4 || d.getGlat() > 53) {
                log.debug("坐标点不在国内 {},{}", d.getGlon(), d.getGlat());
                return null;
            }
            if(d.getGlon() < 73 || d.getGlon() > 135) {
                log.debug("坐标点不在国内 {},{}", d.getGlon(), d.getGlat());
                return null;
            }
            // 判断是否是最新的数据
            RBucket<Long> lastTimeBucket = redissonClient.getBucket(BusApiConstant.DVR_LAST_TIME_PREFIX + d.getDevNo(), new LongCodec());
            Long lastTime = lastTimeBucket.get();
            // 更新dvr状态
            DvrLocation dvrLocation = buildDvrLocation(d);

            if (lastTime != null && lastTime.equals(d.getGpstime())) {
                return null;
            }
            // 设置最新的采集时间
            lastTimeBucket.set(d.getGpstime());
            // 保存到redis
            cacheDeviceInfoService.saveDvrInfo(dvrLocation);
            // 通知告警模块
            if(dvrLocation.getBusId() != null) {
                busAlarmService.alarmBusMapping(dvrLocation);
            }
            return dvrLocation;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        dvrLocationService.saveBatch(dvrLocations);

        Map<String, TripLog> tripLogCache = new HashedMap();
        dvrLocations.forEach(d -> {
            // 上传轨迹
            String busCode = d.getBusCode();
            TripLog tripLog = tripLogCache.get(busCode);
            if(tripLog == null) {
                TripState tripState = tripService.getCurrentRunningTrip(busCode);
                if(tripState == null) {
                    return;
                }
                tripLog = tripLogService.getByTripId(tripState.getId(), tripState.getBusDetailInfo().getBindBusTeacherId());
                if(tripLog == null) {
                    return;
                }
            }

            LocalTime beginTime = tripLog.getTripBeginTime();
            LocalTime endTime = tripLog.getTripEndTime();
            if(endTime != null) {
                Duration duration = Duration.between(endTime, LocalTime.now());
                if(duration.toMinutes() > 30) {
                    //  超过30分钟之后再停止上传
                    return;
                }
            }
            Trajectory trajectory = trajectoryService.getByTripLogId(tripLog.getId());
            if (trajectory != null) {
                Points points = new Points();
                points.setLocation(String.format("%s,%s", d.getGlon(), d.getGlat()));
                points.setSpeed(d.getSpeed().doubleValue());
                points.setDirection(d.getDirection().doubleValue());
                points.setLocatetime(d.getGpstime());
                // 这里会超QPS
                try {
                    mapAmapService.uploadTrajectory(d.getTid(), trajectory.getTrid(), Collections.singletonList(points));
                    // 1秒5次QPS，这里暂时设置250毫秒一次请求
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (BusApiException | InterruptedException e) {
                    log.error("上传单个轨迹点异常", e);
                }
            }
        });

        return dvrLocations;
    }

    public DvrLocation buildDvrLocation(DvrStatusResponse response) {

        DvrLocation dvrLocation = new DvrLocation();
        dvrLocation.setAltitude(BigDecimal.valueOf(response.getAltitude()));
        dvrLocation.setBlat(BigDecimal.valueOf(response.getBlat()));
        dvrLocation.setBlon(BigDecimal.valueOf(response.getBlon()));
        dvrLocation.setDirection(response.getDirection());
        dvrLocation.setDvrno(response.getDevNo());
        dvrLocation.setGlat(BigDecimal.valueOf(response.getGlat()));
        dvrLocation.setGlon(BigDecimal.valueOf(response.getGlon()));
        dvrLocation.setGpstime(response.getGpstime());
        dvrLocation.setLat(response.getLat());
        dvrLocation.setLon(response.getLon());
        dvrLocation.setMileage(BigDecimal.valueOf(response.getMileage()).divide(new BigDecimal(10), DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP));
        dvrLocation.setOdbspeed(BigDecimal.valueOf(response.getOdbspeed()).divide(new BigDecimal(10), DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP));
        dvrLocation.setOnline(response.getOnline());
        dvrLocation.setOil(BigDecimal.valueOf(response.getOil()).divide(new BigDecimal(10), DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP));
        dvrLocation.setSatellites(response.getSatellites());
        dvrLocation.setSpeed(BigDecimal.valueOf(response.getSpeed()).divide(new BigDecimal(10), DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP));
        dvrLocation.setStatus1(response.getStatus1());
        dvrLocation.setStatus2(response.getStatus2());
        dvrLocation.setStatus3(response.getStatus3());
        dvrLocation.setStatus4(response.getStatus4());
        dvrLocation.setCreateTime(LocalDateTime.now());

        // 更新DVR设备状态
        Dvr dvr = dvrService.getByDvrCode(response.getDevNo());
        if(dvr != null) {
            dvr.setOnline(response.getOnline());
            dvrService.updateById(dvr);
            Bus bus = busService.getById(dvr.getBusId());
            if (bus != null) {
                dvrLocation.setBusId(bus.getId());
                dvrLocation.setBusCode(bus.getBusCode());
                dvrLocation.setTid(bus.getTid());
            }
        }
        return dvrLocation;
    }

    @Async
    public CompletableFuture<DvrRtmpResponse> getDvrRtmpInfoAsync(Integer channel, Integer dataType, String dvrno, Integer streamType, DvrServer dvrServer) throws BusApiException {

        CompletableFuture<DvrRtmpResponse> future = new CompletableFuture<DvrRtmpResponse>() {
            @Override
            public DvrRtmpResponse get() {
                return getDvrRtmpInfo(channel, dataType, dvrno, streamType, dvrServer);
            }
        };
        return future;
    }

    public DvrRtmpResponse getDvrRtmpInfo(Integer channel, Integer dataType, String dvrno, Integer streamType, DvrServer dvrServer) {

        String token = getDvrServerToken(dvrServer.getHost(), dvrServer.getPort());
        if (token == null) return null;
        DvrRtmpRequest request = new DvrRtmpRequest(channel, dataType, dvrno, streamType, "ReqAVStream");

        String rtmpUrl = String.format("http://%s:%s%s?jsession=%s", dvrServer.getHost(), dvrServer.getPort(), DVR_RTMP_URL, token);
        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("params", JSON.toJSONString(request));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", CONTENT_TYPE);
        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(postParameters, headers);

        ResponseEntity<DvrRtmpResponse> responseEntity1 = this.restTemplate.postForEntity(rtmpUrl, formEntity, DvrRtmpResponse.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        DvrRtmpResponse responseBody = responseEntity1.getBody();
        log.info("post responseBody: {}", responseBody);
        log.info("post statusCode: {}", statusCode);
        responseBody.setChannelCode(channel);
        return responseBody;
    }

    public String getDvrServerToken(String host, String port) {
        RBucket<String> bucket = redissonClient.getBucket(getCacheTokenKey(host, port), new StringCodec());
        if (bucket == null) {
            log.error("DVR服务未登录");
            return null;
        }
        String token = bucket.get();
        if (StringUtils.isBlank(token)) {
            log.error("DVR服务未登录, " + bucket.getName() + " TOKEN 为空");
//            loginToDvrServer();
            return null;
        }
        return token;
    }

}
