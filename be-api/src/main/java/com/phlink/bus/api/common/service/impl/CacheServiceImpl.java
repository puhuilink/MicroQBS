package com.phlink.bus.api.common.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phlink.bus.api.alarm.domain.AlarmRouteRules;
import com.phlink.bus.api.alarm.domain.AlarmSchoolRules;
import com.phlink.bus.api.alarm.domain.CodeFence;
import com.phlink.bus.api.alarm.domain.CodeRule;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.service.CacheService;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.fence.domain.Fence;
import com.phlink.bus.api.route.domain.vo.RouteBusVO;
import com.phlink.bus.api.system.dao.UserMapper;
import com.phlink.bus.api.system.domain.*;
import com.phlink.bus.api.system.service.MenuService;
import com.phlink.bus.api.system.service.RoleService;
import com.phlink.bus.api.system.service.UserConfigService;
import com.phlink.bus.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void testConnect() throws Exception {
        this.redisService.exists("test");
    }

    @Override
    public User getUser(String username) throws Exception {
        String userString = this.redisService.get(BusApiConstant.USER_CACHE_PREFIX + username);
        if (StringUtils.isBlank(userString)) {
            throw new BusApiException();
        }
        return this.mapper.readValue(userString, User.class);
    }

    @Override
    public List<Role> getRoles(String username) throws Exception {
        String roleListString = this.redisService.get(BusApiConstant.USER_ROLE_CACHE_PREFIX + username);
        if (StringUtils.isBlank(roleListString)) {
            throw new BusApiException();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Role.class);
            return this.mapper.readValue(roleListString, type);
        }
    }

    @Override
    public List<Menu> getPermissions(String username) throws Exception {
        String permissionListString = this.redisService.get(BusApiConstant.USER_PERMISSION_CACHE_PREFIX + username);
        if (StringUtils.isBlank(permissionListString)) {
            throw new BusApiException();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Menu.class);
            return this.mapper.readValue(permissionListString, type);
        }
    }

    @Override
    public UserConfig getUserConfig(String userId) throws Exception {
        String userConfigString = this.redisService.get(BusApiConstant.USER_CONFIG_CACHE_PREFIX + userId);
        if (StringUtils.isBlank(userConfigString)) {
            throw new BusApiException("用户配置信息未找到");
        }
        return this.mapper.readValue(userConfigString, UserConfig.class);
    }

    @Override
    public void saveUser(User user) throws Exception {
        String username = user.getUsername();
        this.deleteUser(username);
        redisService.set(BusApiConstant.USER_CACHE_PREFIX + username, mapper.writeValueAsString(user));
    }

    @Override
    public void saveUser(String username) throws Exception {
        User user = userMapper.findDetail(username);
        this.deleteUser(username);
        redisService.set(BusApiConstant.USER_CACHE_PREFIX + username, mapper.writeValueAsString(user));
    }

    @Override
    public void saveRoles(String username) throws Exception {
        List<Role> roleList = this.roleService.findUserRole(username);
        if (!roleList.isEmpty()) {
            this.deleteRoles(username);
            redisService.set(BusApiConstant.USER_ROLE_CACHE_PREFIX + username, mapper.writeValueAsString(roleList));
        }

    }

    @Override
    public void savePermissions(String username) throws Exception {
        List<Menu> permissionList = this.menuService.findUserPermissions(username);
        if (!permissionList.isEmpty()) {
            this.deletePermissions(username);
            redisService.set(BusApiConstant.USER_PERMISSION_CACHE_PREFIX + username, mapper.writeValueAsString(permissionList));
        }
    }

    @Override
    public void saveUserConfigs(String userId) throws Exception {
        UserConfig userConfig = this.userConfigService.findByUserId(userId);
        if (userConfig != null) {
            this.deleteUserConfigs(userId);
            redisService.set(BusApiConstant.USER_CONFIG_CACHE_PREFIX + userId, mapper.writeValueAsString(userConfig));
        }
    }

    @Override
    public void deleteUser(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(BusApiConstant.USER_CACHE_PREFIX + username);
    }

    @Override
    public void deleteRoles(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(BusApiConstant.USER_ROLE_CACHE_PREFIX + username);
    }

    @Override
    public void deletePermissions(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(BusApiConstant.USER_PERMISSION_CACHE_PREFIX + username);
    }

    @Override
    public void deleteUserConfigs(String userId) throws Exception {
        redisService.del(BusApiConstant.USER_CONFIG_CACHE_PREFIX + userId);
    }

    @Override
    public void saveCaptcha(String mobile, String random) throws Exception {
        String expire = getSystemConfig(Constants.CAPTCHA_EXPIRE_CONFIG);
        int expireMinu = Integer.parseInt(expire);
        redisService.set(Constants.CAPTCHA_CACHE_PREFIX + mobile, random, expireMinu * 60 * 1000L);
    }

    @Override
    public String getCaptcha(String mobile) throws Exception {
        return redisService.get(Constants.CAPTCHA_CACHE_PREFIX + mobile);
    }

    @Override
    public void deleteCaptcha(String mobile) throws Exception {
        redisService.del(Constants.CAPTCHA_CACHE_PREFIX + mobile);
    }

    @Override
    public void saveSystemConfig(SystemConfig systemConfig) throws Exception {
        redisService.hset(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, systemConfig.getKey(), systemConfig.getValue());
    }

    @Override
    public void saveAlarmRouteRules(List<AlarmRouteRules> alarmRouteRulesList) throws Exception {
        HashMap<String, String> routeMap = new HashMap<>();
        HashMap<String, String> stopMap = new HashMap<>();
        HashMap<String, String> busMap = new HashMap<>();
        HashMap<String, String> weekendMap = new HashMap<>();
        HashMap<String, String> invalidMap = new HashMap<>();
        String key;
        for (AlarmRouteRules alarmRouteRules : alarmRouteRulesList) {
            key = String.valueOf(alarmRouteRules.getId());
            routeMap.put(key, String.valueOf(alarmRouteRules.getRouteSwitch()));
            stopMap.put(key, String.valueOf(alarmRouteRules.getStopSwitch()));
            busMap.put(key, String.valueOf(alarmRouteRules.getBusSwitch()));
            weekendMap.put(key, String.valueOf(alarmRouteRules.getWeekendSwitch()));
            if(alarmRouteRules.getInvalidStartDate()  == null || alarmRouteRules.getInvalidEndDate() == null) {
                invalidMap.put(key, DateUtil.localDateToString(alarmRouteRules.getInvalidStartDate()) + "," + DateUtil.localDateToString(alarmRouteRules.getInvalidEndDate()));
            }
        }
        redisService.batchHset(BusApiConstant.ROUTE_SWITCH, routeMap);
        redisService.batchHset(BusApiConstant.STOP_SWITCH, stopMap);
        redisService.batchHset(BusApiConstant.BUS_SWITCH, busMap);
        redisService.batchHset(BusApiConstant.ROUTE_WEEKEND_SWITCH, weekendMap);
        redisService.batchHset(BusApiConstant.ROUTE_INVALID_DATE, invalidMap);
    }

    @Override
    public void saveAlarmSchoolRules(List<AlarmSchoolRules> alarmSchoolRulesList) throws Exception {
        HashMap<String, String> deviceMap = new HashMap<>();
        HashMap<String, String> weekendMap = new HashMap<>();
        String key;
        for (AlarmSchoolRules alarmSchoolRules : alarmSchoolRulesList) {
            key = String.valueOf(alarmSchoolRules.getId());
            deviceMap.put(key, String.valueOf(alarmSchoolRules.getDeviceSwitch()));
            weekendMap.put(key, String.valueOf(alarmSchoolRules.getWeekendSwitch()));
        }
        redisService.batchHset(BusApiConstant.DEVICE_SWITCH, deviceMap);
        redisService.batchHset(BusApiConstant.SCHOOL_WEEKEND_SWITCH, weekendMap);
    }

    @Override
    public void saveFence(Fence fence) throws Exception {
        if(fence == null) {
            log.warn("围栏为空，无法缓存");
            return;
        }
        if(StringUtils.isBlank(fence.getFenceId())) {
            log.warn("围栏ID为空，无法缓存");
            return;
        }
        if(fence.getJobs() == null) {
            log.warn("围栏Jobs为空，无法缓存");
            return;
        }
        redisService.hset(BusApiConstant.ROUTE_FENCE, fence.getFenceId(), fence.getJobs().toString());
    }

    @Override
    public void saveBus(List<Bus> busList) throws Exception {
        HashMap<String, String> amaphash = new HashMap<>();
        for (Bus bus : busList) {
            amaphash.put(bus.getBusCode(), String.valueOf(bus.getTid()));
        }
        this.redisService.batchHset(BusApiConstant.BUS, amaphash);
    }

    @Override
    public void saveBusRouteFence(List<CodeFence> busCodeList) throws Exception {
        Map<String, String> map = busCodeList.stream().collect(Collectors.toMap(CodeFence::getCode, CodeFence::getFenceId));
        this.redisService.batchHset(BusApiConstant.BUS_ROUTE_FENCE, map);
    }

    @Override
    public void saveBusStopFence(List<CodeFence> busCodeList) throws Exception {
        Map<String, String> map = busCodeList.stream().collect(Collectors.toMap(CodeFence::getCode, CodeFence::getFenceId));
        this.redisService.batchHset(BusApiConstant.BUS_STOP_FENCE, map);
    }

    @Override
    public void saveDevice(List<Device> deviceList) throws Exception {
        HashMap<String, String> amaphash = new HashMap<>();
        for (Device device : deviceList) {
            amaphash.put(device.getDeviceCode(), String.valueOf(device.getTid()));
        }
        this.redisService.batchHset(BusApiConstant.DEVICE, amaphash);
    }

    @Override
    public String getSystemConfig(String key) throws Exception {
        return redisService.hget(BusApiConstant.SYSTEM_CONFIG_HASH_PREFIX, key);
    }

    @Override
    public void saveAppLoginToken(String username, String jwtToken, Long exipreTime) throws RedisConnectException {
        redisService.set(BusApiConstant.ONLY_LOGIN_TOKEN_CACHE_PREFIX + username, jwtToken, exipreTime);
    }

    @Override
    public String getAppLoginToken(String username) throws RedisConnectException {
        return redisService.get(BusApiConstant.ONLY_LOGIN_TOKEN_CACHE_PREFIX + username);
    }

    @Override
    public void loadBusAndDeviceAlarmRules(List<CodeRule> list) throws Exception {
        try {
            Map<String, String> map = list.stream().filter(codeRule -> codeRule.getId() != null).collect(Collectors.toMap(CodeRule::getCode, CodeRule::getId));
            redisService.batchHset(BusApiConstant.CODE_RULE, map);
        } catch (Exception e) {
            log.error("loadBusAndDeviceAlarmRules error", e);
        }
    }

    @Override
    public void saveSchoolFence(List<CodeFence> schoolFenceList) throws Exception {
        try {
            Map<String, String> map = schoolFenceList.stream().distinct().collect(Collectors.toMap(CodeFence::getCode, CodeFence::getFenceId));
            redisService.batchHset(BusApiConstant.DEVICE_SCHOOL_FENCE, map);
        } catch (Exception e) {
            log.error("saveSchoolFence error", e);
        }
    }

    @Override
    public void saveRouteBus(List<RouteBusVO> routeBusVOList) throws Exception {
        try {
            Map<String, String> map = routeBusVOList.stream().filter(routeBusVO -> routeBusVO.getBusCode() != null).collect(Collectors.toMap(RouteBusVO::getBusCode, RouteBusVO::getRouteId));
            redisService.batchHset(BusApiConstant.ROUTE_BUS, map);
        } catch (Exception e) {
            log.error("saveSchoolFence error", e);
        }
    }
}
