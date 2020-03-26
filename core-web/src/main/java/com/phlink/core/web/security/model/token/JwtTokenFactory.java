package com.phlink.core.web.security.model.token;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.phlink.core.web.base.constant.SecurityConstant;
import com.phlink.core.web.base.vo.TokenUser;
import com.phlink.core.web.config.properties.PhlinkTokenProperties;
import com.phlink.core.web.entity.User;
import com.phlink.core.web.security.model.Authority;
import com.phlink.core.web.security.model.SecurityUser;
import com.phlink.core.web.security.model.UserPrincipal;
import com.phlink.core.web.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtTokenFactory {

    private static final String SCOPES = "scopes";
    private static final String USER_ID = "userId";
    private static final String ENABLED = "enabled";


    private final PhlinkTokenProperties tokenProperties;
    private final RedissonClient redissonClient;
    private final SecurityUtil securityUtil;

    @Autowired
    public JwtTokenFactory(PhlinkTokenProperties tokenProperties, RedissonClient redissonClient, SecurityUtil securityUtil) {
        this.tokenProperties = tokenProperties;
        this.redissonClient = redissonClient;
        this.securityUtil = securityUtil;
    }

    /**
     * Factory method for issuing new JWT Tokens.
     */
    public AccessJwtToken createAccessJwtToken(SecurityUser securityUser) {
        //用户选择保存登录状态几天
        Boolean saved = false;
        String token = securityUtil.getToken(securityUser.getUsername(), saved);
        return new AccessJwtToken(token);
    }

    public SecurityUser parseAccessJwtToken(RawAccessJwtToken rawAccessToken) {
        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(SecurityConstant.JWT_SIGN_KEY);
        Claims claims = jwsClaims.getBody();
        String subject = claims.getSubject();
        List<String> scopes = claims.get(SCOPES, List.class);
        if (scopes == null || scopes.isEmpty()) {
            throw new IllegalArgumentException("JWT Token doesn't have any scopes");
        }

        SecurityUser securityUser = new SecurityUser();
        securityUser.setUsername(subject);
        securityUser.setAuthority(Authority.parse(scopes.get(0)));
        securityUser.setEnabled(claims.get(ENABLED, Boolean.class));
        UserPrincipal principal = new UserPrincipal(UserPrincipal.Type.USER_NAME, subject);
        securityUser.setUserPrincipal(principal);
        return securityUser;
    }


}
