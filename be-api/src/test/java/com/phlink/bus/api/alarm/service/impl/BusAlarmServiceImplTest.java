package com.phlink.bus.api.alarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.service.IAlarmRouteRulesService;
import com.phlink.bus.api.alarm.service.IBusAlarmService;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.bus.service.IDvrLocationService;
import com.phlink.bus.api.route.manager.TripTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class BusAlarmServiceImplTest {
    private String busCode = "FT9310";
    @Autowired
    private IBusAlarmService busAlarmService;
    @Autowired
    private IDvrLocationService dvrLocationService;
    @Autowired
    private IAlarmRouteRulesService alarmRouteRulesService;
    @Autowired
    private IBusService busService;
    @Autowired
    private TripTask tripTask;

    @Test
    public void testAlarmBusMapping() {
        QueryWrapper<DvrLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DvrLocation::getBusCode, busCode);
        List<DvrLocation> locations = dvrLocationService.list(queryWrapper);
        if(locations != null && !locations.isEmpty()) {
            DvrLocation location = locations.get(locations.size() - 1);
            location.setGpstime(System.currentTimeMillis());
            location.setBlon(BigDecimal.valueOf(116.670744));
            location.setBlat(BigDecimal.valueOf(40.228864));
            location.setSpeed(BigDecimal.valueOf(100));
//            location.setGlon(BigDecimal.valueOf(116.664311));
//            location.setGlat(BigDecimal.valueOf(40.223248));
            // 马家堡
            location.setGlon(BigDecimal.valueOf(116.614933));
            location.setGlat(BigDecimal.valueOf(40.192912));

//            BindBusDetailInfo busDetailInfo = busService.getBindBusDetailInfoByBusCode(busCode);
            busAlarmService.alarmBusMapping(location);
        }
        try {
            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuildTripStopTimeDetail() {
        tripTask.initTripStopTimeDetail();
    }

    @Test
    public void testAsyncBuildStopAlarm() {
        AlarmRouteRules rules = alarmRouteRulesService.getByBusCode(busCode);

        QueryWrapper<DvrLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DvrLocation::getBusCode, busCode);
        queryWrapper.lambda().gt(DvrLocation::getCreateTime, LocalDateTime.of(2020,1,8,7,40));
        queryWrapper.lambda().lt(DvrLocation::getCreateTime, LocalDateTime.of(2020,1,8,7,50));
        queryWrapper.lambda().orderByAsc(DvrLocation::getCreateTime);
        List<DvrLocation> locations = dvrLocationService.list(queryWrapper);
        if(locations != null && !locations.isEmpty()) {
            for(DvrLocation location: locations) {
//                location.setGpstime(System.currentTimeMillis());
//            location.setBlon(BigDecimal.valueOf(116.586192));
//            location.setBlat(BigDecimal.valueOf(40.204865));
//            location.setSpeed(BigDecimal.valueOf(100));
//            location.setGlon(BigDecimal.valueOf(116.664311));
//            location.setGlat(BigDecimal.valueOf(40.223248));
                // 116.713821,40.178467&type=1&output=JSON
//            location.setGlon(BigDecimal.valueOf(116.713821));
//            location.setGlat(BigDecimal.valueOf(40.178467));

//            BindBusDetailInfo busDetailInfo = busService.getBindBusDetailInfoByBusCode(busCode);
                busAlarmService.asyncBuildStopAlarm(rules, location);
            }
        }
    }

    @Test
    public void testAsyncBuildBusAlarm() {
        QueryWrapper<DvrLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DvrLocation::getBusCode, busCode);
        queryWrapper.lambda().gt(DvrLocation::getCreateTime, LocalDateTime.of(2020,1,8,7,40));
        queryWrapper.lambda().lt(DvrLocation::getCreateTime, LocalDateTime.of(2020,1,8,7,50));
        queryWrapper.lambda().orderByAsc(DvrLocation::getCreateTime);
        List<DvrLocation> locations = dvrLocationService.list(queryWrapper);
        if(locations != null && !locations.isEmpty()) {
            for(DvrLocation location: locations) {
//                location.setGpstime(System.currentTimeMillis());
//            location.setBlon(BigDecimal.valueOf(116.586192));
//            location.setBlat(BigDecimal.valueOf(40.204865));
            location.setSpeed(BigDecimal.valueOf(100));
//            location.setGlon(BigDecimal.valueOf(116.664311));
//            location.setGlat(BigDecimal.valueOf(40.223248));
                // 116.713821,40.178467&type=1&output=JSON
//            location.setGlon(BigDecimal.valueOf(116.713821));
//            location.setGlat(BigDecimal.valueOf(40.178467));

//            BindBusDetailInfo busDetailInfo = busService.getBindBusDetailInfoByBusCode(busCode);
                busAlarmService.asyncBuildBusAlarm(location);
            }
        }
    }

    @Test
    public void testInvalidDateCheck() {
        Bus bus = busService.getByBusCode(busCode);
        AlarmRouteRules rules = alarmRouteRulesService.getByBusCode(busCode);
        boolean isInvalid = busAlarmService.invalidDateCheck(rules, busCode, bus.getId());
        log.info("busCode {} invalidDateCheck  is  {}", busCode, isInvalid);
    }

}