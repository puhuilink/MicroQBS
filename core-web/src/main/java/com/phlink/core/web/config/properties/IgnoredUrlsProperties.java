/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:18:27
 * @FilePath: /phlink-common-framework/core-web/src/main/java/com/phlink/core/web/config/properties/IgnoredUrlsProperties.java
 */
package com.phlink.core.web.config.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "ignored")
public class IgnoredUrlsProperties {

    private List<String> urls = new ArrayList<>();
}
