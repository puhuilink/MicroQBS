/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:16:10
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/WebConfig.java
 */
package com.phlink.qbs.core.web.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.phlink.qbs.core.base.gson.LocalDateDeserializer;
import com.phlink.qbs.core.base.gson.LocalDateSerializer;
import com.phlink.qbs.core.base.gson.LocalDateTimeDeserializer;
import com.phlink.qbs.core.base.gson.LocalDateTimeSerializer;
import com.phlink.qbs.core.base.gson.LocalTimeDeserializer;
import com.phlink.qbs.core.base.gson.LocalTimeSerializer;
import com.phlink.qbs.core.web.aop.AppInterceptor;
import com.phlink.qbs.core.web.config.gson.CustomGsonHttpMessageConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.hutool.core.date.DatePattern;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public Gson buildGson() {
        Gson gson = new GsonBuilder().setDateFormat(DatePattern.NORM_DATETIME_PATTERN)
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalTime.class, new LocalTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).disableHtmlEscaping()
                .serializeNulls().create();
        return gson;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Gson gson = buildGson();
        CustomGsonHttpMessageConverter gsonHttpMessageConverter = new CustomGsonHttpMessageConverter();
        gsonHttpMessageConverter.setGson(gson);
        converters.add(0, gsonHttpMessageConverter);
        converters.add(1, new ResourceHttpMessageConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInterceptor());
    }
}
