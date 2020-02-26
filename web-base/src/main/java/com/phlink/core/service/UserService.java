package com.phlink.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.core.common.vo.SearchVo;
import com.phlink.core.entity.Role;
import com.phlink.core.entity.User;
import com.phlink.core.entity.UserRole;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@CacheConfig(cacheNames = "user")
public interface UserService extends IService<User> {

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    @Cacheable(key = "#username")
    User getByUsername(String username);

    /**
     * 通过手机获取用户
     * @param mobile
     * @return
     */
    User getByMobile(String mobile);

    /**
     * 通过邮件和状态获取用户
     * @param email
     * @return
     */
    User getByEmail(String email);

    /**
     * 多条件分页获取用户
     * @param user
     * @param searchVo
     * @param pageable
     * @return
     */
    IPage<User> listByCondition(User user, SearchVo searchVo);

    /**
     * 通过部门id获取
     * @param departmentId
     * @return
     */
    List<User> listByDepartmentId(String departmentId);

    /**
     * 通过用户名模糊搜索
     * @param username
     * @param status
     * @return
     */
    List<User> listByUsernameLikeAndStatus(String username, Integer status);
}