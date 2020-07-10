/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:18:36
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/properties/PhlinkLimitProperties.java
 */
package com.phlink.qbs.core.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "phlink.ratelimit")
public class PhlinkLimitProperties {

    /**
     * 是否开启全局限流
     */
    private Boolean enable = false;

    /**
     * 限制请求个数
     */
    private Integer limit = 100;

    /**
     * 每单位时间内（毫秒）
     */
    private Integer timeout = 1000;
}
