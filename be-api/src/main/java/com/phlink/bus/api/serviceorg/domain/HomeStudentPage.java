package com.phlink.bus.api.serviceorg.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class HomeStudentPage implements Serializable {

	private static final long serialVersionUID = 1L;

 
	/**
	 * 布置给哪天
	 */
	private LocalDate homeworkDate;
	/**
	 * 作业文件名称
	 */
	private String homeworkName;
	/**
	 * 班级作业状态（0未布置-1已布置-2已查看-3已下载-4（家长）已上传-5已批阅）
	 */
	private String homeworkStatus = "0";
	 
	 
	/**
	 * 作业链接
	 * 
	 */
	private String homeworkLink;
	/**
	 * 作业评心
	 * 
	 */
	private int homeworkLevel;
	/**
	 * 作业ID
	 * 
	 */
	private Long homeworkId;
	/**
	 * 学生ID
	 * 
	 */
	private Long studentId;
	/**
	 * 作业与学生关系的ID
	 * 
	 */
	private Long id;

}