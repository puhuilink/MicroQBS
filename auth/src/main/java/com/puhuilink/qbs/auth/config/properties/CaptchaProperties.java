/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:18:23
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/properties/CaptchaProperties.java
 */
package com.puhuilink.qbs.auth.config.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {

    private List<String> username = new ArrayList<>();

    private List<String> email = new ArrayList<>();

    private List<String> image = new ArrayList<>();

    private List<String> sms = new ArrayList<>();

    private List<String> vaptcha = new ArrayList<>();
}
