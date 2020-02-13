package com.phlink.bus.api.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.phlink.bus.api.common.converter.LocalDateTimeConverter;
import com.phlink.bus.api.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 
 *
 * @author wen
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_config")
public class SystemConfig implements Serializable {


    @TableId(value = "CONFIG_ID", type = IdType.ID_WORKER)
    private Long configId;

    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "键")
    private String key;

    @NotBlank(message = "{required}")
    @Size(max = 20, message = "{noMoreThan}")
    @ExcelField(value = "值")
    private String value;

    @NotBlank(message = "{required}")
    @Size(max = 200, message = "{noMoreThan}")
    @ExcelField(value = "描述")
    private String description;

    @ExcelField(value = "创建时间", writeConverter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;


    private Long createBy;

    @ExcelField(value = "修改时间", writeConverter = LocalDateTimeConverter.class)
    private LocalDateTime modifyTime;

    private Long modifyBy;

}
