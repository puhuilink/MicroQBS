package com.phlink.biz.core.message.configuration;

import com.cloopen.rest.sdk.CCPRestSDK;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsRestApiConfiguration {

    @Bean
    public CCPRestSDK ccpRestSDK() {
        CCPRestSDK restAPI = new CCPRestSDK();
        restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
        restAPI.setAccount("8a216da85c62c9ad015cb034616b1e15", "3ad60ef8fe6d4af8b7551ea1e945d258");// 初始化主帐号和主帐号TOKEN
        restAPI.setAppId("8a216da85c62c9ad015cb03461ab1e1a");// 初始化应用ID
        return restAPI;
    }

}
