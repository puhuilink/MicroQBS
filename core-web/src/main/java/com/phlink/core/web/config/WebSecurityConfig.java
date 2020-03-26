package com.phlink.core.web.config;

import com.phlink.core.web.config.properties.IgnoredUrlsProperties;
import com.phlink.core.web.security.auth.rest.RestAuthenticationProvider;
import com.phlink.core.web.security.auth.rest.RestLoginProcessingFilter;
import com.phlink.core.web.security.auth.rest.RestMobileLoginProcessingFilter;
import com.phlink.core.web.security.auth.jwt.JwtAuthenticationProvider;
import com.phlink.core.web.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.phlink.core.web.security.auth.jwt.SkipPathRequestMatcher;
import com.phlink.core.web.security.auth.jwt.extractor.TokenExtractor;
import com.phlink.core.web.security.auth.jwt.RestAccessDeniedHandler;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Security 核心配置类
 * 开启注解控制权限至Controller
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String WEBJARS_ENTRY_POINT = "/webjars/**";
    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/admin/auth/login";
    public static final String MOBILE_LOGIN_ENTRY_POINT = "/admin/auth/login/mobile";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/admin/auth/token";
    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/**";
    public static final String WS_TOKEN_BASED_AUTH_ENTRY_POINT = "/api/ws/**";
    protected static final String[] NON_TOKEN_BASED_AUTH_ENTRY_POINTS = new String[]{"/index.html", "/static/**", "/api/noauth/**", "/webjars/**"};

    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    @Autowired
//    @Qualifier("jwtHeaderTokenExtractor")
    private TokenExtractor jwtHeaderTokenExtractor;
    @Autowired
//    @Qualifier("jwtQueryTokenExtractor")
    private TokenExtractor jwtQueryTokenExtractor;
    @Autowired
    private RestAuthenticationProvider restAuthenticationProvider;
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(restAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    protected RestLoginProcessingFilter buildRestLoginProcessingFilter() throws Exception {
        RestLoginProcessingFilter filter = new RestLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected RestMobileLoginProcessingFilter buildRestPublicLoginProcessingFilter() throws Exception {
        RestMobileLoginProcessingFilter filter = new RestMobileLoginProcessingFilter(MOBILE_LOGIN_ENTRY_POINT, successHandler, failureHandler);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
        List<String> pathsToSkip = new ArrayList(Arrays.asList(NON_TOKEN_BASED_AUTH_ENTRY_POINTS));
        pathsToSkip.addAll(Arrays.asList(WS_TOKEN_BASED_AUTH_ENTRY_POINT, TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT,
            MOBILE_LOGIN_ENTRY_POINT, WEBJARS_ENTRY_POINT));
        pathsToSkip.addAll(ignoredUrlsProperties.getUrls());
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
        JwtTokenAuthenticationProcessingFilter filter
            = new JwtTokenAuthenticationProcessingFilter(failureHandler, jwtHeaderTokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected JwtTokenAuthenticationProcessingFilter buildWsJwtTokenAuthenticationProcessingFilter() throws Exception {
        AntPathRequestMatcher matcher = new AntPathRequestMatcher(WS_TOKEN_BASED_AUTH_ENTRY_POINT);
        JwtTokenAuthenticationProcessingFilter filter
            = new JwtTokenAuthenticationProcessingFilter(failureHandler, jwtQueryTokenExtractor, matcher);
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
        http.headers().cacheControl().and().frameOptions().disable()
            .and()
            .cors()
            .and()
            .csrf().disable()
            .exceptionHandling()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(WEBJARS_ENTRY_POINT).permitAll()
            .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll()
            .antMatchers(MOBILE_LOGIN_ENTRY_POINT).permitAll()
            .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll()
            .antMatchers(NON_TOKEN_BASED_AUTH_ENTRY_POINTS).permitAll()
            .and()
            .authorizeRequests()
            .antMatchers(WS_TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
            .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
            .and()
            .exceptionHandling().accessDeniedHandler(restAccessDeniedHandler)
            .and()
            .addFilterBefore(buildRestLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildRestPublicLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildWsJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
    }


}
