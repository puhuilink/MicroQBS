package com.phlink.bus.api.common.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.phlink.bus.api.common.utils.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
public class JacksonConfig {

    @Value("${spring.jackson.date-format}")
    private String formatValue;

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String timeFormat = "HH:mm:ss";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

//    @Bean("jackson2ObjectMapperBuilderCustomizer")
//    public Jackson2ObjectMapperBuilderCustomizer customJackson() {
//        return jacksonObjectMapperBuilder -> {
//            jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
//            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
//            jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
//
//            jacksonObjectMapperBuilder.simpleDateFormat(dateTimeFormat);
//            jacksonObjectMapperBuilder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
//            jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
//            jacksonObjectMapperBuilder.serializers(new LocalTimeSerializer(DateTimeFormatter.ofPattern(timeFormat)));
//        };
//    }

    @Bean
    public ObjectMapper objectMapper() {

        //LocalDateTime系列序列化和反序列化模块，继承自jsr310，我们在这里修改了日期格式
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // LocalDateTime
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        // LocalDate
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
        // LocalTime
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(timeFormat)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(timeFormat)));
        //Date序列化和反序列化
        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(formatValue)));
        javaTimeModule.addDeserializer(Date.class, new DateCustomDeserializer());

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);

        ObjectMapper objectMapper = new ObjectMapper()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .registerModule(simpleModule)
                .registerModule(javaTimeModule);

        return objectMapper;
    }


    class DateCustomDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            String dateString = p.getText().trim();
            if(StringUtils.isNotBlank(dateString)){
                try {
                    return DateFormatUtil.pareDate(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

}
