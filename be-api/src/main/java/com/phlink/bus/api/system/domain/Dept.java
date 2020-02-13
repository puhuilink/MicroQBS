package com.phlink.bus.api.system.domain;

import com.phlink.bus.api.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_dept")
@Excel("部门信息表")
public class Dept implements Serializable {

    private static final long serialVersionUID = -7790334862410409053L;


    @TableId(value = "DEPT_ID", type = IdType.ID_WORKER)
    private Long deptId;

    private Long parentId;

    @NotBlank(message = "{required}")
    @Size(max = 20, message = "{noMoreThan}")
    @ExcelField(value = "部门名称")
    private String deptName;

    @ExcelField(value = "所属部门名称")
    private transient String parentDeptName;

    @Max(message = "{noMoreThan}", value = 999)
    private Double orderNum;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;

    private Boolean im;

    private transient String createTimeFrom;

    private transient String createTimeTo;

    private Boolean buildGroup;

}