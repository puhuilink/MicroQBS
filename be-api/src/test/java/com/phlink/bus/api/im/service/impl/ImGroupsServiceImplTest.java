package com.phlink.bus.api.im.service.impl;

import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.domain.enums.GroupTypeEnum;
import com.phlink.bus.api.im.service.IImGroupsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class ImGroupsServiceImplTest {

    @Autowired
    private IImGroupsService imGroupsService;

    @Test
    public void testCreateImGroups() throws RedisConnectException {
        ImGroups imGroups = new ImGroups();
        imGroups.setDepartId("1156517271791857665");
        imGroups.setName("测试新建群");
        imGroups.setManagerId(1182557475384139777L);
        imGroups.setManagerName("wendriver1");
        imGroups.setType(GroupTypeEnum.CUSTOMIZE);
        imGroupsService.createImGroups(imGroups);
    }

    @Test
    public void testModifyImGroups() throws RedisConnectException, BusApiException {
//        ImGroups imGroups = new ImGroups();
//        imGroups.setDepartId("1156517271791857665");
//        imGroups.setName("测试新建群");
//        imGroups.setManagerId(1182557475384139777L);
//        imGroups.setManagerName("wendriver1");
//        imGroups.setType(GroupTypeEnum.CUSTOMIZE);
//        imGroupsService.createImGroups(imGroups);

        ImGroups imGroups = new ImGroups();
        imGroups.setId(1213030120935501826L);
        imGroups.setGroupId("1213030120935501826");
        imGroups.setDepartId("1156517271791857665");
        imGroups.setName("测试新建群");
        imGroups.setManagerId(1182557475384139777L);
        imGroups.setManagerName("1司机测试");
        imGroups.setType(GroupTypeEnum.CUSTOMIZE);
        imGroupsService.modifyImGroups(imGroups);
    }
}