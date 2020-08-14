/*
 * @Author: sevncz.wen
 * @Date: 2020-08-14 11:24
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-08-14 11:24
 */
package com.puhuilink.qbs.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.puhuilink.qbs.auth.security.model.TokenStatus;
import com.puhuilink.qbs.core.common.base.QbsBaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @program: qbs-web
 * @description:
 * @author: sevncz.wen
 * @create: 2020-08-14 11:24
 **/
@Data
@TableName("t_user_token")
@ApiModel(value = "用户TOKEN信息")
public class UserToken extends QbsBaseEntity {

    private String username;
    private String userId;
    private TokenStatus tokenStatus;
    private String token;

}
