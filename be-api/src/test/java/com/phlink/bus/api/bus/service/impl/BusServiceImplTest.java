package com.phlink.bus.api.bus.service.impl;

import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class BusServiceImplTest {
    @Autowired
    private IBusService busService;

    @Test
    public void testInsert() {
        Bus bus = new Bus();
        bus.setBrand("宇通");
        bus.setModel("啥型号");
        bus.setNumberPlate("京-adada");
        bus.setChassis("不知道啥底盘");
        bus.setDeptId(1L);
        bus.setEmission("国五");
        bus.setEngineModel("宝马发动机");
        bus.setFuelTank("100L");
        bus.setHeight("1000");
        bus.setWidth("1000");
        bus.setLength("2000");
        bus.setCreateTime(LocalDateTime.now());
        bus.setDeleted(false);
        busService.save(bus);
        Assert.assertNotNull(bus.getId());
    }

    @Test
    public void testRemove() {
        Bus bus = new Bus();
        bus.setBrand("宇通");
        bus.setModel("啥型号");
        bus.setNumberPlate("京-adada");
        bus.setChassis("不知道啥底盘");
        bus.setDeptId(1L);
        bus.setEmission("国五");
        bus.setEngineModel("宝马发动机");
        bus.setFuelTank("100L");
        bus.setHeight("1000");
        bus.setWidth("1000");
        bus.setLength("2000");
        bus.setCreateTime(LocalDateTime.now());
        bus.setDeleted(false);
        busService.save(bus);
        busService.removeById(bus.getId());
        Bus bus1 = busService.getById(bus.getId());
        Assert.assertNull(bus1);
    }

    @Test
    public void testRegister(){
        List<Bus> buses = busService.list();
        busService.batchRegisterToBaidu(buses);
    }
}
