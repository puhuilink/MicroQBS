package com.phlink.bus.api.fence.service.impl;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.fence.service.IFenceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class FenceServiceImplTest {
    @Autowired
    private IFenceService fenceService;

    @Test
    public void findById() {
        Long fenceId = 1192352349431324673L;
        try {
            FenceVO vo = fenceService.findById(fenceId);
            log.info("fence point ---------> {}", JSON.toJSONString(vo.getVertexes()));
        } catch (BusApiException e) {
            e.printStackTrace();
        }
    }
}