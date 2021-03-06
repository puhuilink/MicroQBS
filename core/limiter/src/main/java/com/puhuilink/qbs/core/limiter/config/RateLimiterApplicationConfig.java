package com.puhuilink.qbs.core.limiter.config;

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
        LimitAspect.class,
        RateLimiterProperties.class
})
public class RateLimiterApplicationConfig {
    @Autowired
    ApplicationContext applicationContext;

    public RateLimiterApplicationConfig() {
    }

    @PostConstruct
    public void init() {
    }
}