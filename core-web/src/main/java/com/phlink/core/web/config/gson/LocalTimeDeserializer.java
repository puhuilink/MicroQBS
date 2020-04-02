/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 18:44:55
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-04-02 18:44:55
 */
package com.phlink.core.web.config.gson;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalTime();
    }

}
