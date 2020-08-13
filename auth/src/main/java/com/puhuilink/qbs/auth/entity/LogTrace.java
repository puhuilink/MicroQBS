package com.puhuilink.qbs.auth.entity;

import java.time.LocalDateTime;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableName;
import com.puhuilink.qbs.core.common.utils.ObjectUtil;
import com.puhuilink.qbs.core.common.base.QbsBaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_log")
public class LogTrace extends QbsBaseEntity {
    @ApiModelProperty(value = "方法操作名称")
    private String name;

    @ApiModelProperty(value = "日志类型 0登陆日志 1操作日志")
    private Integer logType;

    @ApiModelProperty(value = "请求路径")
    private String requestUrl;

    @ApiModelProperty(value = "请求类型")
    private String requestType;

    @ApiModelProperty(value = "请求参数")
    private String requestParam;

    @ApiModelProperty(value = "请求用户")
    private String username;

    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "ip信息")
    private String ipInfo;

    @ApiModelProperty(value = "花费时间")
    private Integer costTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 转换请求参数为Json
     *
     * @param paramMap
     */
    public void setMapToParams(Map<String, String[]> paramMap) {

        this.requestParam = ObjectUtil.mapToString(paramMap);
    }
}
