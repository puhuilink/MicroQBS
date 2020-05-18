/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:15:44
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:15:44
 */
package com.phlink.core.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "phlink.iplimit")
public class PhlinkIpLimitProperties {
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
