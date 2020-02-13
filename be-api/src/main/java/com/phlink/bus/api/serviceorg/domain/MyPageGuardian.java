package com.phlink.bus.api.serviceorg.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 我的 - - > 监护人列表 bean
 */
@Data
public class MyPageGuardian implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentId;

	/**
	 * 家长ID，来自user表
	 */
	private Long guardianId;

	/**
	 * 是否可以请假 0 否 1是  
	 */
	private Boolean leaveApplyStatus;

	/**
	 * 是否是主监护人0 否 1是  
	 */
	private Boolean mainGuardianStatus;
	private String username;// 姓名
	private String avatar;// 头像
	private String mobile;// 电话
}
