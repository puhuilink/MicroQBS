package com.phlink.bus.api.serviceorg.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.converter.LocalDateTimeConverter;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author zhouyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_classes")
@Excel("班级信息表")
public class Classes extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ApiModelProperty("学校id")
    private Long schoolId;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ExcelField(value = "创建时间", writeConverter = LocalDateTimeConverter.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("操作人")
    private Long modifyBy;

    @ApiModelProperty("操作时间")
    private LocalDateTime modifyTime;

    /**
     * 年级，用数字表示，如一年级存储  1
     */
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ExcelField(value = "年级")
    @ApiModelProperty(value = "年级",example = "1")
    private Integer grade;

    /**
     * 年级升级版，随系统年份增长，和school_system一起用于判断是否毕业
     */
    @ApiModelProperty(value = "年级升级版，随系统年份增长，和school_system一起用于判断是否毕业",example = "1")
    private Integer gradePro;

    /**
     * 班级， 用数字表示，如一班 存储 1
     */
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ExcelField(value = "班级")
    @ApiModelProperty(value = "班级， 用数字表示",example = "1")
    private Integer classLevel;

    /**
     * 老师id
     */
    @ApiModelProperty(value = "老师id")
    private Long[] teacherId;

    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String degree;

    /**
     * 入学年份
     */
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    @ExcelField(value = "入学年份")
    @ApiModelProperty(value = "入学年份",example = "2019")
    private Integer enrollYear;

    /**
     * 学制
     */
    @ApiModelProperty(value = "学制",example = "6")
    private Integer schoolSystem;

    @ExcelField(value = "服务学生数")
    @ApiModelProperty(value = "服务学生数")
    private transient Integer studentNum;
    @ExcelField(value = "学校名称")
    @ApiModelProperty(value = "学校名称")
    private transient String schoolName;

}
