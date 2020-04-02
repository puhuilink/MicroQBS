/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 18:44:41
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-04-02 18:44:41
 */
package com.phlink.core.web.config.gson;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDate();
    }

}
