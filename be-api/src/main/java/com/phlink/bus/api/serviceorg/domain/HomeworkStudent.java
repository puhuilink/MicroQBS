package com.phlink.bus.api.serviceorg.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_homework_student")
public class HomeworkStudent extends ApiBaseEntity {

	private static final long serialVersionUID = 1L;

	private Long stuId;

	private Long homeworkId;

	/**
	 * 0未布置-1已布置-2已查看-3已下载-4（家长）已上传-5已批阅
	 */
	private String homeworkStatus;

	/**
	 * 布置给哪天
	 */
	private LocalDate homeworkDate;

	/**
	 * 家长上传作业图片链接，逗号分割
	 */
	private String img;

	/**
	 * 批阅评心
	 */
	private Integer homeworkLevel;

	private Long createBy;

	private LocalDateTime createTime;

	private Long modifyBy;

	private LocalDateTime modifyTime;
	/**
	 * 创建时间--开始时间
	 */
	private transient Long createTimeFrom;
	/**
	 * 创建时间--结束时间
	 */
	private transient Long createTimeTo;
	/**
	 * 是否已下载
	 */
	private boolean isDownload;

}
