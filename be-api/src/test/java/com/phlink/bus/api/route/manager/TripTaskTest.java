package com.phlink.bus.api.route.manager;

import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.common.exception.BusApiException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class TripTaskTest {

    @Autowired
    private TripTask tripTask;

    @Test
    public void testInitBusTripList() {
        tripTask.initBusTripList();
    }

    @Test
    public void testCheckWaitTripTimeStatus() {
        tripTask.checkWaitTripTimeStatus();
    }
    @Test
    public void testCheckWaitTripTimeStatusByBusCode() throws BusApiException {
        tripTask.checkWaitTripByBusCode("FT9310");
    }

    @Test
    public void testCheckRunningTripTimeStatus() {
        tripTask.checkRunningTripTimeStatus();
    }

    @Test
    public void testCheckRunningTripByBusCode() throws BusApiException {
        tripTask.checkRunningTripByBusCode("FT9310");
    }

}