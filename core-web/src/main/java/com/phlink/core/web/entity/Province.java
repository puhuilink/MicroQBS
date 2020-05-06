package com.phlink.core.web.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


/**
 * @author wen
 */
@Data
@TableName("t_province")
public class Province implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @TableId(value = "_id", type = IdType.INPUT)
    private Integer id;

    private String name;

    private String provinceId;

}
