package com.phlink.core.web.base.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("s_logtrace")
public class SysLogTrace extends BaseEntity {
    private String username;
    private String ip;
    // 执行时长
    private Long time;
    private String operation;
    private String method;
    private String params;
    private String location;
}
