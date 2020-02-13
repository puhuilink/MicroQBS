package com.phlink.bus.api.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    private static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simformat = new SimpleDateFormat(dateFormatType);
        return simformat.format(date);
    }

    public static String formatCSTTime(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date d = sdf.parse(date);
        return DateUtil.getDateFormat(d, format);
    }

    /**
     * 将日期字符串转换成指定格式的date
     *
     * @return
     */
    public static Date formatStr(String str, String formatStr) {

        Date date = null;
        if (str != null) {
            DateFormat sdf = new SimpleDateFormat(formatStr);
            try {
                date = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static LocalDateTime formatDateTimeStr(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FULL_TIME_SPLIT_PATTERN);
        return LocalDateTime.parse(str, formatter);
    }

    public static LocalDate formatDateStr(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(str, formatter);
    }

    /**
     * Date类型转LocalDate类型
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localPriceDate = instant.atZone(zoneId).toLocalDate();
        return localPriceDate;
    }

    /**
     * LocalDate类型转Date类型
     *
     * @param localDate
     * @return Date
     */
    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days)) + 1;
    }

    /**
     * 计算 minute 分钟后的时间
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 判断今天是否是周末
     *
     * @return
     */
    public synchronized static Boolean defineWeekEndToday() {
        LocalDate today = LocalDate.now();
        DayOfWeek week = today.getDayOfWeek();
        if (DayOfWeek.SATURDAY.equals(week) || DayOfWeek.SUNDAY.equals(week) ) {
            return true;
        }
        return false;
    }

    /**
     * long型时间转成LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public synchronized static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * localDateTime转成long
     *
     * @param localDateTime
     * @return
     */
    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * localDate转成时间戳long
     *
     * @param localDate
     * @return
     */
    public static long getTimestampOfLocalDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * localdate转String (yyyy-MM-dd)
     *
     * @param localDate
     * @return
     */
    public static String localDateToString(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(fmt);
    }

    /**
     * String(yyyy-MM-dd) 转LocalDate
     *
     * @param str
     * @return
     */
    public synchronized static LocalDate stringToLocalDate(String str) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(str, fmt);
    }
}
