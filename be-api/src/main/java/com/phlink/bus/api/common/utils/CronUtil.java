package com.phlink.bus.api.common.utils;

import java.time.LocalTime;

/**
 * Cron表达式工具类（只针对特定cron表达式）
 */
public class CronUtil {

    public static String localTimeToCron(LocalTime date){
        int hour=date.getHour();
        int minute=date.getMinute();
        return "0 "+minute + " "+ hour+ " ? * *";
    }

    public static LocalTime cronToLocalTime(String cron) {
        String[] str=cron.split(" ");
        return LocalTime.of(Integer.parseInt(str[2]),Integer.parseInt(str[1]));
    }
}

