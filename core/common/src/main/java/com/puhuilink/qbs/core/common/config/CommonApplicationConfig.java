/*
 * @Author: sevncz.wen
 * @Date: 2020-07-31 11:14
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-07-31 11:14
 */
package com.puhuilink.qbs.core.common.config;

import com.puhuilink.qbs.core.common.config.httpclient.HttpClientConfig;
import com.puhuilink.qbs.core.common.config.httpclient.RestTemplateConfig;
import com.puhuilink.qbs.core.common.config.mybatis.MybatisPlusConfig;
import com.puhuilink.qbs.core.common.config.mybatis.PageInterceptorConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {WebConfig.class, Swagger2Config.class, MybatisPlusConfig.class, PageInterceptorConfig.class, HttpClientConfig.class, RestTemplateConfig.class})
public class CommonApplicationConfig {
}
