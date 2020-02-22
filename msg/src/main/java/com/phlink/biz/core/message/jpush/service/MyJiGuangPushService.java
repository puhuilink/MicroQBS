package com.phlink.biz.core.message.jpush.service;

import com.phlink.biz.core.message.configuration.JiGuangPushConfiguration;

@Slf4j
@Service
public class MyJiGuangPushService {
    @Autowired
    private JiGuangPushConfiguration jPushConfig;

    /**
     * 广播
     *
     * @param pushBean 推送内容
     * @return
     */
    public boolean push_android_and_ios(PushBean pushBean) {
        return sendPush(PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.android(pushBean.getAlert(), pushBean.getTitle(), pushBean.getExtras()))
                .build());
    }

    /**
     * 通过registid推送 (一次推送最多 1000 个)
     *
     * @param pushBean  推送内容
     * @param registids 推送id
     * @return
     */
    public boolean push_android_and_ios(PushBean pushBean, String... registids) {
        return sendPush(PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(registids))
                .setNotification(
                        Notification.newBuilder()
                                .setAlert(pushBean.getAlert())
                                .addPlatformNotification(AndroidNotification.newBuilder()
                                        .setTitle(pushBean.getTitle())
                                        .addExtras(pushBean.getExtras())
                                        .setStyle(1)
                                        .setBigText(pushBean.getAlert())
                                        .build())
                                .addPlatformNotification(IosNotification.newBuilder()
                                        .incrBadge(1)
                                        .setAlert(pushBean.getAlert())
                                        .addExtras(pushBean.getExtras()).build())
                                .build()
                )
                .build());
    }

    /**
     * 调用api推送
     *
     * @param pushPayload 推送实体
     * @return
     */
    public boolean sendPush(PushPayload pushPayload) {
        log.info("发送极光推送请求: {}", pushPayload);
        Map<String, PushResult> result = null;
        try {
            result = jPushConfig.getGroupPushClient().sendGroupPush(pushPayload);
            for (Map.Entry<String, PushResult> entry : result.entrySet()) {
                PushResult pushResult = entry.getValue();
                PushResult.Error error = pushResult.error;
                if (error != null) {
                    log.info("AppKey: " + entry.getKey() + " error code : " + error.getCode() + " error message: " + error.getMessage());
                } else {
                    log.info("AppKey: " + entry.getKey() + " sendno: " + pushResult.sendno + " msg_id:" + pushResult.msg_id);
                }

            }
        } catch (APIConnectionException e) {
            log.error("极光推送连接异常: ", e);
        } catch (APIRequestException e) {
            log.error("极光推送请求异常: ", e);
        }
        return true;
    }
}
