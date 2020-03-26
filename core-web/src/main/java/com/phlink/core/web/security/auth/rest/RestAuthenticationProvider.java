package com.phlink.core.web.security.auth.rest;

import com.phlink.core.web.entity.User;
import com.phlink.core.web.security.model.SecurityUser;
import com.phlink.core.web.security.model.UserPrincipal;
import com.phlink.core.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
            return authenticateByMobile(userPrincipal, mobile);
        }
    }

    private Authentication authenticateByUsernameAndPassword(Authentication authentication, UserPrincipal userPrincipal, String username, String password) {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        try {
            if (!encoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("用户名密码错误");
            }
            if (user.getAuthority() == null) {
                throw new InsufficientAuthenticationException("您没有权限进入系统");
            }

            SecurityUser securityUser = new SecurityUser(user, true, userPrincipal);
            return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
        } catch (Exception e) {
            throw e;
        }
    }

    private Authentication authenticateByMobile(UserPrincipal userPrincipal, String mobile) {
        User user = userService.getByMobile(mobile);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + mobile);
        }
        SecurityUser securityUser = new SecurityUser(user, true, userPrincipal);

        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

//    private void logLoginAction(User user, Authentication authentication, ActionType actionType, Exception e) {
//        String clientAddress = "Unknown";
//        String browser = "Unknown";
//        String os = "Unknown";
//        String device = "Unknown";
//        if (authentication != null && authentication.getDetails() != null) {
//            if (authentication.getDetails() instanceof RestAuthenticationDetails) {
//                RestAuthenticationDetails details = (RestAuthenticationDetails)authentication.getDetails();
//                clientAddress = details.getClientAddress();
//                if (details.getUserAgent() != null) {
//                    Client userAgent = details.getUserAgent();
//                    if (userAgent.userAgent != null) {
//                        browser = userAgent.userAgent.family;
//                        if (userAgent.userAgent.major != null) {
//                            browser += " " + userAgent.userAgent.major;
//                            if (userAgent.userAgent.minor != null) {
//                                browser += "." + userAgent.userAgent.minor;
//                                if (userAgent.userAgent.patch != null) {
//                                    browser += "." + userAgent.userAgent.patch;
//                                }
//                            }
//                        }
//                    }
//                    if (userAgent.os != null) {
//                        os = userAgent.os.family;
//                        if (userAgent.os.major != null) {
//                            os += " " + userAgent.os.major;
//                            if (userAgent.os.minor != null) {
//                                os += "." + userAgent.os.minor;
//                                if (userAgent.os.patch != null) {
//                                    os += "." + userAgent.os.patch;
//                                    if (userAgent.os.patchMinor != null) {
//                                        os += "." + userAgent.os.patchMinor;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (userAgent.device != null) {
//                        device = userAgent.device.family;
//                    }
//                }
//            }
//        }
//        auditLogService.logEntityAction(
//                user.getTenantId(), user.getCustomerId(), user.getId(),
//                user.getName(), user.getId(), null, actionType, e, clientAddress, browser, os, device);
//    }
}
