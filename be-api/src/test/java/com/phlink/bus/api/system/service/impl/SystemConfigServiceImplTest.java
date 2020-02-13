package com.phlink.bus.api.system.service.impl;

import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.system.domain.SystemConfig;
import com.phlink.bus.api.system.service.ISystemConfigService;
import com.phlink.bus.common.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class SystemConfigServiceImplTest {

    @Autowired
    private ISystemConfigService systemConfigService;

    @Test
    public void testInsert() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setKey(Constants.CAPTCHA_EXPIRE_CONFIG);
        systemConfig.setValue("300");
        systemConfig.setDescription("验证码有效期时间(秒)");
        systemConfig.setCreateTime(LocalDateTime.now());
        systemConfigService.save(systemConfig);
    }

    @Test
    public void testInsertAmapKey() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setKey(BusApiConstant.AMAP_KEY_CONFIG);
        systemConfig.setValue("af4fa1959377c091af53b09ae5cafc39");
        systemConfig.setDescription("高德地图key");
        systemConfig.setCreateTime(LocalDateTime.now());
        systemConfigService.save(systemConfig);
    }

    @Test
    public void testInsertAmapServiceKey() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setKey(BusApiConstant.AMAP_SERVICE_KEY_CONFIG);
        systemConfig.setValue("6b81ee5516ef569d00dd0d0d9164529d");
        systemConfig.setDescription("高德地图Service key");
        systemConfig.setCreateTime(LocalDateTime.now());
        systemConfigService.save(systemConfig);
    }

    @Test
    public void testInsertBaiduKey() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setKey(BusApiConstant.BAIDU_KEY_CONFIG);
        systemConfig.setValue("jjwHu037PWl0X8KGDxrjBfLDhMHOsPMy");
        systemConfig.setDescription("百度地图key");
        systemConfig.setCreateTime(LocalDateTime.now());
        systemConfigService.save(systemConfig);
    }

    @Test
    public void testInsertBaiduServiceKey() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setKey(BusApiConstant.BAIDU_SERVICE_KEY_CONFIG);
        systemConfig.setValue("");
        systemConfig.setDescription("百度地图Service key");
        systemConfig.setCreateTime(LocalDateTime.now());
        systemConfigService.save(systemConfig);
    }

    @Test
    public void testInsertBaiduSnk() {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setKey(BusApiConstant.BAIDU_SN_CONFIG);
        systemConfig.setValue("1de4ca487651ccb58f4a18e6d0d94f2e");
        systemConfig.setDescription("百度地图SN");
        systemConfig.setCreateTime(LocalDateTime.now());
        systemConfigService.save(systemConfig);
    }
    @Test
    public void testInsertHomeworkPath() {
    	SystemConfig systemConfig = new SystemConfig();
    	systemConfig.setKey(BusApiConstant.HOMEWORK_PATH);
    	systemConfig.setValue("F:/aa");
    	systemConfig.setDescription("作业上传路径");
    	systemConfig.setCreateTime(LocalDateTime.now());
    	systemConfigService.save(systemConfig);
    }
    
    @Test
    public void testInsertHomeworkUrl() {
    	SystemConfig systemConfig = new SystemConfig();
    	systemConfig.setKey(BusApiConstant.HOMEWORK_FILE_URL);
    	systemConfig.setValue(" ");
    	systemConfig.setDescription("作业下载url");
    	systemConfig.setCreateTime(LocalDateTime.now());
    	systemConfigService.save(systemConfig);
    }

}