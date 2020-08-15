/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 18:44:55
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-04-02 18:44:55
 */
package com.puhuilink.qbs.core.base.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.puhuilink.qbs.core.base.constant.Constant;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.TIME_FORMAT);
        return LocalTime.parse(json.getAsJsonPrimitive().getAsString(), formatter);
    }

}
