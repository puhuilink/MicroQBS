package com.phlink.bus.api.map.service;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.map.response.BaiduLocationStatusResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
class MapBaiduServiceTest {

    @Autowired
    private IMapBaiduService mapBaiduService;

    @Test
    public void testQueryLocationStatus() {
        String entityName = "20190928-01";
        String fenceId = "19";
        String lon = "116.536297";
        String lat = "40.11653";
        BaiduLocationStatusResultEntity entity = mapBaiduService.queryLocationStatus(entityName, Collections.singletonList(fenceId), lon, lat);
        log.info("entity--------------> {}", JSON.toJSONString(entity));
    }

    @Test
    public void testCreateBaiduEntity() {
        String numberPlate = "aaaaa";
        String busCode = "vvvvv";
        mapBaiduService.createBaiduEntity(numberPlate, busCode);

    }

}