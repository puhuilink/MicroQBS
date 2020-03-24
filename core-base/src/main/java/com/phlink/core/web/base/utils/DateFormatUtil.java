package com.phlink.core.web.base.utils;

import cn.hutool.core.date.format.FastDateFormat;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

public class DateFormatUtil {

    public static final String SYMBOL_DOT = "\\.";

    public static final String DATE_REGEX_YYYYMM = "^\\d{4}-\\d{1,2}$";//日期正则yyyy-MM
    public static final String DATE_REGEX_YYYYMMDD = "^\\d{4}-\\d{1,2}-\\d{1,2}$";//日期正则yyyy-MM-dd
    public static final String DATE_REGEX_YYYYMMDDHHMM = "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$";//日期正则yyyy-MM-dd hh:mm
    public static final String DATE_REGEX_YYYYMMDDHHMMSS = "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$";//日期正则yyyy-MM-dd hh:mm:ss
    public static final String DATE_REGEX_SECOND_DOT_NANOSECOND = "^[0-9]{1,}\\.[0-9]{1,9}$";//Instant日期秒+纳秒
    public static final String DATE_REGEX_YYYYMMDD_T_HHMMSS_Z = "^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}Z$";//日期正则yyyy-MM-dd'T'HH:mm:ssZ
    public static final String DATE_REGEX_YYYYMMDD_T_HHMMSS_SSS_Z = "^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}Z$";//日期正则yyyy-MM-dd'T'HH:mm:ss.SSSZ


    // 以T分隔日期和时间，并带时区信息，符合ISO8601规范
    public static final String PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    public static final String PATTERN_ISO_ON_SECOND = "yyyy-MM-dd'T'HH:mm:ssZZ";
    public static final String PATTERN_ISO_ON_DATE = "yyyy-MM-dd";
    public static final String PATTERN_ISO_ON_MONTH = "yyyy-MM";

    // 以空格分隔日期和时间，不带时区信息
    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_DEFAULT_ON_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DEFAULT_ON_MINUTE = "yyyy-MM-dd HH:mm";

    // 使用工厂方法FastDateFormat.getInstance(), 从缓存中获取实例

    // 以T分隔日期和时间，并带时区信息，符合ISO8601规范
    public static final FastDateFormat ISO_FORMAT = FastDateFormat.getInstance(PATTERN_ISO);
    public static final FastDateFormat ISO_ON_SECOND_FORMAT = FastDateFormat.getInstance(PATTERN_ISO_ON_SECOND);
    public static final FastDateFormat ISO_ON_DATE_FORMAT = FastDateFormat.getInstance(PATTERN_ISO_ON_DATE);
    public static final FastDateFormat ISO_ON_MONTH_FORMAT = FastDateFormat.getInstance(PATTERN_ISO_ON_MONTH);

    // 以空格分隔日期和时间，不带时区信息
    public static final FastDateFormat DEFAULT_FORMAT = FastDateFormat.getInstance(PATTERN_DEFAULT);
    public static final FastDateFormat DEFAULT_ON_SECOND_FORMAT = FastDateFormat.getInstance(PATTERN_DEFAULT_ON_SECOND);
    public static final FastDateFormat DEFAULT_ON_MINUTE_FORMAT = FastDateFormat.getInstance(PATTERN_DEFAULT_ON_MINUTE);

    /**
     * 将日期格式的字符串转换成指定格式的日期
     * @param pattern 日期格式
     * @param dateString 日期字符串
     * @return
     * @throws ParseException
     */
    public static Date pareDate(@NotNull String pattern, @NotNull String dateString) throws ParseException {
        return FastDateFormat.getInstance(pattern).parse(dateString);
    }

    /**
     * 将日期格式的字符串根据正则转换成相应格式的日期
     * @param dateString 日期字符串
     * @return
     * @throws ParseException
     */
    public static Date pareDate(@NotNull String dateString) throws ParseException {
        String source = dateString.trim();
        if (StringUtils.isNotBlank(source)) {
            if(source.matches(DATE_REGEX_YYYYMM)){
                return ISO_ON_MONTH_FORMAT.parse(source);
            }else if(source.matches(DATE_REGEX_YYYYMMDD)){
                return ISO_ON_DATE_FORMAT.parse(source);
            }else if(source.matches(DATE_REGEX_YYYYMMDDHHMM)){
                return DEFAULT_ON_MINUTE_FORMAT.parse(source);
            }else if(source.matches(DATE_REGEX_YYYYMMDDHHMMSS)){
                return DEFAULT_ON_SECOND_FORMAT.parse(source);
            }else if(source.matches(DATE_REGEX_YYYYMMDD_T_HHMMSS_Z)){
                return ISO_ON_SECOND_FORMAT.parse(source);
            }else if(source.matches(DATE_REGEX_YYYYMMDD_T_HHMMSS_SSS_Z)){
                return ISO_FORMAT.parse(source);
            }else if(source.matches(DATE_REGEX_SECOND_DOT_NANOSECOND)){
                String[] split = source.split(SYMBOL_DOT);
                return Date.from(Instant.ofEpochSecond(Long.parseLong(split[0]), Long.parseLong(split[1])));
            }else {
                throw new IllegalArgumentException("Invalid date value '" + source + "'");
            }
        }
        return null;
    }

}