package com.phlink.core.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @author wen
 */
@Data
@TableName("t_province")
public class Province implements Serializable {

    @TableId(value = "_id", type = IdType.INPUT)
    private Integer id;

    private String name;

    private String provinceId;

}
