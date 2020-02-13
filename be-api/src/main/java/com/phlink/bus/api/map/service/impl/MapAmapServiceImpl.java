package com.phlink.bus.api.map.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.common.utils.HttpUtil;
import com.phlink.bus.api.fence.domain.FenceTime;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.fence.domain.enums.ConditionEnum;
import com.phlink.bus.api.fence.domain.enums.RepeatEnum;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.map.domain.BusApiMapConstant;
import com.phlink.bus.api.map.domain.FenceStatus;
import com.phlink.bus.api.map.domain.Points;
import com.phlink.bus.api.map.response.AmapCoordinateResultEntity;
import com.phlink.bus.api.map.response.AmapDistanceResultEntity;
import com.phlink.bus.api.map.response.AmapGeocodeResultEntity;
import com.phlink.bus.api.map.response.AmapResultEntity;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MapAmapServiceImpl implements IMapAmapService {

    private static final Integer SUCCESS_CODE = 10000;
    private static final Integer FENCE_SUCCESS_CODE = 0;
    private static final Integer EXISTING_ELEMENT_CODE = 20009;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RestTemplate restTemplate;
    @Lazy
    @Autowired
    private IFenceService fenceService;

    @Override
    public Long createAmapEntity(String desc, String name) {
        String message = "创建终端失败";
        Long tid = 0L;
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        try {
            postParameters.add("key", getKey());
            postParameters.add("sid", getSid());
            postParameters.add("name", name);
            postParameters.add("desc", desc);
            AmapResultEntity resultEntity = postFormData(BusApiMapConstant.Amap.CREATE_TERMINAL, postParameters);
            if (resultEntity == null) {
                log.error(", 返回对象为null");
                return tid;
            }
            if (!SUCCESS_CODE.equals(resultEntity.getErrcode())) {
                log.error("{}，错误码：{}，错误码描述：{}，错误详情：{}, 请求:{}", message, resultEntity.getErrcode(), resultEntity.getErrmsg(), resultEntity.getErrdetail(), JSON.toJSONString(postParameters));
                if (EXISTING_ELEMENT_CODE.equals(resultEntity.getErrcode())) {
                    tid = getAmapEntity(name);
                }
                return tid;
            }
            LinkedHashMap<String, Object> data = (LinkedHashMap) resultEntity.getData();
            tid = Long.valueOf(data.get("tid").toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(message + "，请求参数{}", postParameters);
        }
        return tid;
    }

    @Override
    public void deleteAmapEntity(Long tid) {
        String message = "删除终端失败";
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        try {
            postParameters.add("key", getKey());
            postParameters.add("sid", getSid());
            postParameters.add("tid", tid.toString());
            AmapResultEntity resultEntity = postFormData(BusApiMapConstant.Amap.DELETE_TERMINAL, postParameters);
            if (resultEntity == null) {
                log.error("删除终端失败, 返回对象为null");
                return;
            }
            if (!SUCCESS_CODE.equals(resultEntity.getErrcode())) {
                log.error("{}，错误码：{}，错误码描述：{}，错误详情：{}", message, resultEntity.getErrcode(), resultEntity.getErrmsg(), resultEntity.getErrdetail());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(message + "，请求参数{}", postParameters, e);
        }
    }

    @Override
    public Long getAmapEntity(String name) {
        Long tid = 0L;
        String url = "";
        try {
            url = String.format("%s?key=%s&sid=%s&name=%s", BusApiMapConstant.Amap.LIST_TERMINAL, getKey(), getSid(), name);
            AmapResultEntity resultEntity = get(url);
            if (resultEntity == null) {
                log.error("查询终端失败, 返回对象为null");
                return tid;
            }
            if (!SUCCESS_CODE.equals(resultEntity.getErrcode())) {
                log.error("查询终端失败，错误码：{}，错误码描述：{}，错误详情：{}", resultEntity.getErrcode(), resultEntity.getErrmsg(), resultEntity.getErrdetail());
                return tid;
            }
            LinkedHashMap<String, Object> data = (LinkedHashMap) resultEntity.getData();
            Integer count = (Integer) data.get("count");
            if (count >= 1) {
                ArrayList<LinkedHashMap> results = (ArrayList) data.get("results");
                Integer tid2 = (Integer) results.get(0).get("tid");
                tid = tid2.longValue();
            } else {
                log.error("查询终端失败，name={}不存在", name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询终端失败，请求参数{}", url);
        }
        return tid;
    }

    @Override
    public String createFence(FenceVO fenceVO) throws BusApiException {
        String gid = null;
        String message = "创建电子围栏失败";
        Map<String, Object> postParameters = new HashMap<>();
        try {
            postParameters = buildFenceParameters(fenceVO);
            AmapResultEntity resultEntity = postJson(BusApiMapConstant.Amap.FENCE_URL + getKey(), JSON.toJSONString(postParameters));
            if (resultEntity == null) {
                log.error(", 返回对象为null");
                return gid;
            }
            if (!FENCE_SUCCESS_CODE.equals(resultEntity.getErrcode())) {
                log.error("{}，错误码：{}，错误码描述：{}，错误详情：{}, 请求:{}", message, resultEntity.getErrcode(), resultEntity.getErrmsg(), resultEntity.getErrdetail(), JSON.toJSONString(postParameters));
                return gid;
            }
            LinkedHashMap<String, Object> data = (LinkedHashMap) resultEntity.getData();
            gid = data.get("gid").toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(message + "，请求参数{}", postParameters);
        }
        return gid;
    }

    @Override
    public void updateFence(FenceVO fenceVO) throws BusApiException {
        String message = "更新电子围栏失败";
        Map<String, Object> postParameters = new HashMap<>();
        try {
            postParameters = buildFenceParameters(fenceVO);
            updateFenceRequest(fenceVO.getFenceId(), message, JSON.toJSONString(postParameters), "patch");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(message + "，请求参数{}", postParameters);
        }
    }

    private Map<String, Object> buildFenceParameters(FenceVO fenceVO) throws BusApiException {
        Map<String, Object> postParameters = new HashMap<>();
        postParameters.put("name", fenceVO.getFenceName());
        // 围栏有效截止日期，过期后不对此围栏进行维护，不能设定创建围栏时间点之前的日期；
        // 格式yyyy-MM-dd； 请设置2055年之前的日期
        postParameters.put("valid_time", "2054-12-30");
        // 星期缩写列表，用","或“;”隔开
        postParameters.put("repeat", RepeatEnum.EVERYDAY.getValue());
        // 开始时间和结束时间定义一时间段，可设置多个时间段，时间段按照时间顺序排列，各时间段不可重叠；
        List<FenceTime> fenceTimes = fenceVO.getMonitorTime();
        String time = fenceTimes.stream().map( f -> f.getStartTime().toString() + "," + f.getEndTime()).collect(Collectors.joining(";"));
        postParameters.put("time", time);
        if(fenceVO.getAlertCondition() == null) {
            postParameters.put("alert_condition", ConditionEnum.ENTERORLEAVE.getValue());
        }else{
            postParameters.put("alert_condition", fenceVO.getAlertCondition().getValue());
        }
        switch (fenceVO.getFenceType()) {
            case CIRCLE:
                if (StringUtils.isBlank(fenceVO.getCenter())) {
                    throw new BusApiException("电子围栏参数错误，请输入圆形围栏中心点");
                }
                if (fenceVO.getRadius() == null) {
                    throw new BusApiException("电子围栏参数错误，请输入圆形围栏半径");
                }
                if (fenceVO.getRadius() < 0 || fenceVO.getRadius() > 5000) {
                    throw new BusApiException("电子围栏参数错误，圆形围栏半径范围在0~5000");
                }
                postParameters.put("center", fenceVO.getCenter());
                postParameters.put("radius", String.valueOf(fenceVO.getRadius()));
                break;
            case POLYGON:
                if (fenceVO.getVertexes() == null) {
                    throw new BusApiException("电子围栏参数错误，请输入多边形围栏坐标点");
                }
                String points = Arrays.stream(fenceVO.getVertexes()).map(i -> Arrays.stream(i).map(Object::toString).collect(Collectors.joining(","))).collect(Collectors.joining(";"));
                postParameters.put("points", points);
                break;
            default:
                throw new BusApiException("无法识别电子围栏类型，fenceType：" + fenceVO.getFenceType());
        }
        return postParameters;
    }

    @Override
    public AmapResultEntity getFence(String fenceId) throws BusApiException {
        String url = "";
        AmapResultEntity resultEntity = null;
        try {
            url = String.format("%s%s&gid=%s", BusApiMapConstant.Amap.FENCE_URL, getKey(), fenceId);
            resultEntity = get(url);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询围栏失败，请求参数{}", url);
        }
        return resultEntity;
    }

    @Override
    public void deleteFence(String fenceId) {
        String message = "删除电子围栏失败";
        Map<String, Object> postParameters = new HashMap<>();
        updateFenceRequest(fenceId, message, JSON.toJSONString(postParameters), "delete");
    }

    @Override
    @Async
    public void runFence(String fenceId) {
        String message = "启动电子围栏失败";
        Map<String, Object> postParameters = new HashMap<>();
        postParameters.put("enable", "true");
        boolean success = updateFenceRequest(fenceId, message, JSON.toJSONString(postParameters), "patch");
        // 更新围栏状态
        if (success) {
            fenceService.updateFenceEnableStatus(fenceId, true);
        }
    }

    @Override
    @Async
    public void stopFence(String fenceId) {
        String message = "停用电子围栏失败";
        Map<String, Object> postParameters = new HashMap<>();
        postParameters.put("enable", "false");
        boolean success = updateFenceRequest(fenceId, message, JSON.toJSONString(postParameters), "patch");
        if (success) {
            fenceService.updateFenceEnableStatus(fenceId, false);
        }
    }

    private boolean updateFenceRequest(String fenceId, String message, String body, String method) {
        String url = null;
        try {
            url = String.format("%s?key=%s&gid=%s&method=%s", BusApiMapConstant.Amap.FENCE_URL_BASE, getKey(), fenceId, method);
            AmapResultEntity resultEntity = postJson(url, body);
            if (resultEntity == null) {
                log.error("{}, 返回对象为null", message);
                return false;
            }
            if (!FENCE_SUCCESS_CODE.equals(resultEntity.getErrcode())) {
                log.error("{}, {}，返回结果 {}", message, url, resultEntity);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{} url={} parameters={}", message, url, body, e);
            return false;
        }
        return true;
    }

    @Override
    public Long createTrajectory(Long tid) throws BusApiException {
        String message = "创建轨迹失败";
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("tid=").append(tid);
            String url = createRequestUrl();
            buffer.append(url);
            String result = HttpUtil.sendPost(BusApiMapConstant.Amap.CREATE_TRAJECTORY, buffer.toString());
            JSONObject jo = JSON.parseObject(result);
            checkError(jo, message);
            return jo.getJSONObject("data").getLong("trid");
        } catch (Exception e) {
            throw new BusApiException(message + "，终端tid：" + tid);
        }
    }

    @Override
    public void deleteTrajectory(Long tid, Long trid) throws BusApiException {
        String message = "删除轨迹失败";
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("tid=").append(tid);
            buffer.append("&trid=").append(trid);
            String url = createRequestUrl();
            buffer.append(url);
            String result = HttpUtil.sendPost(BusApiMapConstant.Amap.DELETE_TRAJECTORY, buffer.toString());
            JSONObject jo = JSON.parseObject(result);
            checkError(jo, message);
        } catch (Exception e) {
            throw new BusApiException(message + "，终端tid：" + tid + ",轨迹trid:" + trid);
        }
    }

    @Override
    public void getTrajectory(Trajectory trajectory, Boolean isHavePoints) throws BusApiException {
        String message = "获取轨迹数据失败";
        try {

            if (!isHavePoints) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("tid=").append(trajectory.getTid());
                String url = createRequestUrl();
                buffer.append(url);
                buffer.append("&trid=").append(trajectory.getTrid());
                buffer.append("&ispoints=0");
                String result = HttpUtil.sendGet(BusApiMapConstant.Amap.TRSEARCH_URL, buffer.toString());
                JSONObject jo = JSONObject.parseObject(result);
                checkTrajectoryError(jo, message);
                JSONObject tJsonObject = jo.getJSONObject("data");
                trajectory.setCounts(tJsonObject.getLong("counts"));
                trajectory.setDistance(tJsonObject.getLong("distance"));
                return;
            }
            int count = 1;
            List<Points> pointsList = new ArrayList<>();


            while (true) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("tid=").append(trajectory.getTid());
                String url = createRequestUrl();
                buffer.append(url);
                buffer.append("&pagesize=").append(999);
                //补点间距(米)
                buffer.append("&gap=").append(500);
                buffer.append("&trid=").append(trajectory.getTrid());
                buffer.append("&page=").append(count);
//                buffer.append("&correction=denoise=1,mapmatch=1,attribute=1,threshold=0,mode=driving");
//                buffer.append("&recoup=1");
                String result = HttpUtil.sendGet(BusApiMapConstant.Amap.TRSEARCH_URL, buffer.toString());
                JSONObject jo = JSONObject.parseObject(result);
                if(jo.getJSONObject("data") == null) {
                    return;
                }
                JSONObject tJsonObject = jo.getJSONObject("data").getJSONArray("tracks").getJSONObject(0);
                checkTrajectoryError(jo, message);
                trajectory.setCounts(tJsonObject.getLong("counts"));
                long counts = tJsonObject.getLong("counts");
                if (counts == 0) {
                    break;
                }
                trajectory.setDistance(tJsonObject.getLong("distance"));
                pointsList.addAll(JSON.parseArray(tJsonObject.getString("points"), Points.class));
                trajectory.setPoints(JSONArray.parseArray(JSON.toJSONString(pointsList)));
                if (Math.ceil((double) counts / 1000) == count) {
                    return;
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(message + "，终端tid：" + trajectory.getTid() + "轨迹id：" + trajectory.getTrid() + "," + e.toString());
            throw new BusApiException(e.toString());
        }
    }

    @Async
    @Override
    public void uploadTrajectory(Long tid, Long trid, List<Points> pointsList) throws BusApiException {
        List<List<Points>> pointsListGroup = Lists.partition(pointsList, 100);
        String message = "上传高德轨迹数据失败";
        for (List<Points> group : pointsListGroup) {
            try {
                MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
                postParameters.add("tid", String.valueOf(tid));
                postParameters.add("trid", String.valueOf(trid));
                postParameters.add("key", getKey());
                postParameters.add("sid", getSid());
                postParameters.add("points", JSONObject.toJSONString(group));
                AmapResultEntity resultEntity = postFormData(BusApiMapConstant.Amap.UPLOAD_TRAJECTORY, postParameters);
                if (resultEntity == null) {
                    continue;
                }
                if (!SUCCESS_CODE.equals(resultEntity.getErrcode())) {
                    log.error(message + ",错误码{},错误码描述{},错误详情:{},参数:{}", resultEntity.getErrcode(), resultEntity.getErrmsg(), resultEntity.getErrdetail(), postParameters);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(message + "，终端tid：" + tid + "轨迹id：" + trid + "," + e.toString());
                throw new BusApiException(e.toString());
            }
        }
    }


    @Override
    public FenceStatus getAlarmFence(String code, Double lon, Double lat, Long time) throws BusApiException {
        String message = "查询坐标与围栏交互状态失败";
        String timeStr = String.valueOf(time);
        if (timeStr.length() != 10) {
            log.error("{} 时间{}格式错误，时间戳为秒", message, time);
            throw new BusApiException("时间格式错误");
        }
        try {
            String key = getKey();
            String location = this.redisService.hget(BusApiConstant.LOCATION, code);
            String url = "";
            if (StringUtils.isNotBlank(location)) {
                url = String.format("%s?key=%s&diu=868805031619896&locations=%s;%s,%s,%s", BusApiMapConstant.Amap.GET_STATUS_BY_FENCE, key, location, lon, lat, time);
            } else {
                url = String.format("%s?key=%s&diu=868805031619896&locations=%s,%s,%s", BusApiMapConstant.Amap.GET_STATUS_BY_FENCE, key, lon, lat, time);
            }
            AmapResultEntity entity = get(url);
            String data = JSON.toJSONString(entity.getData());
            return JSON.parseObject(data, FenceStatus.class);
        } catch (Exception e) {
            throw new BusApiException(message + "，终端code：" + code, e);
        }
    }

    @Override
    public String getLocationRegeo(BigDecimal longitude, BigDecimal latitude) throws BusApiException {
        String address = "";
        String message = "逆地理编码失败";
        String url = "";
        try {
            String key = getKey();
            url = String.format("%s?key=%s&location=%s,%s&poitype=门牌号|道路|道路交叉路口&radius=100&extensions=all&batch=false&roadlevel=0", BusApiMapConstant.Amap.GET_REGEO, key, longitude.toPlainString(), latitude.toPlainString());
            AmapGeocodeResultEntity entity = get(url, AmapGeocodeResultEntity.class);
            if(entity.requestSuccess()) {
                Map<String, Object> regeocodes = entity.getRegeocode();
                address = (String) regeocodes.get("formatted_address");
            }

        } catch (Exception e) {
            log.error("{}, lon:{}, lat: {}, request-url:{}", message, longitude.toPlainString(), latitude.toPlainString(), url, e);
            throw new BusApiException(message + "，lon：" + longitude.toPlainString() + ", lat: " + latitude.toPlainString());
        }
        return address;
    }

    @Override
    public AmapCoordinateResultEntity convertGpsCoordsys(String longitude, String latitude) throws BusApiException {
        AmapCoordinateResultEntity entity = null;
        String message = "坐标转换失败";
        String url = "";
        try {
            String key = getKey();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BusApiMapConstant.Amap.GET_GAODE_COORDINATE)
                    .queryParam("key", key)
                    .queryParam("locations", longitude+","+latitude)
                    .queryParam("coordsys", "gps")
                    ;
            url = builder.build().encode().toUriString();
            entity = get(builder, AmapCoordinateResultEntity.class);
        } catch (Exception e) {
            log.error("{}, lon:{}, lat: {}, request-url:{}", message, longitude, latitude, url, e);
            throw new BusApiException(message + "，lon：" + longitude + ", lat: " + latitude);
        }
        return entity;
    }

    /**
     * @Description:
     * @Param: type
     * 0：直线距离
     *
     * 1：驾车导航距离（仅支持国内坐标）。
     *
     * 必须指出，当为1时会考虑路况，故在不同时间请求返回结果可能不同。
     *
     * 此策略和驾车路径规划接口的 strategy=4策略基本一致，策略为“ 躲避拥堵的路线，但是可能会存在绕路的情况，耗时可能较长 ”
     *
     * 若需要实现高德地图客户端效果，可以考虑使用驾车路径规划接口
     *
     * 3：步行规划距离（仅支持5km之间的距离）
     * @Return: com.phlink.bus.api.map.response.AmapDistanceResultEntity
     * @Author wen
     * @Date 2019/11/20 15:06
     */
    @Override
    public AmapDistanceResultEntity getDistance(List<String> origins, String destination, String type) throws BusApiException {
        AmapDistanceResultEntity resultEntity = null;
        String message = "坐标转换失败";
        String url = "";
        try {
            String key = getKey();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BusApiMapConstant.Amap.GET_DISTANCE)
                    .queryParam("key", key)
                    .queryParam("origins", String.join(StringPool.PIPE, origins))
                    .queryParam("destination", destination)
                    .queryParam("type", type)
                    .queryParam("output", "JSON");
            url = builder.build().encode().toUriString();
            resultEntity = get(builder, AmapDistanceResultEntity.class);
        } catch (Exception e) {
            log.error("{}, origins:{}, destination: {}, request-url:{}", message, origins, destination, url);
            throw new BusApiException(message + "，origins：" + origins + ", destination: " + destination);
        }
        return resultEntity;
    }

    public AmapResultEntity postFormData(String url, MultiValueMap<String, Object> postParameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(postParameters, headers);

        ResponseEntity<AmapResultEntity> responseEntity1 = this.restTemplate.postForEntity(url, formEntity, AmapResultEntity.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        AmapResultEntity resultEntity = responseEntity1.getBody();
        log.info("postForm statusCode:{} url:{} resultEntity: {}", statusCode, url, resultEntity);
        return resultEntity;
    }

    public AmapResultEntity postJson(String url, String postParameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> formEntity = new HttpEntity<>(postParameters, headers);

        ResponseEntity<AmapResultEntity> responseEntity1 = this.restTemplate.postForEntity(url, formEntity, AmapResultEntity.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        AmapResultEntity resultEntity = responseEntity1.getBody();
        log.info("postJson statusCode:{} url: {} resultEntity: {}", statusCode, url, resultEntity);
        return resultEntity;
    }

    public AmapResultEntity get(String url) {
        return get(url, AmapResultEntity.class);
    }

    private <T> T get(String url, Class<T> cls) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(headers);

        ResponseEntity<T> responseEntity1 = this.restTemplate.getForEntity(url, cls, formEntity);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        T resultEntity = responseEntity1.getBody();
        log.info("get statusCode:{} url: {} resultEntity: {}", statusCode, url, resultEntity);
        return resultEntity;
    }

    public <T> T get(UriComponentsBuilder builder, Class<T> cls) {
        URI uri = builder.build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<T> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                cls);
        log.info("get url: {} response: {}", uri.toString(), response);
        return response.getBody();
    }

    private String createRequestUrl() throws BusApiException {
        try {
            String amapKey = getKey();
            String amapServiceKey = getSid();
            return "&key=" + amapKey +
                    "&sid=" + amapServiceKey;
        } catch (Exception e) {
            throw new BusApiException("获取Redis缓存失败");
        }

    }

    private String getSid() throws RedisConnectException {
        return this.redisService.hget(
                BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.AMAP_SERVICE_KEY_CONFIG);
    }

    private String getKey() throws RedisConnectException {
        return this.redisService.hget(
                BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.AMAP_KEY_CONFIG);
    }

    private String createFenceUrl() throws BusApiException {
        try {
            String amapKey = getKey();
            return BusApiMapConstant.Amap.FENCE_URL + amapKey;
        } catch (Exception e) {
            throw new BusApiException("获取Redis缓存失败");
        }
    }

    private void checkError(JSONObject jo, String message) throws BusApiException {
        JSONObject dataObject = jo.getJSONObject("data");
        if (jo.getInteger("errcode") != 10000) {
            String eerDetail = !"OK".equals(jo.getString("errmsg")) ? jo.getString("errdetail") : "";
            if (dataObject == null) {
                throw new BusApiException(eerDetail);
            }
            String resultMessage = !"0".equals(jo.getString("status")) ? jo.getJSONObject("data").getString("message") : "";
            throw new BusApiException(message + ",错误码:" + jo.getString("errmsg")
                    + ",错误码描述:" + jo.getString("errmsg") + eerDetail
                    + ",错误详情:" + resultMessage);
        }
    }

    private void checkFenceError(JSONObject jo, String message) throws BusApiException {
        if (jo.getInteger("errcode") != 0 || jo.getJSONObject("data").getInteger("status") != 0) {
            String eerDetail = !"OK".equals(jo.getString("errmsg")) ? jo.getString("errdetail") : "";
            JSONObject dataObject = jo.getJSONObject("data");
            if (dataObject == null) {
                throw new BusApiException(eerDetail);
            }
            String resultMessage = !"0".equals(jo.getString("status")) ? jo.getJSONObject("data").getString("message") : "";
            message = message + ",错误码:" + jo.getString("errmsg")
                    + ",错误码描述:" + jo.getString("errmsg") + eerDetail
                    + ",错误详情:" + resultMessage;
            throw new BusApiException(message);
        }
    }

    private void checkTrajectoryError(JSONObject jo, String message) throws BusApiException {
        if (jo.getInteger("errcode") != 10000) {
            String eerDetail = !"OK".equals(jo.getString("errmsg")) ? jo.getString("errdetail") : "";
            throw new BusApiException(message + ",错误码:" + jo.getString("errmsg")
                    + ",错误码描述:" + jo.getString("errmsg") + eerDetail);
        }
    }

}
