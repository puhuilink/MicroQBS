package com.phlink.bus.api.serviceorg.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum DegreeEnum implements IEnum<String> {
	// 幼儿园
	KINDERGARTEN("1", "幼儿园"),
	// 小学
	PRIMARYSCHOOL("2", "小学"),
	// 初中
	MIDDLESCHOOL("3", "初中"),
	// 高中
	HIGHSCHOOL("4", "高中");

	DegreeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private String code;
	private String desc;

	@Override
	public String getValue() {
		return this.code;
	}
}
