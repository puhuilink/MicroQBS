package com.phlink.bus.api.serviceorg.domain.VO;

import com.phlink.bus.api.serviceorg.domain.enums.GuardianStatusEnum;
import com.phlink.bus.api.serviceorg.domain.enums.ServiceInvalidReasonEnum;
import com.phlink.bus.api.serviceorg.domain.enums.ServiceStatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentInfoVO {
    private Long id;
    /**
     * 学生姓名
     */
    private String studentName;
    /**
     * 生日
     */
    private LocalDate studentBirthday;
    /**
     * 服务状态
     */
    private ServiceStatusEnum serviceStatus;
    /**
     * 服务开始时间
     */
    private LocalDate serviceStartDate;

    /**
     * 服务结束时间
     */
    private LocalDate serviceEndDate;
    /**
     * 服务失效原因
     */
    private ServiceInvalidReasonEnum serviceInvalidReason;

    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 入学年份
     */
    private Integer enrollYear;
    /**
     * 年级
     */
    private Integer grade;
    /**
     * 班级
     */
    private Integer classLevel;
    /**
     * 主责姓名
     */
    private String guardianRealname;
    /**
     * 主责状态 0=锁定,1=有效
     */
    private GuardianStatusEnum guardianStatus;

    /**
     * 请假权限
     */
    private Boolean guardianLeavePermissions;

    /**
     * 主责电话
     */
    private String guardianMobile;

    /**
     * 手环编码
     */
    private String deviceCode;

    /**
     * 站点名称
     */
    private String stopName;

    private String routeId;

    private String stopId;
}
