/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-06-01 11:48:24
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/properties/IgnoredUrlsProperties.java
 */
package com.puhuilink.qbs.auth.config.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "qbs.entrypoint")
public class QbsApiEntryPointProperties {
    private String base = "/api";
    private QbsAuthEntryPoint auth = new QbsAuthEntryPoint();
    private List<String> ignored = new ArrayList<>();


}
