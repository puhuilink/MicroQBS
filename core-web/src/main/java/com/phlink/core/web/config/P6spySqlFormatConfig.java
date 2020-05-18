/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:14:36
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:14:36
 */
package com.phlink.core.web.config;

import cn.hutool.core.date.DateUtil;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

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
