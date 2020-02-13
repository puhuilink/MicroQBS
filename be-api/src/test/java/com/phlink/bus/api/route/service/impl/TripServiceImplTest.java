package com.phlink.bus.api.route.service.impl;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.TripLog;
import com.phlink.bus.api.route.domain.TripState;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.api.serviceorg.domain.StudentGuardianInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class TripServiceImplTest {
    private String busCode = "LZYTETC92H1002692";
    private Long tripId = 1206480478719279105L;
    private Long teacherId = 1206478453759639553L; // 15910962128
    private Long studentId = 1206488464019783681L; //王亚4

    @Autowired
    private ITripService tripService;

    @Autowired
    private IBusService busService;

    @Test
    public void testGetBusStopTime() {
        // 赵全4线01
        Long studentId = 1177400229041823746L;
//        TripStopTimeDetailVO vo = tripService.getBusStopTime(studentId);
//        log.info("TripStopTimeDetailVO =====> {}", JSON.toJSONString(vo));
    }

    @Test
    public void testListTripToday() throws BusApiException {
        Bus bus = busService.getByBusCode(busCode);
        List<TripState> states = tripService.listTripToday(bus);
        log.info("List<TripState> ====> {}", JSON.toJSONString(states));
    }

    @Test
    public void testStart() throws BusApiException {
        Bus bus = busService.getByBusCode(busCode);
        TripLog tripLog = tripService.start(bus, tripId);
        log.info("TripState ====> {}", JSON.toJSONString(tripLog));
    }

    @Test
    public void testUp() throws BusApiException {
        Long tripId_1 = 1204308428298678274L;
        Long studentId_1 = 1210760963439988737L;
        Long teacherId_1 = 1204321836876197890L;
        String busCode = "FT9310";
        TripState tripState = tripService.getTripStateByIdAndBusCode(tripId_1, busCode);
        tripService.attendanceUp(tripState, studentId_1, teacherId_1,0L);
    }

    @Test
    public void testDown() throws BusApiException {
        Long tripId_1 = 1204308428298678274L;
        Long studentId_1 = 1210760963439988737L;
        Long teacherId_1 = 1204321836876197890L;
        String busCode = "FT9310";
        TripState tripState = tripService.getTripStateByIdAndBusCode(tripId_1, busCode);
        tripService.attendanceDown(tripState, studentId_1, teacherId_1);

    }

    @Test
    public void testStop() throws BusApiException {
        Bus bus = busService.getByBusCode(busCode);
        TripLog tripLog = tripService.stop(bus, tripId);
        log.info("TripState ====> {}", JSON.toJSONString(tripLog));
    }

    @Test
    public void testGetAllStudentList() throws BusApiException {
        RList<StudentGuardianInfo> res = tripService.getAllStudentList("LHM12PDIH", 1176753710622093314L);
        log.info("StudentGuardianInfo ====> {}", JSON.toJSONString(res));
    }

    @Test
    public void testGetCurrentRunningTrip() throws BusApiException {
        TripState trip = tripService.getCurrentRunningTrip("LZYTETC92H1002692");
        log.info("trip ====> {}", JSON.toJSONString(trip));
    }

    @Test
    public void testGetTripState() throws BusApiException {
        Bus bus = busService.getByBusCode("ADY123");
        TripState trip = tripService.getTripState(bus, 1176750163109130242L);
        log.info("trip ====> {}", JSON.toJSONString(trip));
    }

    @Test
    public void testLocation() {
        DvrLocation location = tripService.location("201910180001");
        log.info("location ====> {}", JSON.toJSONString(location));
    }

    @Test
    public void testGetLeavenum() {
        TripState trip = tripService.getTripStateByIdAndBusCode(1204308428340621314L, "FT9310");
        List<Long> allStudentIds = tripService.listAllStudentId(trip);
        List<Long> leaveStudentIds = tripService.listLeaveStudentId(trip, allStudentIds);
        log.info("leaveStudentIds ====> {}", JSON.toJSONString(leaveStudentIds));
    }

    @Test
    public void testListAllStudentId() {
        TripState trip = tripService.getTripStateByIdAndBusCode(1204308428340621314L, "FT9310");
        List<Long> allStudentIds = tripService.listAllStudentId(trip);
        log.info("allStudentIds ====> {}", JSON.toJSONString(allStudentIds));
    }
}