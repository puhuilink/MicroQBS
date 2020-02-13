package com.phlink.bus.api.attendance.domain.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.phlink.bus.api.common.converter.LocalDateTimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

@Data
@Excel("司乘打卡信息")
public class BusAttendanceVO {
	private Long id;
	private Long userId;
	@ExcelField(value = "姓名")
	private String realname;
	private String userName;
	@ExcelField(value = "角色")
	private String roleName;
	private Long roleId;
	@ExcelField(value = "电话")
	private String mobile;
	@ExcelField(value = "车牌号")
	private String numberPlate;
	@ExcelField(value = "打卡状态", writeConverterExp = "1=发车打卡,2=收车打卡")
	private String type;
	private String address;
	private LocalDate time;
	@ExcelField(value = "打卡时间", writeConverter = LocalDateTimeConverter.class)
	private LocalDateTime createTime;
	private int pageNum;
	private int pageSize;
	private String createTimeTo;
	private String createTimeFrom;
	@ExcelField(value = "打卡地址")
	private String position;
	private LocalDateTime timeTo;
	private LocalDateTime timeFrom;
	private String source;
}
