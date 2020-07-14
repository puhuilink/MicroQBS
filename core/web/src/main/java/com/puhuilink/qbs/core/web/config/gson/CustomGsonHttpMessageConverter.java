/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 18:29:29
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:17:31
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/gson/CustomGsonHttpMessageConverter.java
 */
package com.puhuilink.qbs.core.web.config.gson;

import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import com.puhuilink.qbs.core.base.vo.Result;

import org.springframework.http.converter.json.GsonHttpMessageConverter;

public class CustomGsonHttpMessageConverter extends GsonHttpMessageConverter {

    @Override
    protected void writeInternal(Object o, @Nullable Type type, Writer writer) throws Exception {
        Result<Object> result = null;
        if (o instanceof Result) {
            super.getGson().toJson(result, writer);
        } else if (type instanceof ParameterizedType) {
            getGson().toJson(o, type, writer);
        } else {
            getGson().toJson(o, writer);
        }
    }

}
