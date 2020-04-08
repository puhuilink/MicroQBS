/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 18:44:32
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-04-02 18:46:11
 */
package com.phlink.core.web.config.gson;

import java.io.Writer;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import com.phlink.core.web.base.utils.ResultUtil;
import com.phlink.core.web.base.vo.Result;

import org.springframework.http.converter.json.GsonHttpMessageConverter;

public class CustomGsonHttpMessageConverter extends GsonHttpMessageConverter {

    @Override
    protected void writeInternal(Object o, @Nullable Type type, Writer writer) throws Exception {
        Result<Object> result = null;
        if (o instanceof Result) {
            result = (Result) o;
        } else if (o == null) {
            result = ResultUtil.success("OK");
        } else {
            result = ResultUtil.data(o);
        }
        super.getGson().toJson(result, writer);
    }

}
