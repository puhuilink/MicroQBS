package com.phlink.bus.api.common.service.impl;

import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.core.model.JiGuangPushBean;
import com.phlink.bus.core.model.PushBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class MqServiceImplTest {

    @Autowired
    private MqService mqService;

    @Test
    public void testPushMsg(){
        String rid1 = "190e35f7e020bbb4f31";
        String rid2 = "1104a89792c7ad1e652";

        List<String> registerIds = Arrays.asList(rid1, rid2);
        JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
        PushBean pushBean = new PushBean();
        jiGuangPushBean.setPushBean(pushBean);
        jiGuangPushBean.setRegistrationIds(registerIds);
        pushBean.setTitle("车辆站点迟到");
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = df2.format(date);

        pushBean.setAlert(String.format("中益安达校车%s，司机：%s 在%s发生站点迟到事件，请悉知。", "测试司机", "测试站点", dateText));
        mqService.pushMsg(jiGuangPushBean);
    }

}