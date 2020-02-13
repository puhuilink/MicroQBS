package com.phlink.bus.api.map.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phlink.bus.api.alarm.domain.AlarmConfig;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.Point;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.common.utils.CoordinateTransformUtils;
import com.phlink.bus.api.common.utils.HttpUtil;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.map.domain.BusApiMapConstant;
import com.phlink.bus.api.map.response.BaiduFenceCreateResultEntity;
import com.phlink.bus.api.map.response.BaiduFenceListResultEntity;
import com.phlink.bus.api.map.response.BaiduLocationStatusResultEntity;
import com.phlink.bus.api.map.service.IMapBaiduService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MapBaiduServiceImpl implements IMapBaiduService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AlarmConfig alarmConfig;

    @Override
    public boolean createBaiduEntity(String numberPlate, String busCode) {
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        try {
            String baiduKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_KEY_CONFIG);
            String baiduServiceKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_SERVICE_KEY_CONFIG);

            postParameters.add("service_id", baiduServiceKey);
            postParameters.add("ak", baiduKey);
            postParameters.add("entity_name", busCode);
            postParameters.add("entity_desc", numberPlate);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(postParameters, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(BusApiMapConstant.Baidu.CREATE_TERMINAL, request, String.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            String entity = responseEntity.getBody();
            log.info("post params: {}", postParameters);
            log.info("post responseEntity: {}", entity);
            log.info("post statusCode: {}", statusCode);
            if (entity == null) {
                log.error("创建百度终端失败，entity == null");
                return false;
            }
            BaiduFenceCreateResultEntity entity1 = JSON.parseObject(entity, BaiduFenceCreateResultEntity.class);
            if (entity1.getStatus() != 0) {
                log.error("创建百度终端失败，{}", entity1.getMessage());
                return false;
            }
            return true;

        } catch (RedisConnectException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void deleteBaiduEntitys(String entityName) {
        String result;
        try {
            StringBuffer buffer = new StringBuffer();
            // 百度
            buffer.append("entity_name=").append(entityName);
            String url = createRequestUrl();
            buffer.append(url);
            result = HttpUtil.sendPost(BusApiMapConstant.Baidu.DELETE_TERMINAL, buffer.toString());
            // 解析
            JSONObject jo = JSON.parseObject(result);
            if (jo.getInteger("status") == 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            log.info("删除百度终端失败，终端entity_name:" + entityName);
            e.printStackTrace();
        }
    }

    @Override
    public int createpolylinefence(FenceVO fenceVO) throws BusApiException {
        // 经纬度顺序为：纬度,经度；
        String vertexes = Arrays.stream(fenceVO.getVertexes()).map(i -> {
            // 转为百度系坐标
            Point wgs84ToBd09 = CoordinateTransformUtils.gcj02ToBd09(i[0], i[1]);
            return wgs84ToBd09.getLat()+","+wgs84ToBd09.getLng();
        }).collect(Collectors.joining(";"));


        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

        try {
            String baiduKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_KEY_CONFIG);
            String baiduServiceKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_SERVICE_KEY_CONFIG);

            postParameters.add("service_id", baiduServiceKey);
            postParameters.add("ak", baiduKey);
            postParameters.add("fence_name", fenceVO.getFenceName());
            postParameters.add("monitored_person", "#allentity");
            postParameters.add("vertexes", vertexes);
            postParameters.add("offset", String.valueOf(alarmConfig.getRouteOffsetDistance()));
            postParameters.add("coord_type", BusApiMapConstant.Baidu.COORD_TYPE);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(postParameters, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(BusApiMapConstant.Baidu.CREATE_FENCE_LINE, request, String.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            String entity = responseEntity.getBody();
            log.info("baidu createpolylinefence post params: {}", postParameters);
            log.info("baidu createpolylinefence post responseEntity: {}", entity);
            log.info("baidu createpolylinefence post statusCode: {}", statusCode);
            if (entity == null) {
                throw new BusApiException("创建百度电子围栏失败");
            }
            BaiduFenceCreateResultEntity entity1 = JSON.parseObject(entity, BaiduFenceCreateResultEntity.class);
            if (entity1.getStatus() != 0) {
                throw new BusApiException("创建百度电子围栏失败，" + entity1.getMessage());
            }
            return entity1.getFence_id();

        } catch (RedisConnectException e) {
            e.printStackTrace();
            throw new BusApiException("创建百度电子围栏失败");
        }
    }

    @Override
    public void updatepolylinefence(FenceVO fenceVO) throws BusApiException {
        String result;
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("fence_name=").append(fenceVO.getFenceName());
//            buffer.append("&monitored_person=").append(StringUtils.join(fenceVO.getEntityNames(), ","));
            String vertexes = Arrays.stream(fenceVO.getVertexes()).map(i -> Arrays.stream(i).map(Object::toString).collect(Collectors.joining(","))).collect(Collectors.joining(";"));
            buffer.append("&vertexes=").append(vertexes);
            buffer.append("&fence_id=").append(fenceVO.getFenceId());
            String url = createRequestUrl();
            buffer.append(url);
            result = HttpUtil.sendPost(BusApiMapConstant.Baidu.UPDATE_FENCE_LINE, buffer.toString());
            // 解析
            JSONObject jo = JSON.parseObject(result);
            if (jo.getInteger("status") == 0) {
                throw new BusApiException("更新百度电子围栏失败，" + jo.getString("message"));
            }
        } catch (Exception e) {
            throw new BusApiException("更新百度电子围栏失败，名称fenceName:" + fenceVO.getFenceName());
        }
    }

    @Override
    public void deletepolylinefence(List<Integer> fenceIds) throws BusApiException {
        String errorMsg = "删除百度电子围栏失败";
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        try {
            String baiduKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_KEY_CONFIG);
            String baiduServiceKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_SERVICE_KEY_CONFIG);

            postParameters.add("service_id", baiduServiceKey);
            postParameters.add("ak", baiduKey);
            postParameters.add("fence_ids", JSON.toJSONString(fenceIds));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(postParameters, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(BusApiMapConstant.Baidu.DELETE_FENCE_LINE, request, String.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            String entity = responseEntity.getBody();
            log.info("baidu deletepolylinefence post params: {}", postParameters);
            log.info("baidu deletepolylinefence post responseEntity: {}", entity);
            log.info("baidu deletepolylinefence post statusCode: {}", statusCode);
            if (entity == null) {
                throw new BusApiException(errorMsg);
            }
            BaiduFenceCreateResultEntity entity1 = JSON.parseObject(entity, BaiduFenceCreateResultEntity.class);
            if (entity1.getStatus() != 0) {
                throw new BusApiException(errorMsg + ", " + entity1.getMessage());
            }

        } catch (RedisConnectException e) {
            e.printStackTrace();
            throw new BusApiException(errorMsg);
        }
    }

    @Override
    public BaiduLocationStatusResultEntity queryLocationStatus(String entityName, List<String> fenceIds, String lon, String lat) {
        try {
            String baiduKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_KEY_CONFIG);
            String baiduServiceKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_SERVICE_KEY_CONFIG);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BusApiMapConstant.Baidu.QUERY_STATUS_BY_LOCATION)
                    .queryParam("ak", baiduKey)
                    .queryParam("service_id", baiduServiceKey)
                    .queryParam("monitored_person", entityName)
                    .queryParam("fence_ids", String.join(",", fenceIds))
                    .queryParam("longitude", lon)
                    .queryParam("latitude", lat)
                    .queryParam("coord_type", BusApiMapConstant.Baidu.COORD_TYPE);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            HttpEntity<String> response = restTemplate.exchange(
                    builder.build().encode().toUri(),
                    HttpMethod.GET,
                    entity,
                    String.class);
            log.info("baidu queryLocationStatus GET {}, result {}", builder.build().encode().toUri(), response.getBody());
            String result = response.getBody();
            return JSON.parseObject(result, BaiduLocationStatusResultEntity.class);
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BaiduFenceListResultEntity getFence(String fenceId) {
        try {
            String baiduKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_KEY_CONFIG);
            String baiduServiceKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_SERVICE_KEY_CONFIG);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BusApiMapConstant.Baidu.LIST_FENCE)
                    .queryParam("ak", baiduKey)
                    .queryParam("service_id", baiduServiceKey)
                    .queryParam("fence_ids", fenceId)
                    .queryParam("coord_type_output", "bd09ll");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            HttpEntity<String> response = restTemplate.exchange(
                    builder.build().encode().toUri(),
                    HttpMethod.GET,
                    entity,
                    String.class);
            log.info("GET {}, result {}", builder.build().encode().toUri(), response.getBody());
            String result = response.getBody();
            return JSON.parseObject(result, BaiduFenceListResultEntity.class);
        } catch (RedisConnectException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String createRequestUrl() throws RedisConnectException {
        String baiduKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_KEY_CONFIG);
        String baiduServiceKey = this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, BusApiConstant.BAIDU_SERVICE_KEY_CONFIG);
        // String sn=this.redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX,BusApiConstant.BAIDU_SN_CONFIG);
        // todo:暂时不使用sn
        // buffer.append("&sn="+sn);
        return "&service_id=" + baiduServiceKey +
                "&ak=" + baiduKey;
    }
}
