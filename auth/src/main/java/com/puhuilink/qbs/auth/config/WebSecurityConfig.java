/*
 * @Author: sevncz.wen
 * @Date: 2020-03-26 17:52:35
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-06-01 11:17:29
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/WebSecurityConfig.java
 */
package com.puhuilink.qbs.auth.config;

import com.google.common.collect.Lists;
import com.puhuilink.qbs.auth.config.properties.QbsApiEntryPointProperties;
import com.puhuilink.qbs.auth.security.auth.jwt.JwtAuthenticationProvider;
import com.puhuilink.qbs.auth.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.puhuilink.qbs.auth.security.auth.jwt.RestAccessDeniedHandler;
import com.puhuilink.qbs.auth.security.auth.jwt.SkipPathRequestMatcher;
import com.puhuilink.qbs.auth.security.auth.jwt.extractor.TokenExtractor;
import com.puhuilink.qbs.auth.security.auth.rest.RestAuthenticationProvider;
import com.puhuilink.qbs.auth.security.auth.rest.RestImageLoginProcessingFilter;
import com.puhuilink.qbs.auth.security.auth.rest.RestLoginProcessingFilter;
import com.puhuilink.qbs.auth.security.auth.rest.RestMobileLoginProcessingFilter;
import com.puhuilink.qbs.auth.security.permission.MyFilterSecurityInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

/**
 * Security 核心配置类 开启注解控制权限至Controller
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 用户名密码登录
//    public String usernameLoginEntryPoint = "/auth/login";
    // 手机验证码登录
//    public String mobileLoginEntryPoint = "/auth/login/mobile";
    // 图片验证码登录
//    public String IMAGE_LOGIN_ENTRY_POINT = "/auth/login/image";
    // 所有/api的请求都拦截
//    public String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";
    // websocket
    public static final String WS_TOKEN_BASED_AUTH_ENTRY_POINT = "/ws/**";
    protected static final String[] SWAGGER_ENTRY_POINTS = new String[]{
            "/webjars/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/swagger.json",
            "/configuration/ui",
            "/configuration/security"};

    @Autowired
    private QbsApiEntryPointProperties qbsApiEntryPointProperties;
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

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    private SkipPathRequestMatcher buildSkipPathRequestMatcher() {
        List<String> pathsToSkip = Lists.newArrayList(SWAGGER_ENTRY_POINTS);
        pathsToSkip.addAll(Lists.newArrayList(WS_TOKEN_BASED_AUTH_ENTRY_POINT,
                qbsApiEntryPointProperties.getAuth().getUsernameLogin(),
                qbsApiEntryPointProperties.getAuth().getImageLogin(),
                qbsApiEntryPointProperties.getAuth().getMobileLogin()
                ));
        pathsToSkip.addAll(qbsApiEntryPointProperties.getIgnored());
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, qbsApiEntryPointProperties.getAuth().getTokenProtect());
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
        RestLoginProcessingFilter filter = new RestLoginProcessingFilter(qbsApiEntryPointProperties.getAuth().getUsernameLogin(), successHandler,
                failureHandler);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected RestMobileLoginProcessingFilter buildRestPublicLoginProcessingFilter() throws Exception {
        RestMobileLoginProcessingFilter filter = new RestMobileLoginProcessingFilter(qbsApiEntryPointProperties.getAuth().getMobileLogin(),
                successHandler, failureHandler);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected RestImageLoginProcessingFilter buildRestImageLoginProcessingFilter() throws Exception {
        RestImageLoginProcessingFilter filter = new RestImageLoginProcessingFilter(qbsApiEntryPointProperties.getAuth().getImageLogin(),
                successHandler, failureHandler);
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
                .and().authorizeRequests()
                .antMatchers(qbsApiEntryPointProperties.getAuth().getUsernameLogin()).permitAll()
                .antMatchers(qbsApiEntryPointProperties.getAuth().getMobileLogin()).permitAll()
                .antMatchers(qbsApiEntryPointProperties.getAuth().getImageLogin()).permitAll()
                .antMatchers(SWAGGER_ENTRY_POINTS).permitAll()
                .antMatchers(qbsApiEntryPointProperties.getIgnored().toArray(new String[0])).permitAll()
                .antMatchers(WS_TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
                .antMatchers(qbsApiEntryPointProperties.getAuth().getTokenProtect()).authenticated().anyRequest().authenticated().and().exceptionHandling()
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
