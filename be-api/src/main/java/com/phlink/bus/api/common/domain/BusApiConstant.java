package com.phlink.bus.api.common.domain;

import com.phlink.bus.common.Constants;

/**
 * API常量
 */
public class BusApiConstant {

    /**
     * user缓存前缀
     */
    public static final String USER_CACHE_PREFIX = "api.cache.user.";
    /**
     * user角色缓存前缀
     */
    public static final String USER_ROLE_CACHE_PREFIX = "api.cache.user.role.";
    /**
     * user权限缓存前缀
     */
    public static final String USER_PERMISSION_CACHE_PREFIX = "api.cache.user.permission.";
    /**
     * user个性化配置前缀
     */
    public static final String USER_CONFIG_CACHE_PREFIX = "api.cache.user.config.";
    /**
     * token缓存前缀
     */
    public static final String TOKEN_CACHE_PREFIX = Constants.TOKEN_CACHE_PREFIX;
    /**
     * token缓存前缀
     */
    public static final String ONLY_LOGIN_TOKEN_CACHE_PREFIX = "api.cache.onlylogin.token.";

    /**
     * 通讯IM服务登录的cookie缓存
     */
    public static final String IM_COOKIE = "api.im.cookie";
    /**
     * DVR服务登录的TOKEN缓存
     */
    public static final String DVR_TOKEN_PREFIX = "api.dvr.token";
    public static final String DVR_LAST_TIME_PREFIX = "api.dvr.last.time.";
    /**
     * iot服务器登录TOKEN缓存
     */
    public static final String IOT_TOKEN = "api.iot.token";

    /**
     * 高德地图key
     */
    public static final String AMAP_KEY_CONFIG = "amap-key";

    /**
     * 告警规则缓存
     */
    private static final String ALARM_RULES_HASH_PREFIX = "api.cache.alarm.rules.";
    //路线开关
    public static final String ROUTE_SWITCH = "api.cache.route-switch";
    //站点开关
    public static final String STOP_SWITCH = "api.cache.stop-switch";
    //车辆开关
    public static final String BUS_SWITCH = "api.cache.bus-switch";
    //路线周末开关
    public static final String ROUTE_WEEKEND_SWITCH = "api.cache.route-weekend-switch";
    //路线规则失效时间
    public static final String ROUTE_INVALID_DATE = "api.cache.route-invalid-date";
    /*   //学校开关
       public static final String SCHOOL_SWITCH = "api.cache.school-switch";*/
    //手环开关
    public static final String DEVICE_SWITCH = "api.cache.device-switch";
    //学校周末开关
    public static final String SCHOOL_WEEKEND_SWITCH = "api.cache.school-weekend-switch";

    //路线电子围栏定时
    public static final String ROUTE_FENCE = "api.cache.route-job";
    //车辆、设备最新位置信息
    public static final String LOCATION = "api.cache.location";

    /**
     * 车辆告警
     */
    public static final String ALARM_ROUTE = "api.cache.alarm.route";
    /**
     * 车辆告警
     */
    public static final String ALARM_SPEED = "api.cache.alarm.speed";
    /**
     * 车辆高德tid
     */
    public static final String BUS = "api.cache.bus";
    /**
     * 车辆与电子围栏关系(百度)
     */
    public static final String BUS_ROUTE_FENCE = "api.cache.bus.bus-routefenceid";
    /**
     * 手环与电子围栏关系(高德)
     */
    public static final String DEVICE_SCHOOL_FENCE = "api.cache.bus.bus-schoolfenceid";

    /**
     * 车辆与站点围栏关系(高德)
     */
    public static final String BUS_STOP_FENCE = "api.cache.bus.bus-stopfenceid";
    /**
     * 路线与车辆
     */
    public static final String ROUTE_BUS = "api.cache.bus.routeid-buscode";
    /**
     * 站点最新记录
     */
    public static final String BUS_STOP_LOG = "api.cache.bus.bus-stoplog";

    /**
     * 手环高德tid
     */
    public static final String DEVICE = "api.cache.device";

    /**
     * 手环、车辆对应规则id
     */
    public static final String CODE_RULE = "api.cache.code-rule";

    /**
     * 高德地图service key
     */
    public static final String AMAP_SERVICE_KEY_CONFIG = "amap-service-key";

    /**
     * 百度地图key
     */
    public static final String BAIDU_KEY_CONFIG = "baidu-key";

    /**
     * 百度地图service key
     */
    public static final String BAIDU_SERVICE_KEY_CONFIG = "baidu-service-key";

    /**
     * 百度地图sk
     */
    public static final String BAIDU_SN_CONFIG = "baidu-sn";

    /**
     * 存储在线用户的 zset前缀
     */
    public static final String ACTIVE_USERS_ZSET_PREFIX = "api.user.active";
    /**
     * 存储系统配置的 hash前缀
     */
    public static final String SYSTEM_CONFIG_HASH_PREFIX = "api.system.config";

    /**
     * 存储地图服务service key 的 hash前缀
     */
    public static final String MAP_SERVICE_KEY_HASH_PREFIX = "map.service.key";

    /**
     * 排序规则： descend 降序
     */
    public static final String ORDER_DESC = "descend";
    /**
     * 排序规则： ascend 升序
     */
    public static final String ORDER_ASC = "ascend";

    /**
     * 按钮
     */
    public static final String TYPE_BUTTON = "1";
    /**
     * 菜单
     */
    public static final String TYPE_MENU = "0";
    /**
     * 作业访问url
     */
    public static final String HOMEWORK_FILE_URL = "homework_file_url";
    /**
     * 作业文件存储路径
     */
    public static final String HOMEWORK_PATH = "homework_path";

    /**
     * 主要责任人角色ID
     */
    public static final Long ROLE_MAINGUARDIAN = 11L;

    public static final Long ROLE_VISTOR = 10L;

    /**
     * 司机角色ID
     */
    public static final Long ROLE_DRIVER = 5L;

    /**
     * 随车老师角色ID
     */
    public static final Long ROLE_BUS_TEACHER = 6L;

    /**
     * 超级管理员角色ID
     */
    public static final Long ROLE_ADMIN = 1L;

    /**
     * 唯一登录标志
     */
    public static final String UNIQUE_LOGIN = "UniqueLogin";
}
