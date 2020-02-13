package com.phlink.bus.api.serviceorg.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class HomeClassPage implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 班级id
	 */
	private Long classId;
	/**
	 * 学生姓名
	 */
	private String studentName;

	/**
	 * 班级作业状态（0未布置-1已布置-2已查看-3已下载-4（家长）已上传-5已批阅）
	 */
	private String homeworkStatus ="0";
	/**
	 * 监护人id
	 * 
	 */
	private Long guardianId;
	/**
	 * 监护人姓名
	 * 
	 */
	private String guardianName;
	/**
	 * 作业链接
	 * 
	 */
	private String homeworkLink;
	/**
	 * 作业Id
	 * 
	 */
	private Long homeworkId;
	/**
	 * 作业Id
	 * 
	 */
	private int homeworkLevel;

}