/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:15:34
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:15:34
 */
package com.phlink.core.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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
