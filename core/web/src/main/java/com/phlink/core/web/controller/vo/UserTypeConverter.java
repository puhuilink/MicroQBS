package com.phlink.core.web.controller.vo;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.phlink.core.base.constant.CommonConstant;

public class UserTypeConverter implements Converter<Integer> {
    @Override
    public Class supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty,
            GlobalConfiguration globalConfiguration) throws Exception {
        String value = cellData.getStringValue();
        if ("正常".equals(value)) {
            return CommonConstant.USER_STATUS_NORMAL;
        }
        return CommonConstant.USER_STATUS_LOCK;
    }

    @Override
    public CellData convertToExcelData(Integer value, ExcelContentProperty excelContentProperty,
            GlobalConfiguration globalConfiguration) throws Exception {
        if (CommonConstant.USER_STATUS_NORMAL.equals(value)) {
            return new CellData("正常");
        }
        return new CellData("锁定");
    }
}
