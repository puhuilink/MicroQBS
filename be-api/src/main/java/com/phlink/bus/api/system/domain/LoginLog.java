package com.phlink.bus.api.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("sys_login_log")
@Data
public class LoginLog implements Serializable {
    /**
     * 登录平台
     */
    public static final String LOGIN_PASSWORD = "0";

    public static final String LOGIN_MOBILE = "1";


    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 用户 ID
     */
    private String username;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录地点
     */
    private String location;

    private String loginWay;

    private String ip;
}
