package com.phlink.bus.api.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* 
*
* @author wen
*/
@Data
@TableName("sys_country")
public class Country implements Serializable {

    @TableId(value = "_id", type = IdType.INPUT)
    private Integer id;

    private String name;

    private String countryId;

    private String cityId;

}
