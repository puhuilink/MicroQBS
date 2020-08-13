/*
 * @Author: sevncz.wen
 * @Date: 2020-07-31 11:14
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-07-31 11:14
 */
package com.puhuilink.qbs.core.common.config;

import com.puhuilink.qbs.core.common.config.httpclient.HttpClientConfig;
import com.puhuilink.qbs.core.common.config.httpclient.RestTemplateConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        WebConfig.class,
        Swagger2Config.class,
        HttpClientConfig.class,
        RestTemplateConfig.class,
        MybatisConfig.class
})
public class CommonApplicationConfig {
}
