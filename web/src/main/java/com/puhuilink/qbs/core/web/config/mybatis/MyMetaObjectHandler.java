/*
 * @Author: sevncz.wen
 * @Date: 2020-03-24 10:35:07
 * @LastEditors: sevncz.wen
 * @LastEditTime: 2020-05-19 09:18:07
 * @FilePath: /phlink-common-framework/core/web/src/main/java/com/phlink/core/web/config/mybatis/MyMetaObjectHandler.java
 */
package com.puhuilink.qbs.core.web.config.mybatis;

import java.util.Date;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.puhuilink.qbs.core.web.security.model.SecurityUser;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = getUsername(authentication);
            this.setFieldValByName("createBy", username, metaObject);
        }
        this.setFieldValByName("createTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = getUsername(authentication);
            this.setFieldValByName("updateBy", username, metaObject);
        }
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    private String getUsername(Authentication authentication) {
        String username = "";
        if (authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            username = securityUser.getUsername();
        }
        if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }
        return username;
    }
}
