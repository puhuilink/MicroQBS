package com.phlink.bus.api.im.service;

import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.response.CommonResultEntity;
import com.phlink.bus.api.im.response.LoginResultEntity;
import com.phlink.bus.api.system.domain.Dept;
import com.phlink.bus.api.system.domain.User;

public interface IImService {

    /**
     * 登录到im服务器，保存cookie
     * @throws RedisConnectException
     */
    LoginResultEntity loginToImServer() throws RedisConnectException;

    /**
     * 创建部门
     * @param dept
     * @throws RedisConnectException
     */
    CommonResultEntity createDept(Dept dept, Boolean withGroup, String companyId) throws RedisConnectException;

    /**
     * 更新部门
     * @param dept
     * @param withGroup
     * @throws RedisConnectException
     */
    CommonResultEntity updateDept(Dept dept, Boolean withGroup) throws RedisConnectException;

    /**
     * 删除部门
     * @param deptId
     * @throws RedisConnectException
     */
    CommonResultEntity deleteDept(String deptId) throws RedisConnectException;

    /**
     * 增加员工
     * @param user
     */
    CommonResultEntity addStaff(User user) throws RedisConnectException;

    /**
     * 更新员工
     * @param user
     */
    CommonResultEntity updateStaff(User user) throws RedisConnectException;

    CommonResultEntity addGuardian(User user) throws RedisConnectException;

    CommonResultEntity updateGuardian(User user) throws RedisConnectException;

    /**
     * 删除员工
     * @param s
     * @throws RedisConnectException
     */
    CommonResultEntity deleteStaff(String s) throws RedisConnectException;

    /**
     * 创建群组
     * @param groups
     */
    CommonResultEntity addGroup(ImGroups groups) throws RedisConnectException;

    /**
     * 转移群主
     * @param groupId
     * @param newManagerId
     */
    CommonResultEntity transferGroupManager(String groupId, String newManagerId) throws RedisConnectException;

    /**
     * 删除群组
     * @param groupId
     */
    CommonResultEntity deleteGroup(String groupId) throws RedisConnectException;

    /**
     * 修改群名称
     * @param groupId
     * @param name
     */
    CommonResultEntity updateGroupName(String groupId, String name) throws RedisConnectException;

    /**
     * 邀请成员进群
     * @param groupId
     * @param ids
     * @return
     */
    CommonResultEntity invite(String groupId, Long[] ids) throws RedisConnectException;

    /**
     * 移除群成员
     * @param groupId
     * @param ids
     * @return
     */
    CommonResultEntity remove(String groupId, Long[] ids) throws RedisConnectException;

    CommonResultEntity updateCustomer(User user) throws RedisConnectException;

    /**
     * 将用户加入指定的部门
     * @param user
     * @param deptId
     * @return
     */
    CommonResultEntity addToDept(User user, Long deptId) throws RedisConnectException;

    void initImUser();

    void initImDept();
}
