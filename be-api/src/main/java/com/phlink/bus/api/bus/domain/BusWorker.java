package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 车辆司乘人员绑定信息
 *
 * @author wen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_bus_worker")
public class BusWorker extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 随车老师ID
     */
    private Long busTeacherId;

    /**
     * 所属车队
     */
    private Long deptId;

    /**
     * 绑定时间
     */
    private LocalDateTime bindTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 车辆ID
     */
    private Long busId;

    /**
     * 车架号
     */
    private String busCode;

    /**
    * 创建时间--开始时间
    */
    private transient Long createTimeFrom;
    /**
    * 创建时间--结束时间
    */
    private transient Long createTimeTo;

}
