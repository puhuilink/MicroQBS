package com.phlink.bus.api.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 设备信息关系表
 *
 * @author zhouyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device_relation")
public class DeviceRelation extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 创建人
     */

    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 操作人
     */
    private Long modifyBy;

    /**
     * 操作时间
     */
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
