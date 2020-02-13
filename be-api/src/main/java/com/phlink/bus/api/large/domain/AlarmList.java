package com.phlink.bus.api.large.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/13$ 16:04$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/13$ 16:04$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class AlarmList {

    private  String driverMobile;

    private String numberPlate;

    private String driverName;

    private Integer alarmLevel;

    private Integer alarmType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date alarmTime;

}
