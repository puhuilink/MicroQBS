package com.phlink.bus.api.alarm.domain;

public class BusApiAlarmContants {

    public static final String BUS_STOP_NAME = "站点名称";
    public static final String BUS_STOP_THRESHOLD = "时间阈值(min)";
    public static final String BUS_STOP_DELAY = "迟到时间(min)";
    public static final String BUS_STOP_LOCATION = "迟到站点位置";

    public static final String BUS_SPEED = "当前车速(km/h)";
    public static final String BUS_SPEED_ALARM = "车速阈值(km/h)";
    public static final String BUS_SPEED_LOCATION = "发生超速时的位置";

    public static final String BUS_ROUTE_THRESHOLD = "偏离阈值(m)";
    public static final String BUS_ROUTE_DISTANCE = "当前偏离(m)";
    public static final String BUS_ROUTE_LOCATION = "发生偏离时的位置";

    public static final String LEAVE_FENCE = "偏离围栏";
    public static final String LEAVE_LOCATION = "偏离围栏的位置";

    public static final String BUS_OFFINE_CODE = "设备编号";
    public static final String BUS_OFFINE_THRESHOLD= "告警阈值(min)";
    public static final String BUS_OFFINE_TIME= "失联时长(min)";
    public static final String BUS_OFFINE_LOCATION = "失联设备的位置";
}
