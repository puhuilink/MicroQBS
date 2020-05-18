/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:15:40
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:15:40
 */
package com.phlink.core.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "ignored")
public class IgnoredUrlsProperties {

    private List<String> urls = new ArrayList<>();
}
