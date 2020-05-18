/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:16:03
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:16:03
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
