package com.phlink.bus.api.common.converter;

import com.phlink.bus.api.common.utils.DateUtil;
import com.wuwenze.poi.convert.WriteConverter;
import com.wuwenze.poi.exception.ExcelKitWriteConverterException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Execl导出时间类型字段格式化
 */
@Slf4j
public class LocalDateTimeConverter implements WriteConverter {
    @Override
    public String convert(Object value) throws ExcelKitWriteConverterException {
        try {
            if (value == null)
                return "";
            else {
                return value.toString().replace("T", " ");
            }
        } catch (Exception e) {
            log.error("时间转换异常", e);
            return "";
        }
    }
}
