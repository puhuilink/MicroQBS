package com.puhuilink.qbs.core.limiter.config;

import com.puhuilink.qbs.core.limiter.aop.LimitAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {LimitAspect.class})
public class RateLimiterApplicationConfig {
}