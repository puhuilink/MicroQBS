package com.puhuilink.qbs.auth.controller.vo;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.puhuilink.qbs.core.base.constant.CommonConstant;

public class UserStatusConverter implements Converter<Integer> {
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
        if ("管理员".equals(value)) {
            return CommonConstant.USER_TYPE_ADMIN;
        }
        return CommonConstant.USER_TYPE_NORMAL;
    }

    @Override
    public CellData convertToExcelData(Integer value, ExcelContentProperty excelContentProperty,
            GlobalConfiguration globalConfiguration) throws Exception {
        if (CommonConstant.USER_TYPE_ADMIN.equals(value)) {
            return new CellData("管理员");
        }
        return new CellData("普通用户");
    }
}
