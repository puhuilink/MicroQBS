package com.phlink.bus.api.common.service;

import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.domain.AlarmSchoolRules;
import com.phlink.bus.api.alarm.domain.CodeFence;
import com.phlink.bus.api.alarm.domain.CodeRule;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.fence.domain.Fence;
import com.phlink.bus.api.route.domain.vo.RouteBusVO;
import com.phlink.bus.api.system.domain.*;

import java.util.List;

public interface CacheService {

    /**
     * 测试 Redis是否连接成功
     */
    void testConnect() throws Exception;

    /**
     * 从缓存中获取用户
     *
     * @param username 用户名
     * @return User
     */
    User getUser(String username) throws Exception;

    /**
     * 从缓存中获取用户角色
     *
     * @param username 用户名
     * @return 角色集
     */
    List<Role> getRoles(String username) throws Exception;

    /**
     * 从缓存中获取用户权限
     *
     * @param username 用户名
     * @return 权限集
     */
    List<Menu> getPermissions(String username) throws Exception;

    /**
     * 从缓存中获取用户个性化配置
     *
     * @param userId 用户 ID
     * @return 个性化配置
     */
    UserConfig getUserConfig(String userId) throws Exception;

    /**
     * 缓存用户信息，只有当用户信息是查询出来的，完整的，才应该调用这个方法
     * 否则需要调用下面这个重载方法
     *
     * @param user 用户信息
     */
    void saveUser(User user) throws Exception;

    /**
     * 缓存用户信息
     *
     * @param username 用户名
     */
    void saveUser(String username) throws Exception;

    /**
     * 缓存用户角色信息
     *
     * @param username 用户名
     */
    void saveRoles(String username) throws Exception;

    /**
     * 缓存用户权限信息
     *
     * @param username 用户名
     */
    void savePermissions(String username) throws Exception;

    /**
     * 缓存用户个性化配置
     *
     * @param userId 用户 ID
     */
    void saveUserConfigs(String userId) throws Exception;

    /**
     * 删除用户信息
     *
     * @param username 用户名
     */
    void deleteUser(String username) throws Exception;

    /**
     * 删除用户角色信息
     *
     * @param username 用户名
     */
    void deleteRoles(String username) throws Exception;

    /**
     * 删除用户权限信息
     *
     * @param username 用户名
     */
    void deletePermissions(String username) throws Exception;

    /**
     * 删除用户个性化配置
     *
     * @param userId 用户 ID
     */
    void deleteUserConfigs(String userId) throws Exception;

    /**
     * 保存验证码
     *
     * @param random
     */
    void saveCaptcha(String username, String random) throws Exception;

    /**
     * 获得验证码
     *
     * @param username
     * @return
     * @throws Exception
     */
    String getCaptcha(String username) throws Exception;

    /**
     * 删除验证码
     *
     * @param mobile
     */
    void deleteCaptcha(String mobile) throws Exception;

    /**
     * 保存系统配置
     *
     * @param systemConfig
     * @throws Exception
     */
    void saveSystemConfig(SystemConfig systemConfig) throws Exception;

    /**
     * 保存路线告警规则
     *
     * @param alarmRouteRulesList
     * @throws Exception
     */
    void saveAlarmRouteRules(List<AlarmRouteRules> alarmRouteRulesList) throws Exception;

    /**
     * 保存学校告警规则
     *
     * @param alarmSchoolRulesList
     * @throws Exception
     */
    void saveAlarmSchoolRules(List<AlarmSchoolRules> alarmSchoolRulesList) throws Exception;

    /**
     * 保存围栏与设备关系
     *
     * @param fence
     * @throws Exception
     */
    void saveFence(Fence fence) throws Exception;

    /**
     * 保存车辆地图id
     *
     * @param busList
     * @throws Exception
     */
    void saveBus(List<Bus> busList) throws Exception;

    /**
     * 保存手环地图id
     *
     * @param deviceList
     * @throws Exception
     */
    void saveDevice(List<Device> deviceList) throws Exception;

    /**
     * 获得一个系统配置属性的值
     *
     * @param key
     * @return
     * @throws Exception
     */
    String getSystemConfig(String key) throws Exception;

    /**
     * 唯一登录，记录token
     *
     * @param username
     * @param token
     */
    void saveAppLoginToken(String username, String token, Long exipreTime) throws RedisConnectException;

    String getAppLoginToken(String username) throws RedisConnectException;

    /**
     * 设备、车辆对应规则id
     *
     * @param list
     * @throws Exception
     */
    void loadBusAndDeviceAlarmRules(List<CodeRule> list) throws Exception;

    /**
     * 车辆对应路线围栏id
     *
     * @param list
     * @throws Exception
     */
    void saveBusRouteFence(List<CodeFence> list) throws Exception;

    /**
     * 车辆对应站点围栏id
     *
     * @param list
     * @throws Exception
     */
    void saveBusStopFence(List<CodeFence> list) throws Exception;

    /**
     * 手环对应学校围栏id
     *
     * @param list
     * @throws Exception
     */
    void saveSchoolFence(List<CodeFence> list) throws Exception;

    /**
     * 车辆路线关系
     *
     * @param list
     * @throws Exception
     */
    void saveRouteBus(List<RouteBusVO> list) throws Exception;
}
