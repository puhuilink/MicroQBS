/*
 * @Author: sevncz.wen
 * @Date: 2020-08-19 13:57
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-19 13:57
 */
package com.puhuilink.qbs.auth.config;

import com.puhuilink.qbs.auth.controller.auth.CaptchaController;
import com.puhuilink.qbs.auth.controller.common.RegionController;
import com.puhuilink.qbs.auth.controller.manage.UserController;
import com.puhuilink.qbs.auth.mapper.UserMapper;
import com.puhuilink.qbs.auth.security.auth.jwt.AuthenticationSuccessHandler;
import com.puhuilink.qbs.auth.security.auth.jwt.extractor.JwtHeaderTokenExtractor;
import com.puhuilink.qbs.auth.security.auth.rest.RestAuthenticationProvider;
import com.puhuilink.qbs.auth.security.permission.MyAccessDecisionManager;
import com.puhuilink.qbs.auth.service.impl.UserServiceImpl;
import com.puhuilink.qbs.auth.utils.SecurityUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @program: qbs-web
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-19 13:57
 **/
@Configuration
@MapperScan(basePackageClasses = {
        UserMapper.class
})
@ComponentScan(basePackageClasses = {
        WebSecurityConfig.class,
        RestAuthenticationProvider.class,
        AuthenticationSuccessHandler.class,
        MyAccessDecisionManager.class,
        JwtHeaderTokenExtractor.class,
        UserServiceImpl.class,
        SecurityUtil.class,
        CaptchaController.class,
        RegionController.class,
        UserController.class,
})
public class AuthApplicationConfig {
    @Autowired
    ApplicationContext applicationContext;

    public AuthApplicationConfig() {
    }

    @PostConstruct
    public void init() {
    }
}
