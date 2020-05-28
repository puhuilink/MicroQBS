/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:18:03
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/mybatis/MybatisPlusConfig.java
 */
package com.phlink.core.web.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.phlink.core.*.mapper"})
public class MybatisPlusConfig {

    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
