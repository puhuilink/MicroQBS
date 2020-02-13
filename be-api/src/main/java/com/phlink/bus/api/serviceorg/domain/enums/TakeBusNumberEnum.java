package com.phlink.bus.api.serviceorg.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TakeBusNumberEnum implements IEnum<Integer> {
	// 2次
	TWO(2),
	// 4次
	FOUR(4),
	;

	TakeBusNumberEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	@JsonValue
	@Override
	public Integer getValue() {
		return this.code;
	}

	@JsonCreator
	public static TakeBusNumberEnum of(Integer value) {
		if (null == value) {
			return null;
		}
		for (TakeBusNumberEnum item : TakeBusNumberEnum.values()) {
			if (value.equals(item.getValue())) {
				return item;
			}
		}
		throw new IllegalArgumentException("TakeBusNumberEnum: unknown value: " + value);
	}
}
