package com.phlink.bus.api.system.service;

import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.im.response.CommonResultEntity;
import com.phlink.bus.api.system.domain.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface UserService extends IService<User> {

    /**
     * 通过用户名查找用户
     *
     * @param username username
     * @return user
     */
    User findByName(String username);

    User findByMobile(String mobile);

    /**
     * 查询用户详情，包括基本信息，用户角色，用户部门
     *
     * @param user user
     * @param queryRequest queryRequest
     * @return IPage
     */
    IPage<User> findUserDetail(User user, QueryRequest queryRequest);

    /**
     * 更新用户登录时间
     *
     * @param username username
     */
    void updateLoginTime(String username) throws Exception;

    /**
     * 新增用户
     *
     * @param user user
     */
    void createUser(User user) throws Exception;

    CommonResultEntity registerToImServer(User user);

    /**
     * 修改用户
     *
     * @param user user
     */
    void updateUser(User user) throws Exception;

    /**
     * 删除用户
     *
     * @param userIds 用户 id数组
     */
    void deleteUsers(String[] userIds) throws Exception;

    /**
     * 更新个人信息
     *
     * @param user 个人信息
     */
    void updateProfile(User user) throws Exception;

    /**
     * 更新用户头像
     *
     * @param username 用户名
     * @param avatar   用户头像
     */
    void updateAvatar(String username, String avatar) throws Exception;

    /**
     * 更新用户密码
     *
     * @param username 用户名
     * @param password 新密码
     */
    void updatePassword(String username, String password) throws Exception;

    /**
     * 注册用户
     *
     * @param username 用户名
     * @param password 密码
     */
    void registVisitor(String username, String password) throws Exception;


    /**
     * 游客注册
     *
     * @param mobile 手机号
     */
    User registVisitor(String mobile) throws Exception;

    /**
     * 重置密码
     *
     * @param usernames 用户集合
     */
    void resetPassword(String[] usernames) throws Exception;

    IPage<User> listGuardianDetail(User user, QueryRequest queryRequest);


    /**
     * 获得车队队长信息
     * @param busCode
     * @return
     */
    List<User> listBusLeaderByBus(String busCode);

    /**
     * 获取组织机构下的用户列表
     * @param deptId
     * @return
     */
    List<User> listByDept(Long deptId);

    /**
     * 修改手机号
     * @param mobile
     */
    void updateMobile(String mobile) throws Exception;

    User createGuardianUser(String mobile, String realname, String idcard) throws Exception;

    /**
     * 更新极光ID
     * @param username
     * @param registrationID
     */
    void updateJiguangId(String username, String registrationID);

    /**
     * 根据绑定的老师ID获取路线上的用户信息
     * @param busTeacherId
     * @return
     */
    List<User> listOnRouteByWorkerId(Long busTeacherId);

    /**
     * 根据车辆获取线路上的用户信息
     * @param busId
     * @return
     */
    List<User> listOnRouteByBusId(Long busId);

    List<User> listGuardianDetail(User user);

    IPage<User> listVisitorDetail(User user, QueryRequest queryRequest);

    List<User> listVisitorDetail(User user);

    /**
     * 根据条件查询员工信息
     * @param user
     * @return
     */
    List<User> listUserDetail(User user);

    /**
     * 根据绑定的车辆获取司机信息
     * @param busCode
     * @return
     */
    User getDriverByBusCode(String busCode);

    List<User> listGuardianByStudent(Long id);

    void updateRegistrationId(String registrationId, Long userId);

    void clearRegistrationId(String username);

    void batchImport(List<User> list);
}
