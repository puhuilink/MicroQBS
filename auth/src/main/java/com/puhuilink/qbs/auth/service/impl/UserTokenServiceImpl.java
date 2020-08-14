/*
 * @Author: sevncz.wen
 * @Date: 2020-05-06 14:53:00
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:13:46
 */
package com.puhuilink.qbs.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puhuilink.qbs.auth.entity.User;
import com.puhuilink.qbs.auth.entity.UserToken;
import com.puhuilink.qbs.auth.mapper.UserTokenMapper;
import com.puhuilink.qbs.auth.security.model.TokenStatus;
import com.puhuilink.qbs.auth.service.UserTokenService;
import com.puhuilink.qbs.core.common.utils.SnowFlakeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserTokenServiceImpl extends ServiceImpl<UserTokenMapper, UserToken> implements UserTokenService {

    @Override
    public UserToken create(User u, String tokenEncode) {
        UserToken userToken = new UserToken();
        userToken.setUsername(u.getUsername());
        userToken.setUserId(u.getId());
        userToken.setTokenStatus(TokenStatus.LOGIN);
        userToken.setToken(tokenEncode);
        save(userToken);
        return userToken;
    }

    @Override
    public UserToken logoutAndCreate(User u, String tokenEncode) {
        LambdaUpdateWrapper<UserToken> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserToken::getUserId, u.getId())
                .eq(UserToken::getTokenStatus, TokenStatus.LOGIN)
                .set(UserToken::getTokenStatus, TokenStatus.LOGOUT);
        this.update(updateWrapper);
        return create(u, tokenEncode);
    }

    @Override
    public UserToken getLoginStatusTokenByUserId(String userId, String token) {
        LambdaQueryWrapper<UserToken> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserToken::getUserId, userId)
                .eq(UserToken::getTokenStatus, TokenStatus.LOGIN);
        List<UserToken> userTokens = this.list(queryWrapper);
        Optional<UserToken> op = userTokens.stream().filter(t -> token.equals(t.getToken())).findAny();
        return op.orElse(null);
    }

    @Override
    public void updateLoginStatusToReset(String[] userIds, TokenStatus reset) {
        LambdaUpdateWrapper<UserToken> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(UserToken::getUserId, userIds)
                .eq(UserToken::getTokenStatus, TokenStatus.LOGIN)
                .set(UserToken::getTokenStatus, reset);
        this.update(updateWrapper);
    }

    @Override
    public void updateLoginStatusToReset(String userId, TokenStatus reset) {
        LambdaUpdateWrapper<UserToken> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserToken::getUserId, userId)
                .eq(UserToken::getTokenStatus, TokenStatus.LOGIN)
                .set(UserToken::getTokenStatus, reset);
        this.update(updateWrapper);

    }

    @Override
    public void logout(String id, String accessToken) {
        LambdaUpdateWrapper<UserToken> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserToken::getUserId, id)
                .eq(UserToken::getToken, accessToken)
                .set(UserToken::getTokenStatus, TokenStatus.LOGOUT);
        this.update(updateWrapper);
    }
}
