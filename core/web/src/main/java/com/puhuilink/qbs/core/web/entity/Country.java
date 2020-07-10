package com.puhuilink.qbs.core.web.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_country")
public class Country implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @TableId(value = "_id", type = IdType.INPUT)
    private Integer id;

    private String name;

    private String countryId;

    private String cityId;

}
