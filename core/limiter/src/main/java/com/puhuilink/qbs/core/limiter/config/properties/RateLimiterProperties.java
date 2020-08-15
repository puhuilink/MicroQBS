/*
 * @Author: sevncz.wen
 * @Date: 2020-08-13 16:39
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-13 16:39
 */
package com.puhuilink.qbs.core.limiter.config.properties;

import com.puhuilink.qbs.core.limiter.enums.RuleAuthority;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @program: qbs-web
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-13 16:39
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "qbs.ratelimit")
public class RateLimiterProperties {

    private boolean enable = true;
    /**
     * 全局每秒请求数量
     */
    private Double QPS = Double.MAX_VALUE;

    private RuleAuthority ruleAuthority = RuleAuthority.NULL;
    /**
     * 全局限流黑/白名单
     */
    private List<String> ipList;

}
