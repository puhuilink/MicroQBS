/*
 * @Author: sevncz.wen
 * @Date: 2020-03-26 17:52:35
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-06-01 11:17:29
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/WebSecurityConfig.java
 */
package com.phlink.qbs.core.web.config;

import java.util.List;

import com.phlink.qbs.core.web.config.properties.IgnoredUrlsProperties;
import com.phlink.qbs.core.web.security.auth.jwt.JwtAuthenticationProvider;
import com.phlink.qbs.core.web.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.phlink.qbs.core.web.security.auth.jwt.RestAccessDeniedHandler;
import com.phlink.qbs.core.web.security.auth.jwt.SkipPathRequestMatcher;
import com.phlink.qbs.core.web.security.auth.jwt.extractor.TokenExtractor;
import com.phlink.qbs.core.web.security.auth.rest.RestAuthenticationProvider;
import com.phlink.qbs.core.web.security.auth.rest.RestImageLoginProcessingFilter;
import com.phlink.qbs.core.web.security.auth.rest.RestLoginProcessingFilter;
import com.phlink.qbs.core.web.security.auth.rest.RestMobileLoginProcessingFilter;
import com.phlink.qbs.core.web.security.permission.MyFilterSecurityInterceptor;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Security 核心配置类 开启注解控制权限至Controller
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String WEBJARS_ENTRY_POINT = "/webjars/**";
    public static final String USERNAME_LOGIN_ENTRY_POINT = "/api/auth/login";
    public static final String MOBILE_LOGIN_ENTRY_POINT = "/api/auth/login/mobile";
    public static final String IMAGE_LOGIN_ENTRY_POINT = "/api/auth/login/image";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";
    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/**";
    public static final String WS_TOKEN_BASED_AUTH_ENTRY_POINT = "/api/ws/**";
    public static final String NOAUTH_ENTRY_POINT = "/api/auth/**";
    protected static final String[] NON_TOKEN_BASED_AUTH_ENTRY_POINTS = new String[] { "/index.html", "/static/**",
            "/api/auth/**", "/api/common/**", "/webjars/**" };

    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    @Autowired
    private TokenExtractor jwtHeaderTokenExtractor;
    @Autowired
    private TokenExtractor jwtQueryTokenExtractor;
    @Autowired
    private RestAuthenticationProvider restAuthenticationProvider;
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;
    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;
    @Autowired
    private RedissonClient redissonClient;

    private SkipPathRequestMatcher buildSkipPathRequestMatcher() {
        List<String> pathsToSkip = CollUtil.newArrayList(NON_TOKEN_BASED_AUTH_ENTRY_POINTS);
        pathsToSkip.addAll(CollUtil.newArrayList(WS_TOKEN_BASED_AUTH_ENTRY_POINT, TOKEN_REFRESH_ENTRY_POINT,
                USERNAME_LOGIN_ENTRY_POINT, MOBILE_LOGIN_ENTRY_POINT, IMAGE_LOGIN_ENTRY_POINT, WEBJARS_ENTRY_POINT,
                NOAUTH_ENTRY_POINT));
        pathsToSkip.addAll(ignoredUrlsProperties.getUrls());
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
        return matcher;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.userDetailsService(userDetailsService).passwordEncoder(new
        // BCryptPasswordEncoder());
        auth.authenticationProvider(restAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    protected RestLoginProcessingFilter buildRestLoginProcessingFilter() throws Exception {
        RestLoginProcessingFilter filter = new RestLoginProcessingFilter(USERNAME_LOGIN_ENTRY_POINT, successHandler,
                failureHandler);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected RestMobileLoginProcessingFilter buildRestPublicLoginProcessingFilter() throws Exception {
        RestMobileLoginProcessingFilter filter = new RestMobileLoginProcessingFilter(MOBILE_LOGIN_ENTRY_POINT,
                successHandler, failureHandler, redissonClient);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected RestImageLoginProcessingFilter buildRestImageLoginProcessingFilter() throws Exception {
        RestImageLoginProcessingFilter filter = new RestImageLoginProcessingFilter(IMAGE_LOGIN_ENTRY_POINT,
                successHandler, failureHandler, redissonClient);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
        SkipPathRequestMatcher matcher = buildSkipPathRequestMatcher();
        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler,
                jwtHeaderTokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected JwtTokenAuthenticationProcessingFilter buildWsJwtTokenAuthenticationProcessingFilter() throws Exception {
        AntPathRequestMatcher matcher = new AntPathRequestMatcher(WS_TOKEN_BASED_AUTH_ENTRY_POINT);
        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler,
                jwtQueryTokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("SpringSecurity 初始化");
        http.headers().cacheControl().and().frameOptions().disable().and().cors().and().csrf().disable()
                .exceptionHandling().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(WEBJARS_ENTRY_POINT).permitAll()
                .antMatchers(USERNAME_LOGIN_ENTRY_POINT).permitAll().antMatchers(MOBILE_LOGIN_ENTRY_POINT).permitAll()
                .antMatchers(IMAGE_LOGIN_ENTRY_POINT).permitAll().antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll()
                .antMatchers(NON_TOKEN_BASED_AUTH_ENTRY_POINTS).permitAll()
                .antMatchers(ignoredUrlsProperties.getUrls().toArray(new String[0])).permitAll()
                .antMatchers(WS_TOKEN_BASED_AUTH_ENTRY_POINT).authenticated().antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT)
                .authenticated().anyRequest().authenticated().and().exceptionHandling()
                .accessDeniedHandler(restAccessDeniedHandler).and()
                .addFilterBefore(buildRestLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildRestPublicLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildWsJwtTokenAuthenticationProcessingFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                // 添加自定义权限过滤器
                .addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
    }
}
