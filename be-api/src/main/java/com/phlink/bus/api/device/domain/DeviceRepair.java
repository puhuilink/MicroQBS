package com.phlink.bus.api.device.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 设备维修表
 *
 * @author zhouyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device_repair")
public class DeviceRepair extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 维修原因
     */
    private String comment;

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
     * 创建人
     */

    private Long createBy;

    /**
     * 维修状态
     */
    private String status;
    /**
     * 创建时间--开始时间
     */
    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "设备名称")
    @TableField(exist = false)
    private transient String deviceName;
    /**
     * 设备标识码
     */
    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "设备标识码")
    @TableField(exist = false)
    private transient String deviceCode;
    /**
     * 设备类型
     */
    @Size(max = 100, message = "{noMoreThan}")
//    @ExcelField(value = "设备类型",writeConverterExp = "0=手环,1=有效")
    @TableField(exist = false)
    private transient String deviceType;
    /**
    * 创建时间--开始时间
    */
    @TableField(exist = false)
    private transient Long createTimeFrom;
    /**
    * 创建时间--结束时间
    */
    @TableField(exist = false)
    private transient Long createTimeTo;

}
