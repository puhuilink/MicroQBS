package com.phlink.bus.api.common.utils;

import com.wuwenze.poi.annotation.ExcelField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {



    public static Map<String, String> getExcelMapping(Class className) {
        Map<String, String> result = new HashMap<>();
        Field[] fields = className.getDeclaredFields();
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if(excelField != null) {
                result.put(excelField.value(), field.getName());
            }
        }
        return result;
    }

    public static Map<String, Field> getExcelFieldMapping(Class className) {
        Map<String, Field> result = new HashMap<>();
        Field[] fields = className.getDeclaredFields();
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if(excelField != null) {
                result.put(excelField.value(), field);
            }
        }
        return result;
    }

}
