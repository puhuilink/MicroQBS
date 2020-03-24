package com.phlink.core.web.security.validator.sms;

import cn.hutool.core.util.StrUtil;
import com.phlink.core.web.security.UserDetailsServiceImpl;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Data
public class SmsAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken) authentication;
        UserDetails userDetails = userDetailsService.loadUserByMobile((String) smsAuthenticationToken.getPrincipal());
        if (StrUtil.isBlank(userDetails.getUsername())) {
            throw new UsernameNotFoundException("用户信息不存在");
        }
        SmsAuthenticationToken authenticationToken = new SmsAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(smsAuthenticationToken.getDetails());
        return authenticationToken;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(SmsAuthenticationToken.class);
    }

}