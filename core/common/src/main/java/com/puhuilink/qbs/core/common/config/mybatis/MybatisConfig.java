/*
 * @Author: sevncz.wen
 * @Date: 2020-08-13 14:26
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-13 14:26
 */
package com.puhuilink.qbs.core.common.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: qbs-web
 * @description: 开启乐观锁
 * @author: sevncz.wen
 * @create: 2020-08-13 14:26
 **/
@Configuration
public class MybatisConfig {

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

}
