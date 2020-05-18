/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 10:25:23
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:11:01
 */
package com.phlink.core.web.security.auth.rest;

import com.phlink.core.web.entity.User;
import com.phlink.core.web.security.model.SecurityUser;
import com.phlink.core.web.security.model.UserPrincipal;
import com.phlink.core.web.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public RestAuthenticationProvider(final UserService userService, final BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserPrincipal)) {
            throw new BadCredentialsException("Authentication Failed. Bad user principal.");
        }

        UserPrincipal userPrincipal = (UserPrincipal) principal;
        if (userPrincipal.getType() == UserPrincipal.Type.USER_NAME) {
            String username = userPrincipal.getValue();
            String password = (String) authentication.getCredentials();
            return authenticateByUsernameAndPassword(authentication, userPrincipal, username, password);
        } else {
            String mobile = userPrincipal.getValue();
            return authenticateByMobile(authentication, userPrincipal, mobile);
        }
    }

    private Authentication authenticateByUsernameAndPassword(Authentication authentication, UserPrincipal userPrincipal,
            String username, String password) {
        User user = userService.getByUsername(username);
        if (user == null) {
            log.info("用户{}不存在", username);
            throw new UsernameNotFoundException("用户不存在");
        }

        try {
            if (!encoder.matches(password, user.getPassword())) {
                log.info("用户名密码错误{}", username);
                throw new BadCredentialsException("用户名密码错误");
            }
            if (user.getAuthority() == null) {
                log.info("您没有权限进入系统{}", username);
                throw new InsufficientAuthenticationException("您没有权限进入系统");
            }

            SecurityUser securityUser = new SecurityUser(user, userPrincipal);
            return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
        } catch (Exception e) {
            throw e;
        }
    }

    private Authentication authenticateByMobile(Authentication authentication, UserPrincipal userPrincipal,
            String mobile) {
        User user = userService.getByMobile(mobile);
        if (user == null) {
            log.info("用户{}不存在", mobile);
            throw new UsernameNotFoundException("手机号不存在");
        }
        SecurityUser securityUser = new SecurityUser(user, userPrincipal);

        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
