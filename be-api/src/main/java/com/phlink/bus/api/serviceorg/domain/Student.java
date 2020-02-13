package com.phlink.bus.api.serviceorg.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.serviceorg.domain.enums.GuardianStatusEnum;
import com.phlink.bus.api.serviceorg.domain.enums.ServiceInvalidReasonEnum;
import com.phlink.bus.api.serviceorg.domain.enums.ServiceStatusEnum;
import com.phlink.bus.api.system.domain.SexOptions;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生信息
 *
 * @author zhouyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_student")
@Excel("学生信息表")
public class Student extends ApiBaseEntity {

    private static final long serialVersionUID = 1;

    @ExcelField(value = "学生姓名")
    @ApiModelProperty(value = "学生姓名")
    private String name;

    @ExcelField(value = "学号")
    @ApiModelProperty(value = "学号")
    private String studentCode;

    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    @ExcelField(value = "学校名称")
    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    @ApiModelProperty(value = "班级id")
    private Long classId;

    @ExcelField(value = "性别", writeConverterExp = "0=男,1=女", readConverterExp = "男=0,女=1", options = SexOptions.class)
    @ApiModelProperty(value = "性别")
    private String sex;

    @ExcelField(value = "年龄")
    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ExcelField(value = "出生年月日")
    @ApiModelProperty(value = "出生年月日")
    private LocalDate birthday;

    @ExcelField(value = "家庭住址")
    @ApiModelProperty(value = "家庭住址")
    private String address;

    @ApiModelProperty(value = "服务状态 0：失效 1：有效")
    private ServiceStatusEnum serviceStatus;

    @ApiModelProperty(value = "创建人id",hidden = true)
    private Long createBy;

    @ApiModelProperty(value = "创建时间",hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作人id",hidden = true)
    private Long modifyBy;

    @ApiModelProperty(value = "更新时间",hidden = true)
    private LocalDateTime modifyTime;

    @ExcelField(value = "服务开始时间")
    @ApiModelProperty(value = "服务开始时间")
    private LocalDate serviceStartDate;

    @ExcelField(value = "服务结束时间")
    @ApiModelProperty(value = "服务结束时间")
    private LocalDate serviceEndDate;

//    @ExcelField(value = "服务周期")
    @ApiModelProperty(value = "服务周期")
    private Integer serviceCycle;

    @ApiModelProperty(value = "站点id")
    private Long stopId;

    @ApiModelProperty(value = "路线关联id")
    private Long routeOperationId;

    @ApiModelProperty(value = "备注")
    private String context;

    @ExcelField(value = "入学年份")
    @ApiModelProperty(value = "入学年份")
    private Integer startYear;

    @ApiModelProperty(value = "失效原因")
    private ServiceInvalidReasonEnum serviceInvalidReason;

    @ApiModelProperty(value = "主责家长ID")
    private Long mainGuardianId;

    @ApiModelProperty(value = "请假家长ID")
    private Long leaveGuardianId;

    @ExcelField(value = "主责人姓名")
    @ApiModelProperty(value = "主责人姓名")
    private transient String mainGuardianName;

    @ExcelField(value = "主责人电话")
    @ApiModelProperty(value = "主责人电话")
    private transient String mainGuardianMobile;

    @ExcelField(value = "主责人身份证号")
    @ApiModelProperty(value = "主责人身份证号码")
    private transient String mainGuardianIdcard;

    @ApiModelProperty(value = "主责人出生年月日")
    private transient LocalDate mainGuardianBirthday;

    @ApiModelProperty(value = "主责人推送ID")
    private transient String mainRegistrationId;

    // writeConverterExp = "1=母亲,2=父亲,3=爷爷,4=奶奶,5=外公,6=外婆,7=舅舅,8=舅妈,9=姑姑,10=姨妈"
    // （1：母亲；2：父亲；3：爷爷；4：奶奶；5：外公；6：外婆；7：舅舅；8：舅妈；9：姑姑；10：姨妈）
    @ExcelField(value = "主责和学生关系", options = StudentRelationOptions.class)
    @ApiModelProperty(value = "和孩子的关系")
    private String mainGuardianRelation;

    @ApiModelProperty(value = "主责人状态(0:冻结;1:正常)")
    private transient GuardianStatusEnum mainGuardianStatus;

    @ApiModelProperty(value = "请假权限")
    private transient Boolean leavePermissions;

    @ExcelField(value = "请假监护人姓名")
    @ApiModelProperty(value = "请假监护人姓名")
    private transient String leaveGuardianName;

    @ExcelField(value = "请假监护人电话")
    @ApiModelProperty(value = "请假监护人电话")
    private transient String leaveGuardianMobile;

    @ExcelField(value = "年级")
    @ApiModelProperty(value = "年级")
    private transient Integer grade;

    @ExcelField(value = "班级")
    @ApiModelProperty(value = "班级")
    private transient Integer classLevel;

    @ExcelField(value = "手环编号")
    @ApiModelProperty(value = "手环编号")
    private transient String deviceCode;

    @ExcelField(value = "乘车站点")
    @ApiModelProperty(value = "站点名称")
    private transient String stopName;

    @ApiModelProperty(value = "站点ID")
    private transient Long routeId;

    @ExcelField(value = "乘车路线")
    @ApiModelProperty(value = "路线ID")
    private transient String routeName;

    @ApiModelProperty(value = "最后登录时间(主责人)")
    private transient LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "登录次数(主责人)",example = "0",hidden = true)
    private transient Integer loginTimes;

    @ApiModelProperty(value = "车辆id")
    private Long busId;

    @ExcelField(value = "车牌")
    @ApiModelProperty(value = "车牌号码")
    private transient String numberPlate;

    @ExcelField(value = "车号")
    @ApiModelProperty(value = "车号")
    private transient String busCode;

    @ApiModelProperty("头像")
    private String avatar;

    public void setBirthday(LocalDate birthday) {
        if(this.age == null) {
            LocalDate now = LocalDate.now();
            if(now.isBefore(birthday)) {
                return;
            }
            int yearNow = now.getYear();
            int monthNow = now.getMonthValue();
            int dayOfMonth = now.getDayOfMonth();

            int age = yearNow - birthday.getYear();
            if (monthNow <= birthday.getMonthValue()) {
                if (monthNow == birthday.getMonthValue()) {
                    if (dayOfMonth < birthday.getDayOfMonth()) {
                        //当前日期在生日之前，年龄减一
                        age--;
                    }
                } else {
                    //当前月份在生日之前，年龄减一
                    age--;
                }
            }
            this.age = age;
        }
        this.birthday = birthday;

    }
}
