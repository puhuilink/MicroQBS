package com.phlink.bus.api.map.service;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.fence.domain.FenceTime;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.fence.domain.enums.FenceTypeEnum;
import com.phlink.bus.api.map.response.AmapCoordinateResultEntity;
import com.phlink.bus.api.map.response.AmapDistanceResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class IMapAmapServiceTest {
    @Autowired
    private IMapAmapService mapAmapService;

    @Test
    public void testCreateAmapEntity() {
        String name = "1111111";
        String desc = "京ADY992";
        mapAmapService.createAmapEntity(desc, name);
    }

    @Test
    public void testGetAmapEntity() {
        String name = "测试车辆注册";
        String desc = "adadad";
        mapAmapService.getAmapEntity(name);
    }

    @Test
    public void testRunFence() throws BusApiException {
        String fenceId = "bde0ee02-6fbf-405f-a24f-94babc888adb";
        mapAmapService.runFence(fenceId);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void testGetAlarmFence() throws BusApiException {
        String code = "9612481148";
        Double lon = 116.3395071;
        Double lat = 40.04821565;
        Long time = System.currentTimeMillis()/1000;
        mapAmapService.getAlarmFence(code, lon, lat, time);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void testGetLocationRegeo() {
        BigDecimal lon = BigDecimal.valueOf(116.525566);
        BigDecimal lat = BigDecimal.valueOf(40.097344);
        try {
            String address = mapAmapService.getLocationRegeo(lon, lat);
            log.info("return address {}", address);
        } catch (BusApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertCoordsys() {
        String lon = "116.525566";
        String lat = "40.097344";
        try {
            AmapCoordinateResultEntity points = mapAmapService.convertGpsCoordsys(lon, lat);
            log.info("return points {}", points.getPoints());
        } catch (BusApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDistance() {
        List<String> origins = Arrays.asList("116.481028,39.989643", "114.481028,39.989643", "115.481028,39.989643");
        String destination = "114.465302,40.004717";
        try {
            AmapDistanceResultEntity entity = mapAmapService.getDistance(origins, destination, "0");
            log.info("return result {}", JSON.toJSONString(entity.getResults()));
        } catch (BusApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateFenceVO() {
        FenceVO fenceVO = buildFenceVO();
        try {
            String gid = mapAmapService.createFence(fenceVO);
            Assert.assertNotNull(gid);
            log.info("return result {}", gid);
        } catch (BusApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateFence() {
        FenceVO fenceVO = buildFenceVO();
        fenceVO.setFenceId("396514e7-0cb0-480e-bbec-f69389be8b99");
        fenceVO.setRadius(1030);
        try {
            mapAmapService.updateFence(fenceVO);
        } catch (BusApiException e) {
            e.printStackTrace();
        }
    }

    public FenceVO buildFenceVO() {
        FenceVO fenceVO = new FenceVO();
        fenceVO.setFenceType(FenceTypeEnum.CIRCLE);
        fenceVO.setFenceName(RandomStringUtils.random(5, true, true));
        fenceVO.setCenter("116.524633,40.079509");
        fenceVO.setRadius(1024);
        fenceVO.setRelationId(1182556958239039490L);
        List<FenceTime> timeList = new ArrayList<>();
        FenceTime time1 = new FenceTime();
        time1.setStartTime(LocalTime.now().plusHours(-10));
        time1.setEndTime(LocalTime.now().plusHours(-8));
        timeList.add(time1);
        FenceTime time2 = new FenceTime();
        time2.setStartTime(LocalTime.now().plusHours(-7));
        time2.setEndTime(LocalTime.now().plusHours(-5));
        timeList.add(time2);
        fenceVO.setMonitorTime(timeList);
        return fenceVO;
    }

    @Test
    public void testDeleteFenceVO() {
//        String gid = "4143e18c-fe6e-4050-81e9-4807df1d73d1";
        String gid = "396514e7-0cb0-480e-bbec-f69389be8b99";
        try {
            mapAmapService.deleteFence(gid);
        } catch (BusApiException e) {
            e.printStackTrace();
        }

    }
}