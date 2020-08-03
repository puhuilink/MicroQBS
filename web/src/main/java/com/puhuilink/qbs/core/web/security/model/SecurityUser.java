/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:11:55
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:11:55
 */
package com.puhuilink.qbs.core.web.security.model;

import com.puhuilink.qbs.core.base.constant.CommonConstant;
import com.puhuilink.qbs.core.web.entity.Permission;
import com.puhuilink.qbs.core.web.entity.Role;
import com.puhuilink.qbs.core.web.entity.User;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser extends User {

    private Collection<GrantedAuthority> authorities;
    private UserPrincipal userPrincipal;
    private Boolean saveLogin;

    public SecurityUser() {
        super();
    }

    public SecurityUser(User user, UserPrincipal userPrincipal) {
        super(user);
        this.userPrincipal = userPrincipal;
        this.saveLogin = userPrincipal.getSaveLogin();
    }

    public SecurityUser(User user) {
        super(user);
    }

    public Collection<GrantedAuthority> getAuthorities() {

        this.authorities = new ArrayList<>();
        List<Permission> permissions = this.getPermissions();
        // 添加请求权限
        if (permissions != null && permissions.size() > 0) {
            for (Permission permission : permissions) {
                if (CommonConstant.PERMISSION_OPERATION.equals(permission.getType())
                        && StringUtils.isNotBlank(permission.getTitle()) && StringUtils.isNotBlank(permission.getPath())) {

                    this.authorities.add(new SimpleGrantedAuthority(permission.getTitle()));
                }
            }
        }
        // 添加角色
        List<Role> roles = this.getRoles();
        if (roles != null && roles.size() > 0) {
            roles.forEach(item -> {
                if (StringUtils.isNotBlank(item.getName())) {
                    this.authorities.add(new SimpleGrantedAuthority(item.getName()));
                }
            });
        }
        return this.authorities;
    }

    public UserPrincipal getUserPrincipal() {
        return userPrincipal;
    }

    public void setUserPrincipal(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    /**
     * 账户是否过期
     *
     * @return
     */
    public boolean isAccountNonExpired() {

        return true;
    }

    /**
     * 是否禁用
     *
     * @return
     */
    public boolean isAccountNonLocked() {

        return CommonConstant.USER_STATUS_LOCK.equals(this.getStatus()) ? false : true;
    }

    /**
     * 密码是否过期
     *
     * @return
     */
    public boolean isCredentialsNonExpired() {

        return true;
    }

    /**
     * 是否启用
     *
     * @return
     */
    public boolean isEnabled() {

        return CommonConstant.USER_STATUS_NORMAL.equals(this.getStatus()) ? true : false;
    }
}
