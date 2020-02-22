package com.phlink.biz.core.message.configuration;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.GroupPushClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class JiGuangPushConfiguration {
    // 极光官网-个人管理中心-appkey
    @Value("${push.key}")
    private String key;
    // 极光官网-个人管理中心-点击查看-secret
    @Value("${push.secret}")
    private String secret;
    private GroupPushClient jPushClient;

    // 推送客户端
    @PostConstruct
    public void initJPushClient() {
        jPushClient = new GroupPushClient(secret, key);
    }

    // 获取推送客户端
    public GroupPushClient getGroupPushClient() {
        return jPushClient;
    }
}
