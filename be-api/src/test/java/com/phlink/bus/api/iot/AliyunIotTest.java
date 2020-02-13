package com.phlink.bus.api.iot;

import com.aliyun.openservices.iot.api.Profile;
import com.aliyun.openservices.iot.api.message.MessageClientFactory;
import com.aliyun.openservices.iot.api.message.api.MessageClient;
import com.aliyun.openservices.iot.api.message.callback.MessageCallback;
import com.aliyun.openservices.iot.api.message.entity.Message;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class AliyunIotTest {


    @Test
    public void testConnect() throws InterruptedException {
        // 阿里云accessKey
        String accessKey = "LTAI4FeeyX3brZzCi6PD2UvP";
        // 阿里云accessSecret
        String accessSecret = "Vt4dcJMcx4d48jY75rlL0fdPBjYV06";
        // regionId
        String regionId = "cn-shanghai";
        // 阿里云uid
        String uid = "1617604132577423";
        // endPoint:  https://${uid}.iot-as-http2.${region}.aliyuncs.com
        String endPoint = "https://" + uid + ".iot-as-http2." + regionId + ".aliyuncs.com";

        // 连接配置
        Profile profile = Profile.getAccessKeyProfile(endPoint, regionId, accessKey, accessSecret);

        // 构造客户端
        MessageClient client = MessageClientFactory.messageClient(profile);
        // 数据接收
        client.connect(messageToken -> {
            Message m = messageToken.getMessage();
            System.out.println("receive message from " + m);
            return MessageCallback.Action.CommitSuccess;
        });

        TimeUnit.SECONDS.sleep(10);
    }



}
