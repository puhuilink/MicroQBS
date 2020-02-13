package com.phlink.bus.common;

public class Constants {
    public static final String TOKEN_CACHE_PREFIX = "api.cache.token.";
    public static final String QUEUE_DEVICE_INFO = "iot.queue.deviceinfo.";
    public static final String QUEUE_DVR_INFO = "iot.queue.dvrinfo.";

    /**
     * 验证码缓存前缀
     */
    public static final String CAPTCHA_CACHE_PREFIX = "api.cache.captcha.";
    public static final String MSG_TYPE_CAPTCHA = "CAPTCHA";
    /**
     * 添加共同监护人信息前缀
     */
    public static final String ADD_GUARDIAN_CACHE_PREFIX = "api.cache.add-guardian.";
    public static final String MSG_TYPE_ADD_GUARDIAN = "ADD_GUARDIAN";
    /**
     * 绑定学生路线信息前缀
     */
    public static final String BIND_GUARDIAN_CACHE_PREFIX = "api.cache.bind-guardian.";
    public static final String MSG_TYPE_BIND_GUARDIAN = "BIND_GUARDIAN";
    /**
     * 验证码缓存时间
     */
    public static final String CAPTCHA_EXPIRE_CONFIG = "cache-expire";


    public static final String CACHE_GAODE_POINT = "cache.gaode.point";

    /**
     * 校车当前站点时刻队列
     */
    public static final String QUEUE_STOP_TIME_PREFIX = "queue.current.stoptime.";
    public static final String QUEUE_COMPLETE_STOP_TIME_PREFIX = "queue.current.complete.stoptime.";

    public static final String QUEUE_BINDBUS_NEEDCHECK = "queue.bindbus.needcheck";

//    public static final String CACHE_STOP_TIME_PREFIX = "cache.current.stoptime.";
//    public static final String CACHE_STOP_TIME_KEY_CURRENT = "current";
//    public static final String CACHE_STOP_TIME_KEY_NEXT = "next";
    /**
     * 等待运行的行程
     */
    public static final String QUEUE_BINDBUS_WAITING_TRIP_PREFIX = "queue.bindbus.waiting.trip.";
    public static final String QUEUE_BINDBUS_RUNNING_TRIP_PREFIX = "queue.bindbus.running.trip.";
    public static final String QUEUE_BINDBUS_FINISHED_TRIP_PREFIX = "queue.bindbus.finished.trip.";

    public static final String QUEUE_BINDBUS_STOPTIME_STUDENT_PREFIX = "queue.bindbus.stoptime.student.";
    public static final String QUEUE_BINDBUS_HOME_UP_STOPTIME_STUDENT_PREFIX = "queue.bindbus.home.up.stoptime.student.";
    public static final String QUEUE_BINDBUS_SCHOOL_DOWN_STOPTIME_STUDENT_PREFIX = "queue.bindbus.school.down.stoptime.student.";
    public static final String QUEUE_BINDBUS_SCHOOL_UP_STOPTIME_STUDENT_PREFIX = "queue.bindbus.school.up.stoptime.student.";
    public static final String QUEUE_BINDBUS_HOME_DOWN_STOPTIME_STUDENT_PREFIX = "queue.bindbus.home.down.stoptime.student.";

    /**
     * 车辆离线告警
     */
    public static final String BUS_OFFLINE_ALARM_PREFIX = "cache.bus.offline.alarm.";

    public static final String ALARM_PAUSE_PREFIX = "cache.pause.alarm.";
    public static final String ALARM_SAME_PREFIX = "cache.same.alarm.";

    public static final String ALARM_PAUSE_DEVICE_PREFIX = "cache.pause.device.alarm.";
    public static final String ALARM_SAME_DEVICE_PREFIX = "cache.same.device.alarm.";


    // 队列保存的历史最大数量
    public static final Integer QUEUE_CAPACITY = 20000;

    public static final String MQ_QUEUE_SEND_SMS = "queue.sendSms";
    public static final String MQ_EXCHANGE_SEND_SMS = "exchange.sendSms";
    public static final String MQ_ROUTE_SEND_SMS = "mq.sms.sender";


    public static final String MQ_QUEUE_SEND_EWATCH = "mq.iot.positionInfo";
    public static final String MQ_EXCHANGE_SEND_EWATCH = "mq.iotExChange";
    public static final String MQ_ROUTE_SEND_EWATCH = "mq.positionInfo.send";


    public static final String MQ_QUEUE_SEND_PUSH = "mq.pushMsg";
    public static final String MQ_EXCHANGE_SEND_PUSH = "exchange.pushMsg";
    public static final String MQ_ROUTE_SEND_PUSH = "mq.msg.send";
}
