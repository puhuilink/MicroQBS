/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 18:44:50
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-04-02 18:44:50
 */
package com.puhuilink.qbs.core.base.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.puhuilink.qbs.core.base.constant.Constant;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.DATETIME_FORMAT);
        return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), formatter);
    }

}
