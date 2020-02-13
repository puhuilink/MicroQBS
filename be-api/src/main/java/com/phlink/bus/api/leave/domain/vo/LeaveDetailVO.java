package com.phlink.bus.api.leave.domain.vo;

import com.phlink.bus.api.common.converter.LocalDateTimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Excel(value = "请假信息表")
public class LeaveDetailVO {

    private Long id;
    private Long studentId;
    private String reason;

    /**
     *行程ID列表
     */
    private Long [] tripId;

    /**
     * 提交人ID
     */
    private Long applyId;

    /**
     * 请假选择班次
     */
    private String [] busTime;

    /**
     * 签名
     */
    private String signImg;

    /**
     * 排序字段
     */
    private transient String sortField;

    /**
     * 排序规则 ascend 升序 descend 降序
     */
    private transient String sortOrder;

    /**
     * 创建时间--开始时间
     */
    private transient Long createTimeFrom;

    /**
     * 创建时间--结束时间
     */
    private transient Long createTimeTo;


    @ExcelField(value = "学生姓名")
    private String studentName;
    @ExcelField(value = "学校姓名")
    private String schoolName;
    @ExcelField(value = "请假人姓名")
    private String applyName;
    @ExcelField(value = "请假人电话")
    private String applyMobile;
    @ExcelField(value = "请假开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaveDateStart;
    @ExcelField(value = "请假结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaveDateEnd;
    @ExcelField(value = "上午上学", writeConverterExp = "true=请假,false=未请假")
    private Boolean busTime1;
    @ExcelField(value = "上午放学", writeConverterExp = "true=请假,false=未请假")
    private Boolean busTime2;
    @ExcelField(value = "下午上学", writeConverterExp = "true=请假,false=未请假")
    private Boolean busTime3;
    @ExcelField(value = "下午放学", writeConverterExp = "true=请假,false=未请假")
    private Boolean busTime4;
    @ExcelField(value = "上车站点")
    private String stopName;
    @ExcelField(value = "班级")
    private Integer classLevel;
    @ExcelField(value = "年级")
    private Integer grade;
    @ExcelField(value = "入学年份")
    private Integer enrollYear;
    @ExcelField(value = "随车老师姓名")
    private String busTeacherName;
    @ExcelField(value = "随车老师电话")
    private String busTeacherMobile;
    @ExcelField(value = "请假时间", writeConverter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;
}
