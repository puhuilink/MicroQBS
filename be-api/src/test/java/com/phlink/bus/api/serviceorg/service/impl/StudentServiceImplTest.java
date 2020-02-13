package com.phlink.bus.api.serviceorg.service.impl;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.serviceorg.domain.StudentGuardianInfo;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class StudentServiceImplTest {
    @Autowired
    private IStudentService studentService;

    @Test
    public void testUpdateAllServiceStatus() {
        studentService.updateAllServiceStatus();
    }

    @Test
    public void testListStudentGuardianInfoByStop() {
        Long stopId = 1176753709481242628L;
        Long routeOperationId = 1186229459166982146L;
        List<StudentGuardianInfo> guardianInfos = studentService.listStudentGuardianInfoByStop(stopId, routeOperationId);
        log.info(JSON.toJSONString(guardianInfos));
    }
}