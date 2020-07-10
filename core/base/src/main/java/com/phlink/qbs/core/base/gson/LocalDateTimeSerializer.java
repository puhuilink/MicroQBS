/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 18:44:53
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-04-02 18:44:53
 */
package com.phlink.qbs.core.base.gson;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cn.hutool.core.date.DatePattern;

public class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
    }

}
