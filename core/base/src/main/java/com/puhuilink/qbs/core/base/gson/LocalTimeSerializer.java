/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 18:44:58
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-04-02 18:44:58
 */
package com.puhuilink.qbs.core.base.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.puhuilink.qbs.core.base.constant.Constant;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeSerializer implements JsonSerializer<LocalTime> {

    @Override
    public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

}
