package com.phlink.bus.api.serviceorg.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.serviceorg.domain.enums.SchoolFenceStatus;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 服务机构
 *
 * @author wen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_school")
@Excel("学校信息表")
public class School extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;


    @NotBlank(message = "{required}")
    @Size(max = 20, message = "{noMoreThan}")
    @ExcelField(value = "学校名称")
    @ApiModelProperty("学校名称")
    private String schoolName;

    /**
     * 所属组织
     */
    @ApiModelProperty("所属部门")
    private Long deptId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("操作时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty("操作人")
    private Long modifyBy;

    /**
     * 省
     */
    @ExcelField(value = "省")
    @NotBlank(message = "{required}")
    @ApiModelProperty("省")
    private String province;

    /**
     * 市
     */
    @ExcelField(value = "市")
    @NotBlank(message = "{required}")
    @ApiModelProperty("市")
    private String city;

    /**
     * 区/县
     */
    @ExcelField(value = "区/县")
    @NotBlank(message = "{required}")
    @ApiModelProperty("区/县")
    private String country;
    /**
     * 围栏状态
     */
    @ExcelField(value = "围栏配置状态", writeConverterExp = "UNCONFIGURED=未配置,CONFIGURED=已配置")
    @ApiModelProperty("围栏配置状态")
    private transient SchoolFenceStatus fenceStatus;

    @ApiModelProperty("围栏id")
    private transient String fenceId;
    /**
     * 服务班级数量
     */
    @ExcelField(value = "服务班级数量")
    @ApiModelProperty("服务班级数量")
    private transient Integer classNumber;
    /**
     * 服务学生数量
     */
    @ExcelField(value = "服务学生数量")
    @ApiModelProperty("服务学生数量")
    private transient Integer studentNumber;

    @ExcelField(value = "所属部门")
    @ApiModelProperty("所属部门")
    private transient String deptName;


}
