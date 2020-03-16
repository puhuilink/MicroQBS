package com.phlink.core.config.security;

import com.phlink.core.security.UserDetailsServiceImpl;
import com.phlink.core.security.jwt.AuthenticationFailHandler;
import com.phlink.core.security.jwt.AuthenticationSuccessHandler;
import com.phlink.core.security.validator.sms.SmsAuthenticationFilter;
import com.phlink.core.security.validator.sms.SmsAuthenticationProvider;
import com.phlink.core.security.validator.username.UsernameAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class UsernameAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailHandler failHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        UsernameAuthenticationFilter filter = new UsernameAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failHandler);
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        filter.setFilterProcessesUrl("/login/username");
        filter.setPostOnly(true);

        http.addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);

    }

}