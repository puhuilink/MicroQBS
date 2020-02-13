package com.phlink.bus.api.serviceorg.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.serviceorg.domain.enums.CalendarStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
* 
*
* @author wen
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_calendar")
public class Calendar extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    private Long schoolId;

    /**
     * Indicates whether the service is valid for all Mondays within the date range specified by the start_date and end_date fields. The following are valid values for this field:
        1: Service is available for all Mondays in the date range.
        0: Service isn't available for Mondays in the date range.
     */
    private CalendarStatusEnum monday;

    private CalendarStatusEnum tuesday;

    private CalendarStatusEnum wednesday;

    private CalendarStatusEnum thursday;

    private CalendarStatusEnum friday;

    private CalendarStatusEnum saturday;

    private CalendarStatusEnum sunday;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

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
    private transient Long startDateFrom;

    /**
     * 创建时间--结束时间
     */
    private transient Long startDateTo;


}
