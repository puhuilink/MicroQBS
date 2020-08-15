/*
 * @Author: sevncz.wen
 * @Date: 2020-03-25 20:31:15
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:18:41
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/properties/PhlinkTokenProperties.java
 */
package com.puhuilink.qbs.auth.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "qbs.token")
public class QbsTokenProperties {

    /**
     * 单点登陆
     */
    private Boolean sdl = true;

    /**
     * 存储权限数据
     */
    private Boolean storePerms = true;

    /**
     * token默认过期时间
     */
    private Integer tokenExpireTime = 30;

    /**
     * 用户选择保存登录状态对应token过期时间（天）
     */
    private Integer saveLoginTime = 7;

    /**
     * 限制用户登陆错误次数（次）
     */
    private Integer loginTimeLimit = 10;

    /**
     * 登陆错误剩余提醒
     */
    private Integer loginTimeNotify = 5;

    /**
     * 错误超过次数后多少分钟后才能继续登录（分钟）
     */
    private Integer loginAfterTime = 10;
}
