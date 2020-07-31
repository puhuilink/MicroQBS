/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:12:48
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:12:48
 */
package com.puhuilink.qbs.core.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.puhuilink.qbs.core.base.constant.CommonConstant;
import com.puhuilink.qbs.core.web.entity.Permission;
import com.puhuilink.qbs.core.web.entity.Role;
import com.puhuilink.qbs.core.web.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import cn.hutool.core.util.StrUtil;

public class SecurityUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 1L;

    public SecurityUserDetails(User user) {
        if (user != null) {
            this.setUsername(user.getUsername());
            this.setMobile(user.getMobile());
            this.setEmail(user.getEmail());
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());
            this.setRoles(user.getRoles());
            this.setPermissions(user.getPermissions());
        }
    }

    /**
     * 添加用户拥有的权限和角色
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorityList = new ArrayList<>();
        List<Permission> permissions = this.getPermissions();
        // 添加请求权限
        if (permissions != null && permissions.size() > 0) {
            for (Permission permission : permissions) {
                if (CommonConstant.PERMISSION_OPERATION.equals(permission.getType())
                        && StrUtil.isNotBlank(permission.getTitle()) && StrUtil.isNotBlank(permission.getPath())) {

                    authorityList.add(new SimpleGrantedAuthority(permission.getTitle()));
                }
            }
        }
        // 添加角色
        List<Role> roles = this.getRoles();
        if (roles != null && roles.size() > 0) {
            roles.forEach(item -> {
                if (StrUtil.isNotBlank(item.getName())) {
                    authorityList.add(new SimpleGrantedAuthority(item.getName()));
                }
            });
        }
        return authorityList;
    }

    /**
     * 账户是否过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    /**
     * 是否禁用
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {

        return CommonConstant.USER_STATUS_LOCK.equals(this.getStatus()) ? false : true;
    }

    /**
     * 密码是否过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    /**
     * 是否启用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {

        return CommonConstant.USER_STATUS_NORMAL.equals(this.getStatus()) ? true : false;
    }
}
