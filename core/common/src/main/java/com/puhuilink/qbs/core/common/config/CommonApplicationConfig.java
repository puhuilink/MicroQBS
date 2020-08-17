/*
 * @Author: sevncz.wen
 * @Date: 2020-07-31 11:14
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-07-31 11:14
 */
package com.puhuilink.qbs.core.common.config;

import com.puhuilink.qbs.core.common.config.httpclient.HttpClientConfig;
import com.puhuilink.qbs.core.common.config.httpclient.RestTemplateConfig;
import com.puhuilink.qbs.core.common.config.mybatis.MybatisConfig;
import com.puhuilink.qbs.core.limiter.aop.LimitAspect;
import com.puhuilink.qbs.core.limiter.config.properties.RateLimiterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackageClasses = {
        WebConfig.class,
        Swagger2Config.class,
        HttpClientConfig.class,
        RestTemplateConfig.class,
        MybatisConfig.class
})
public class CommonApplicationConfig {
    @Autowired
    ApplicationContext applicationContext;

    public CommonApplicationConfig() {
    }

    @PostConstruct
    public void init() {
    }
}
