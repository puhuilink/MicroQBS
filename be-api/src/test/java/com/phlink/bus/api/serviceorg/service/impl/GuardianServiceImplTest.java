package com.phlink.bus.api.serviceorg.service.impl;

import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
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
public class GuardianServiceImplTest {
    @Autowired
    private IGuardianService guardianService;

    @Test
    public void testRemoveGuardianIds() {
        Long studentId = 1169127611821268993L;
        String[] guardianIds = new String[]{"1169127604846141441"};
        guardianService.removeGuardianIds(studentId, guardianIds);
    }

    @Test
    public void testAddOtherGuardian() throws Exception {
        Long studentId = 1188644995654086657L;
        String guardianName = "张飞柳树";
        String mobile = "13911110129";
        String idcard = "371522199909095912";
        guardianService.addOtherGuardian(studentId, guardianName, mobile, idcard);
    }

}