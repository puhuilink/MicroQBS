/*
 * @Author: sevncz.wen
 * @Date: 2020-04-02 14:38:40
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-04-02 14:38:40
 */
package com.phlink.core.web.config.mybatis;

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
