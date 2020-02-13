package com.phlink.bus.api.serviceorg.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class HomeWorkPage implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 作业ID
	 */
	private Long homeworkId;
	/**
	 * 下载数量
	 */
	private Integer downNum = 0;

	/**
	 * 查看数量
	 */
	private Integer browseNum = 0;

	/**
	 * 班级人数，作业表没有值，从班级表取
	 */
	private Integer classNum;

	/**
	 * 班级id
	 */
	private Long classId;

	private String className;

	/**
	 * 班级作业状态（0 未布置 1 已布置）
	 */
	private String homeworkClassStatus = "0";

}