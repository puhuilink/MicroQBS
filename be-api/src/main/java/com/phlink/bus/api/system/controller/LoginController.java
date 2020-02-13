package com.phlink.bus.api.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phlink.bus.api.common.annotation.Limit;
import com.phlink.bus.api.common.authentication.JWTToken;
import com.phlink.bus.api.common.authentication.JWTUtil;
import com.phlink.bus.api.common.domain.ActiveUser;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.properties.BusApiProperties;
import com.phlink.bus.api.common.service.CacheService;
import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.common.utils.*;
import com.phlink.bus.api.system.dao.LoginLogMapper;
import com.phlink.bus.api.system.domain.Dept;
import com.phlink.bus.api.system.domain.LoginLog;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.domain.UserConfig;
import com.phlink.bus.api.system.domain.VO.LoginAppMobileVO;
import com.phlink.bus.api.system.domain.VO.LoginAppVO;
import com.phlink.bus.api.system.domain.VO.LoginMobileVO;
import com.phlink.bus.api.system.domain.VO.LoginVO;
import com.phlink.bus.api.system.manager.UserManager;
import com.phlink.bus.api.system.service.DeptService;
import com.phlink.bus.api.system.service.LoginLogService;
import com.phlink.bus.api.system.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Validated
@RestController
@Api(tags = "登录")
public class LoginController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private LoginLogMapper loginLogMapper;
    @Autowired
    private BusApiProperties properties;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private MqService mqService;
    @Autowired
    private DeptService deptService;

    @PostMapping("/login")
    @Limit(key = "login", period = 60, count = 20, name = "密码登录接口", prefix = "limit")
    public BusApiResponse login(@RequestBody @Valid LoginVO loginVO, HttpServletRequest request) throws Exception {
//        String username = StringUtils.lowerCase(loginVO.getUsername());
        String username = loginVO.getUsername();

        final String errorMessage = "用户名或密码错误";
        User user = this.userService.findByName(username);

        if (user == null) {
            user = this.userService.findByMobile(username);
            if(user == null) {
                throw new BusApiException(errorMessage);
            }
        }
        // 统一替换成username
        username = user.getUsername();
        String password = MD5Util.encrypt(username, loginVO.getPassword());
        if (!StringUtils.equals(user.getPassword(), password)) {
            throw new BusApiException(errorMessage);
        }
        if (User.STATUS_LOCK.equals(user.getStatus())) {
            throw new BusApiException("账号已被锁定,请联系管理员！");
        }
        // 更新用户登录时间
        this.userService.updateLoginTime(username);
        // 保存登录记录
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setLoginWay(LoginLog.LOGIN_PASSWORD);
        this.loginLogService.saveLoginLog(loginLog);

        String token = BusApiUtil.encryptToken(JWTUtil.sign(username, password));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getJwtTimeOut());
        String expireTimeStr = DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);

        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        return new BusApiResponse().message("认证成功").data(userInfo);
    }

    @PostMapping("/login-app")
    @Limit(key = "login", period = 60, count = 20, name = "登录接口", prefix = "limit")
    public BusApiResponse appLogin(@RequestBody @Valid LoginAppVO loginVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String username = StringUtils.lowerCase(loginVO.getUsername());
        String username = loginVO.getUsername();

        final String errorMessage = "用户名或密码错误";
        User user = this.userService.findByName(username);

        if (user == null) {
            user = this.userService.findByMobile(username);
            if(user == null) {
                throw new BusApiException(errorMessage);
            }
        }
        // 统一替换成username
        username = user.getUsername();
        String password = MD5Util.encrypt(username, loginVO.getPassword());
        if (!StringUtils.equals(user.getPassword(), password)) {
            throw new BusApiException(errorMessage);
        }

        if (User.STATUS_LOCK.equals(user.getStatus())) {
            throw new BusApiException("账号已被锁定,请联系管理员！");
        }

        // 更新用户登录时间
        this.userService.updateLoginTime(username);

        this.userService.updateJiguangId(username, loginVO.getRegistrationID());
        // 保存登录记录
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setLoginWay(LoginLog.LOGIN_PASSWORD);
        this.loginLogService.saveLoginLog(loginLog);

        String token = BusApiUtil.encryptToken(JWTUtil.sign(username, password));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getAppJwtTimeOut());
        String expireTimeStr = DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        String uniq = saveAppLogin(response, user, jwtToken);

        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);

        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        userInfo.put(BusApiConstant.UNIQUE_LOGIN, uniq);
        return new BusApiResponse().message("认证成功").data(userInfo);
    }

    private String saveAppLogin(HttpServletResponse response, User user, JWTToken jwtToken) throws RedisConnectException {
        String onlyToken = String.valueOf(System.currentTimeMillis());
        cacheService.saveAppLoginToken(user.getUsername(), onlyToken, Long.valueOf(jwtToken.getExipreAt()));
        response.setHeader(BusApiConstant.UNIQUE_LOGIN, onlyToken);
        return onlyToken;
    }

    @GetMapping("/login-captcha/{mobile}")
    @Limit(key = "login-captcha", period = 60, count = 10, name = "发送验证码", prefix = "limit")
    public BusApiResponse sendLoginCaptcha(@NotBlank(message = "{required}") @PathVariable String mobile) throws Exception {
        // 生成验证码
        String random = BusApiUtil.getRandom();
        // 缓存验证码
        this.cacheService.saveCaptcha(mobile, random);
        // 异步发送验证码
        mqService.sendCaptcha(mobile);
        return new BusApiResponse().message("发送成功").data(random);
    }


    @PostMapping("/login-mobile")
    @Limit(key = "login-mobile", period = 60, count = 10, name = "手机验证码登录", prefix = "limit")
    public BusApiResponse mobileLogin(
            @Valid @RequestBody LoginMobileVO loginMobileVO, HttpServletRequest request) throws Exception {
        String mobile = loginMobileVO.getMobile();
        String captcha = loginMobileVO.getCaptcha();

        User user = this.userService.findByMobile(mobile);

        if (user == null) {
            // 注册
            // 未在后台找到对应的用户，则该用户是游客
            user = this.userService.registVisitor(mobile);
        }
        if (User.STATUS_LOCK.equals(user.getStatus())) {
            throw new BusApiException("账号已被锁定,请联系管理员！");
        }
        String checkCaptch = this.cacheService.getCaptcha(mobile);
        if(!captcha.equals(checkCaptch)) {
            throw new BusApiException("验证码错误！");
        }

        // 更新用户登录时间
        this.userService.updateLoginTime(user.getUsername());
        // 保存登录记录
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(user.getUsername());
        loginLog.setLoginWay(LoginLog.LOGIN_MOBILE);
        this.loginLogService.saveLoginLog(loginLog);

        String token = BusApiUtil.encryptToken(JWTUtil.sign(user.getUsername(), captcha));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getJwtTimeOut());
        String expireTimeStr = DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);

        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        // 登录成功，删除验证码
        this.cacheService.deleteCaptcha(mobile);
        return new BusApiResponse().message("认证成功").data(userInfo);
    }



    @PostMapping("/login-app-mobile")
    @Limit(key = "login-mobile", period = 60, count = 10, name = "手机验证码登录", prefix = "limit")
    public BusApiResponse appMobileLogin(
            @Valid @RequestBody LoginAppMobileVO loginMobileVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String mobile = loginMobileVO.getMobile();
        String captcha = loginMobileVO.getCaptcha();

        User user = this.userService.findByMobile(mobile);

        if (user == null) {
            // 注册
            // 未在后台找到对应的用户，则该用户是游客
            user = this.userService.registVisitor(mobile);
        }
        if (User.STATUS_LOCK.equals(user.getStatus())) {
            throw new BusApiException("账号已被锁定,请联系管理员！");
        }
        String checkCaptch = this.cacheService.getCaptcha(mobile);
        if(!captcha.equals(checkCaptch)) {
            throw new BusApiException("验证码错误！");
        }

        // 更新用户登录时间
        this.userService.updateLoginTime(user.getUsername());

        this.userService.updateJiguangId(user.getUsername(), loginMobileVO.getRegistrationID());

        // 保存登录记录
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(user.getUsername());
        loginLog.setLoginWay(LoginLog.LOGIN_MOBILE);
        this.loginLogService.saveLoginLog(loginLog);

        String token = BusApiUtil.encryptToken(JWTUtil.sign(user.getUsername(), captcha));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getAppJwtTimeOut());
        String expireTimeStr = DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);

        String uniq = saveAppLogin(response, user, jwtToken);

        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        userInfo.put(BusApiConstant.UNIQUE_LOGIN, uniq);
        // 登录成功，删除验证码
        this.cacheService.deleteCaptcha(mobile);
        return new BusApiResponse().message("认证成功").data(userInfo);
    }

    @GetMapping("index/{username}")
    public BusApiResponse index(@NotBlank(message = "{required}") @PathVariable String username) {
        Map<String, Object> data = new HashMap<>();
        // 获取系统访问记录
        Long totalVisitCount = loginLogMapper.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = loginLogMapper.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = loginLogMapper.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastSevenVisitCount = loginLogMapper.findLastSevenDaysVisitCount(null);
        data.put("lastSevenVisitCount", lastSevenVisitCount);
        User param = new User();
        param.setUsername(username);
        List<Map<String, Object>> lastSevenUserVisitCount = loginLogMapper.findLastSevenDaysVisitCount(param);
        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return new BusApiResponse().data(data);
    }

    //@RequiresPermissions("user:online")
    @GetMapping("online")
    public BusApiResponse userOnline(String username) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(BusApiConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
        List<ActiveUser> activeUsers = new ArrayList<>();
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            activeUser.setToken(null);
            if (StringUtils.isNotBlank(username)) {
                if (StringUtils.equalsIgnoreCase(username, activeUser.getUsername())){
                    activeUsers.add(activeUser);
                }
            } else {
                activeUsers.add(activeUser);
            }
        }
        return new BusApiResponse().data(activeUsers);
    }

    @DeleteMapping("kickout/{id}")
    //@RequiresPermissions("user:kickout")
    public void kickout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(BusApiConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
        ActiveUser kickoutUser = null;
        String kickoutUserString = "";
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            if (StringUtils.equals(activeUser.getId(), id)) {
                kickoutUser = activeUser;
                kickoutUserString = userOnlineString;
            }
        }
        if (kickoutUser != null && StringUtils.isNotBlank(kickoutUserString)) {
            // 删除 zset中的记录
            redisService.zrem(BusApiConstant.ACTIVE_USERS_ZSET_PREFIX, kickoutUserString);
            // 删除对应的 token缓存
            redisService.del(BusApiConstant.TOKEN_CACHE_PREFIX + kickoutUser.getToken() + "." + kickoutUser.getIp());
//            String username = BusApiUtil.getCurrentUser().getUsername();
//            userService.clearRegistrationId(kickoutUser.getUsername());
        }
        try {
            // 删除registrationId
            String username = BusApiUtil.getCurrentUser().getUsername();
            userService.clearRegistrationId(username);
        } catch (Exception e) {

        }
    }

    @GetMapping("/logout/{id}")
    public void logout(@PathVariable String id) throws Exception {
        log.info("[logout] {} 退出登录", id);
        this.kickout(id);
    }

//    @PostMapping("regist")
//    public void regist(
//            @NotBlank(message = "{required}") String username,
//            @NotBlank(message = "{required}") String password) throws Exception {
//        this.userService.registVisitor(username, password);
//    }

    private String saveTokenToRedis(User user, JWTToken token, HttpServletRequest request) throws Exception {
        String ip = IPUtil.getIpAddr(request);

        // 构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUsername());
        activeUser.setIp(ip);
        activeUser.setToken(token.getToken());
        activeUser.setLoginAddress(AddressUtil.getCityInfo(ip));

        // zset 存储登录用户，score 为过期时间戳
        this.redisService.zadd(BusApiConstant.ACTIVE_USERS_ZSET_PREFIX, Double.valueOf(token.getExipreAt()), mapper.writeValueAsString(activeUser));
        // redis 中存储这个加密 token，key = 前缀 + 加密 token + .ip
        this.redisService.set(BusApiConstant.TOKEN_CACHE_PREFIX + token.getToken() + StringPool.DOT + ip, token.getToken(), properties.getShiro().getJwtTimeOut() * 1000);

        return activeUser.getId();
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. Vue Router
     * 3. 用户角色
     * 4. 用户权限
     * 5. 前端系统个性化配置信息
     *
     * @param token token
     * @param user  用户信息
     * @return UserInfo
     */
    private Map<String, Object> generateUserInfo(JWTToken token, User user) {
        String username = user.getUsername();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getToken());
        userInfo.put("exipreTime", token.getExipreAt());

        Set<String> roles = this.userManager.getUserRoles(username);
        userInfo.put("roles", roles);

        Set<String> permissions = this.userManager.getUserPermissions(username);
        userInfo.put("permissions", permissions);

        UserConfig userConfig = this.userManager.getUserConfig(String.valueOf(user.getUserId()));
        userInfo.put("config", userConfig);

        Dept dept = deptService.getById(user.getDeptId());
        if(dept != null) {
            user.setDeptName(dept.getDeptName());
        }

        final Base64.Encoder encoder = Base64.getEncoder();
        userInfo.put("imPassword", encoder.encodeToString(User.IM_DEFAULT_PASSWORD.getBytes()));

        user.setPassword("it's a secret");
        userInfo.put("user", user);

        return userInfo;
    }
}
