package com.phlink.bus.api.common.converter;

import com.phlink.bus.api.common.utils.DateUtil;
import com.wuwenze.poi.convert.ReadConverter;
import com.wuwenze.poi.exception.ExcelKitReadConverterException;

public class CustomizeFieldReadToLocalDateConverter implements ReadConverter {
    /**
     * 读取单元格时，将值进行转换（此处示例为计算单元格字符串char的总和）
     */
    @Override
    public Object convert(Object o) throws ExcelKitReadConverterException {
        String value = (String) o;
        return DateUtil.formatDateStr(value);
    }

}
