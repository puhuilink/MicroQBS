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
@TableName("t_homework")
public class Homework extends ApiBaseEntity {

	private static final long serialVersionUID = 1L;

	private String homeworkName;

	private LocalDate homeworkStartDate;

	private LocalDate homeworkEndDate;

	/**
	 * 下载数量
	 */
	private Integer downNum;

	/**
	 * 查看数量
	 */
	private Integer browseNum;

	/**
	 * 班级人数
	 */
	private Integer classNum;

	/**
	 * 班级id
	 */
	private Long classId;

	/**
	 * 班级作业状态（0 未布置 1 已布置）
	 */
	private String homeworkClassStatus;

	/**
	 * 作业上传的的链接
	 */
	private String homeworkLink;

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

}
