package com.phlink.core.web.security.model;

import cn.hutool.core.util.StrUtil;
import com.phlink.core.web.base.constant.CommonConstant;
import com.phlink.core.web.entity.Permission;
import com.phlink.core.web.entity.Role;
import com.phlink.core.web.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class SecurityUser extends User {

    private static final long serialVersionUID = -797397440703066079L;

    private Collection<GrantedAuthority> authorities;
    private boolean enabled;
    private UserPrincipal userPrincipal;

    public SecurityUser() {
        super();
    }

    public SecurityUser(User user, boolean enabled, UserPrincipal userPrincipal) {
        super(user);
        this.enabled = enabled;
        this.userPrincipal = userPrincipal;
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
                    && StrUtil.isNotBlank(permission.getTitle())
                    && StrUtil.isNotBlank(permission.getPath())) {

                    this.authorities.add(new SimpleGrantedAuthority(permission.getTitle()));
                }
            }
        }
        // 添加角色
        List<Role> roles = this.getRoles();
        if (roles != null && roles.size() > 0) {
            roles.forEach(item -> {
                if (StrUtil.isNotBlank(item.getName())) {
                    this.authorities.add(new SimpleGrantedAuthority(item.getName()));
                }
            });
        }
        return this.authorities;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UserPrincipal getUserPrincipal() {
        return userPrincipal;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setUserPrincipal(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

}
