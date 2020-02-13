package com.phlink.bus.api.device.manager;

import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.properties.BusApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class IotManager {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BusApiProperties busApiProperties;
    @Autowired
    private RedissonClient redissonClient;

    private static final String LOGIN_URL = "/auth/login";
    private static final String COMMAND_URL = "/plugins/rpc/oneway";
    private static final String HEADER_KEY = "X-Authorization";
    private static final String HEADER_VALUE = "Bearer ";

    @Async
    @Scheduled(cron = "${bus-api.iot.cron}")
    public void loginToIotServer() {
        String url = busApiProperties.getIot().getUrl();
        String loginUrl = url + LOGIN_URL;

        HttpHeaders headers = new HttpHeaders();
        Map<String, String> params = new HashedMap();
        params.put("username", busApiProperties.getIot().getUsername());
        params.put("password", busApiProperties.getIot().getPassword());
        HttpEntity<Map<String, String>> formEntity = new HttpEntity<>(params, headers);

        ResponseEntity<IotLoginResultEntity> responseEntity1 = this.restTemplate.postForEntity(loginUrl, formEntity, IotLoginResultEntity.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        IotLoginResultEntity entityBody = responseEntity1.getBody();
        log.info("post testEntity2: {}", entityBody);
        log.info("post statusCode: {}", statusCode);
        if(entityBody == null || StringUtils.isBlank(entityBody.getToken())) {
            return;
        }
        redissonClient.getBucket(BusApiConstant.IOT_TOKEN, new StringCodec()).set(entityBody.getToken());
        log.info("iot系统登录成功 {}", entityBody.getToken());
    }

    public Boolean call(String deviceId, String toMobile) {
        RBucket<String> rBucket = redissonClient.getBucket(BusApiConstant.IOT_TOKEN, new StringCodec());
        if(rBucket == null) {
            log.error("IOT还未登录，token为空");
            return false;
        }
        HttpStatus statusCode = postToIot(deviceId, toMobile, rBucket.get(), "MONITOR");
        log.info("post statusCode: {}", statusCode);
        return statusCode.is2xxSuccessful();
    }

    public Boolean changeUploadFrequency(String deviceId, Integer frequency) {

        RBucket<String> rBucket = redissonClient.getBucket(BusApiConstant.IOT_TOKEN, new StringCodec());
        if(rBucket == null) {
            log.error("IOT还未登录，token为空");
            return false;
        }
        HttpStatus statusCode = postToIot(deviceId, String.valueOf(frequency), rBucket.get(), "UPLOAD");
        log.info("post statusCode: {}", statusCode);
        return statusCode.is2xxSuccessful();
    }

    public Boolean cr(String deviceId) {
        RBucket<String> rBucket = redissonClient.getBucket(BusApiConstant.IOT_TOKEN, new StringCodec());
        if(rBucket == null) {
            log.error("IOT还未登录，token为空");
            return false;
        }
        HttpStatus statusCode = postToIot(deviceId, "", rBucket.get(), "CR");
        log.info("post statusCode: {}", statusCode);
        return statusCode.is2xxSuccessful();

    }

    private HttpStatus postToIot(String deviceId, String args, String token, String cmd) {
        String url = busApiProperties.getIot().getUrl();
        String commandUrl = url + COMMAND_URL + "/" + busApiProperties.getIot().getGateway();

        Map<String, Object> params = new HashedMap();
        params.put("method", cmd);
        Map<String, Object> subParams = new HashedMap();
        subParams.put("vendor", "3G");
        subParams.put("deviceId", deviceId);
        subParams.put("args", args);

        params.put("params", subParams);
        params.put("timeout", 500);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_KEY, HEADER_VALUE + token);
        HttpEntity<Map<String, Object>> formEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(commandUrl, formEntity, String.class);
        return responseEntity.getStatusCode();
    }
}
