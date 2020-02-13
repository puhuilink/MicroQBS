package com.phlink.bus.api.leave.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.phlink.bus.api.common.converter.LocalDateTimeConverter;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.route.domain.enums.TripTimeEnum;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author zhouyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_leave")
@Excel("请假信息表")
public class Leave extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    private Long studentId;

    private String reason;

    /**
     * 请假日期开始
     */
    @ExcelField(value = "请假开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaveDateStart;
    
    /**
     * 请假日期结束
     */
    @ExcelField(value = "请假结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaveDateEnd;
    
    /**
     *行程ID列表 
     */
    private Long [] tripId;

    @ExcelField(value = "请假时间", writeConverter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

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


}
