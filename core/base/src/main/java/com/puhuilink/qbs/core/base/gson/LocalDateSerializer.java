/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 18:44:46
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-04-02 18:44:46
 */
package com.puhuilink.qbs.core.base.gson;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cn.hutool.core.date.DatePattern;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
    }
}
