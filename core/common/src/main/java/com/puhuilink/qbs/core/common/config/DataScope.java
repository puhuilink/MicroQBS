/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:17:50
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/mybatis/DataScope.java
 */
package com.puhuilink.qbs.core.common.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;

/**
 * 数据权限查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataScope extends HashMap {
	/**
     *
     */
    private static final long serialVersionUID = 5528100842169903358L;

    /**
     * 限制范围的字段名称
     */
	private String scopeName = "departmentId";

	/**
	 * 具体的数据范围
	 */
	private List<Integer> deptIds;

	/**
	 * 是否只查询本部门
	 */
	private Boolean isOnly = false;
}
