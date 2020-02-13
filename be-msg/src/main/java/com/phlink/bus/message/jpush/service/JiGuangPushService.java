package com.phlink.bus.message.jpush.service;

import com.phlink.bus.core.model.PushBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JiGuangPushService {

    /**
     * 一次推送最大数量 (极光限制1000)
     */
    private static final int max_size = 800;

    @Autowired
    private MyJiGuangPushService jPushService;

    /**
     * 推送全部, 不支持附加信息
     *
     * @return
     */
    public boolean pushAll(PushBean pushBean) {
        return jPushService.push_android_and_ios(pushBean);
    }

    /**
     * 推送指定id
     *
     * @return
     */
    public boolean push(PushBean pushBean, String... registids) {
        registids = checkRegistids(registids); // 剔除无效registed
        while (registids.length > max_size) { // 每次推送max_size个
            jPushService.push_android_and_ios(pushBean, Arrays.copyOfRange(registids, 0, max_size));
            registids = Arrays.copyOfRange(registids, max_size, registids.length);
        }
        return jPushService.push_android_and_ios(pushBean, registids);
    }

    /**
     * 剔除无效registed
     *
     * @param registids
     * @return
     */
    public String[] checkRegistids(String[] registids) {
        List<String> regList = new ArrayList<String>(registids.length);
        for (String registid : registids) {
            if (registid != null && !"".equals(registid.trim())) {
                regList.add(registid);
            }
        }
        return regList.toArray(new String[0]);
    }

}
