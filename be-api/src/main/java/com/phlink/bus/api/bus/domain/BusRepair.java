package com.phlink.bus.api.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 *
 * @author wen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_bus_repair")
public class BusRepair extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆ID

     */
    private Long busId;

    /**
     * 说明
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 维修状态 0: 处理中 1: 已修好 2: 未修好
     */
    private String status;

    private LocalDateTime modifyTime;

    private Long modifyBy;
    /**
    * 创建时间--开始时间
    */
    private transient Long createTimeFrom;
    /**
    * 创建时间--结束时间
    */
    private transient Long createTimeTo;

}
