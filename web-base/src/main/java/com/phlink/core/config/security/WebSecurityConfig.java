package com.phlink.core.config.security;

import com.phlink.core.config.properties.IgnoredUrlsProperties;
import com.phlink.core.config.properties.PhlinkTokenProperties;
import com.phlink.core.security.UserDetailsServiceImpl;
import com.phlink.core.security.jwt.AuthenticationFailHandler;
import com.phlink.core.security.jwt.AuthenticationSuccessHandler;
import com.phlink.core.security.jwt.JWTAuthenticationFilter;
import com.phlink.core.security.jwt.RestAccessDeniedHandler;
import com.phlink.core.security.permission.MyFilterSecurityInterceptor;
import com.phlink.core.security.validator.image.ImageValidateFilter;
import com.phlink.core.security.validator.sms.SmsValidateFilter;
import com.phlink.core.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 核心配置类
 * 开启注解控制权限至Controller
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PhlinkTokenProperties tokenProperties;

    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailHandler failHandler;

    @Autowired
    private RestAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Autowired
    private ImageValidateFilter imageValidateFilter;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private SmsValidateFilter smsValidateFilter;
    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;
    @Autowired
    private UsernameAuthenticationConfig usernameAuthenticationConfig;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("SpringSecurity 初始化");
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();

        // 除配置文件忽略路径其它所有请求都需经过认证和授权
        for (String url : ignoredUrlsProperties.getUrls()) {
            registry.antMatchers(url).permitAll();
        }
        registry.and()
                // 表单登录方式
                .formLogin()
                .loginPage("/common/needLogin")
                // 登录请求url
                .loginProcessingUrl("/login")
                .permitAll()
                // 成功处理类
                .successHandler(successHandler)
                // 失败
                .failureHandler(failHandler)
                .and()
                // 允许网页iframe
                .headers().frameOptions().disable()
                .and()
                .logout()
                .permitAll()
                .and()
                .authorizeRequests()
                // 任何请求
                .anyRequest()
                // 需要身份认证
                .authenticated()
                .and()
                // 允许跨域
                .cors().and()
                // 关闭跨站请求防护
                .csrf().disable()
                // 前后端分离采用JWT 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 自定义权限拒绝处理类
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                // 图形验证码过滤器
                .addFilterBefore(imageValidateFilter, UsernamePasswordAuthenticationFilter.class)
                // 短信过滤器
                .addFilterBefore(smsValidateFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加自定义权限过滤器
                .addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                // 添加JWT认证过滤器
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), tokenProperties, redissonClient, securityUtil))
                .apply(smsAuthenticationConfig)
                .and()
                .apply(usernameAuthenticationConfig)
        ;
    }


}