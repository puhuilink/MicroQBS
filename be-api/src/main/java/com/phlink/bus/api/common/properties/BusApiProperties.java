package com.phlink.bus.api.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "bus-api")
public class BusApiProperties {

    private ShiroProperties shiro = new ShiroProperties();

    private boolean openAopLog = true;

    private ImServerProperties im = new ImServerProperties();

    private DvrServerProperties dvr = new DvrServerProperties();

    private IotServerProperties iot = new IotServerProperties();

}
