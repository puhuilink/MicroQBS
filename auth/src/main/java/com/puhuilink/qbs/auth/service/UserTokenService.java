package com.puhuilink.qbs.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puhuilink.qbs.auth.entity.User;
import com.puhuilink.qbs.auth.entity.UserToken;
import com.puhuilink.qbs.auth.security.model.TokenStatus;
import com.puhuilink.qbs.auth.security.model.token.RawAccessJwtToken;

public interface UserTokenService extends IService<UserToken> {

    UserToken create(User u, String tokenEncode);

    UserToken logoutAndCreate(User u, String tokenEncode);

    UserToken getLoginStatusTokenByUserId(String userId, String token);

    void updateLoginStatusToReset(String[] userIds, TokenStatus reset);

    void updateLoginStatusToReset(String userId, TokenStatus reset);

    void logout(String id, String accessToken);
}
