/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 14:38:14
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:18:13
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/mybatis/PageInterceptorConfig.java
 */
package com.puhuilink.qbs.core.mybatis.config;

import com.github.pagehelper.PageInterceptor;
import com.github.pagehelper.autoconfigure.PageHelperProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(PageHelperProperties.class)
@Configuration
public class PageInterceptorConfig {
    @Autowired
    private PageHelperProperties properties;

    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        pageInterceptor.setProperties(properties.getProperties());
        return pageInterceptor;
    }
}
