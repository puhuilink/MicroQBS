/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:15:53
 * @FilePath: /phlink-common-framework/core-web/src/main/java/com/phlink/core/web/config/P6spySqlFormatConfig.java
 */
package com.phlink.core.web.config;

import java.util.Date;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.date.DateUtil;

/**
 * 自定义 p6spy sql输出格式
 */
public class P6spySqlFormatConfig implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared,
            String sql, String url) {
        return StringUtils.isNotBlank(sql)
                ? DateUtil.formatDateTime(new Date()) + " | 耗时 " + elapsed + " ms | SQL 语句：" + StringUtils.LF
                        + sql.replaceAll("[\\s]+", StringUtils.SPACE) + ";"
                : "";
    }
}
