package com.phlink.core.web.base.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "phlink-common")
public class PhlinkProperties {
    private boolean openAopLog = true;
}
