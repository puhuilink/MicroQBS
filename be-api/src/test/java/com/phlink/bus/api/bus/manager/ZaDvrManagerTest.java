package com.phlink.bus.api.bus.manager;

import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IDvrLocationService;
import com.phlink.bus.api.bus.service.IDvrServerService;
import com.phlink.bus.api.common.exception.BusApiException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
class ZaDvrManagerTest {

    @Autowired
    private ZaDvrManager dvrManager;
    @Autowired
    private IDvrServerService dvrServerService;
    @Autowired
    private IDvrLocationService dvrLocationService;

    @Test
    public void testLoginToDvrServer() {
        dvrManager.loginToDvrServer();
    }

    @Test
    public void testGetDvrStatus() throws InterruptedException {
        dvrManager.fetchDvrStatus();
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void testGetDvrRtmpInfo() throws InterruptedException, BusApiException {
        Integer channel = 0;
        Integer dataType = 1;
        String dvrno = "18925280088";
        Integer streamType = 1;

//        dvrManager.getDvrRtmpInfo(channel, dataType, dvrno, streamType, );
    }

    @Test
    public void testCreateDvrLocation() throws InterruptedException, BusApiException {
        Long busId = 1177522517410062338L;
        Long locationId = 1181847767551610882L;
        DvrLocation location = dvrLocationService.findById(locationId);
        /*
        [
          [116.368904, 39.913423],
          [116.382122, 39.901176],
          [116.387271, 39.912501],
          [116.398258, 39.904600],
          [116.418251, 39.914609]
        ]
         */
        Double[] p1 = new Double[]{116.368904, 39.913423};
        Double[] p2 = new Double[]{116.382122, 39.901176};
        Double[] p3 = new Double[]{116.387271, 39.912501};
        Double[] p4 = new Double[]{116.398258, 39.904600};
        Double[] p5 = new Double[]{116.418251, 39.914609};
        List<Double[]> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        BigDecimal totalCost = BigDecimal.valueOf(1000000);
        for(int i=0;i<5;i++) {
            location.setCreateTime(LocalDateTime.now());
            location.setGpstime(System.currentTimeMillis());
            location.setGlon(BigDecimal.valueOf(points.get(i)[0]));
            location.setGlat(BigDecimal.valueOf(points.get(i)[1]));
            location.setBlon(BigDecimal.valueOf(points.get(i)[0]));
            location.setBlat(BigDecimal.valueOf(points.get(i)[1]));
            location.setLon(BigDecimal.valueOf(points.get(i)[0]).multiply(totalCost).longValue());
            location.setLat(BigDecimal.valueOf(points.get(i)[1]).multiply(totalCost).longValue());
//            dvrManager.createDvrLocation(busId, location);
            TimeUnit.SECONDS.sleep(10);
        }
    }
}