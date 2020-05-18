/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:15:55
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:15:55
 */
package com.phlink.core.web.config.mybatis;

import java.util.HashMap;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
