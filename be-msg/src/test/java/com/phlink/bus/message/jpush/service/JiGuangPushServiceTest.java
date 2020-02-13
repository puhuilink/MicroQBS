package com.phlink.bus.message.jpush.service;

import com.phlink.bus.core.model.JiGuangPushBean;
import com.phlink.bus.core.model.PushBean;
import com.phlink.bus.message.MessageApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MessageApplication.class)
public class JiGuangPushServiceTest {

    @Autowired
    private JiGuangPushService jiGuangPushService;

    @Test
    public void testPush(){
        String rid = "1104a89792c7ad1e652";

        List<String> registerIds = Collections.singletonList(rid);
        JiGuangPushBean jiGuangPushBean = new JiGuangPushBean();
        PushBean pushBean = new PushBean();
        jiGuangPushBean.setPushBean(pushBean);
        jiGuangPushBean.setRegistrationIds(registerIds);
        pushBean.setTitle("车辆站点迟到");
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = df2.format(date);

        pushBean.setAlert(String.format("中益安达校车%s，司机：%s 在%s发生站点迟到事件，请悉知。", "test", "test", dateText));

        jiGuangPushService.push(pushBean, registerIds.toArray(new String[0]));

    }

}