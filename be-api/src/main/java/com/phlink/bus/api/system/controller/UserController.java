package com.phlink.bus.api.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.DistributedLock;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.excel.ExcelUtil;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.service.CacheService;
import com.phlink.bus.api.common.service.MqService;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.MD5Util;
import com.phlink.bus.api.im.response.CommonResultEntity;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.domain.UserConfig;
import com.phlink.bus.api.system.domain.VO.AvatarVO;
import com.phlink.bus.api.system.domain.VO.LoginMobilePasswordVO;
import com.phlink.bus.api.system.domain.VO.LoginVO;
import com.phlink.bus.api.system.domain.VO.UsernameResetVO;
import com.phlink.bus.api.system.domain.enums.UserTypeEnum;
import com.phlink.bus.api.system.service.LoginLogService;
import com.phlink.bus.api.system.service.UserConfigService;
import com.phlink.bus.api.system.service.UserService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Validated
@RestController
@RequestMapping("user")
@Api(tags ="系统用户")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private MqService mqService;
    @Autowired
    private LoginLogService loginLogService;

    @GetMapping("/check/{username}")
    @ApiOperation(value = "检查用户名是否存在", notes = "检查用户名是否存在", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "GET")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    @GetMapping("/check-identity")
    @ApiOperation(value = "检查身份证和名字是否正确", notes = "检查身份证和名字是否正确", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "GET")
    public boolean checkIdentity(@NotBlank(message = "{required}") @RequestParam("realname") String realname,
                               @NotBlank(message = "{required}") @RequestParam("idcard") String idcard
                               ) throws Exception {
        User currentUser = BusApiUtil.getCurrentUser();
        if(currentUser == null) {
            throw new BusApiException("还未登录");
        }
        User entityUser = userService.findByName(currentUser.getUsername());
        if(idcard != null && idcard.equals(entityUser.getIdcard()) && realname != null && realname.equals(entityUser.getRealname())) {
            entityUser.setCheckIdentify(true);
            userService.updateProfile(entityUser);
           return true;
        }

        return false;
    }

    @GetMapping("/login-times")
    @ApiOperation(value = "登录次数", notes = "登录次数", httpMethod = "GET")
    public BusApiResponse loginTimes() throws Exception {
        User currentUser = BusApiUtil.getCurrentUser();
        if(currentUser == null) {
            throw new BusApiException("您还未登录");
        }
        int count = loginLogService.getCountByUser(currentUser.getUsername());
        User user = userService.findByName(currentUser.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("times", count);
        data.put("checkIdentify", user.getCheckIdentify());
        return new BusApiResponse().data(data);
    }

    @GetMapping("/{username}")
    @ApiOperation(value = "用户详情", notes = "根据用户名获取用户详情", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "GET")
    public User detail(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username);
    }

    @GetMapping
    @ApiOperation(value = "后台用户列表", notes = "这里只展示后台用户，用于后台用户的管理", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "GET")
    //@RequiresPermissions("user:view")
    public Map<String, Object> userList(QueryRequest queryRequest, User user) {
        return getDataTable(userService.findUserDetail(user, queryRequest));
    }

    @Log("修改用户")
    @PutMapping
    @Validated({OnUpdate.class})
    //@RequiresPermissions("user:update")
    public void updateUser(@RequestBody @Valid User user) throws BusApiException {
        checkUser(user);
        try {
            this.userService.updateUser(user);
        } catch (Exception e) {
            String message = "修改用户失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }



    private void checkUser(User user) throws BusApiException {
        checkMobile(user.getMobile(), user.getUserId());
        checkUsername(user.getUsername(), user.getUserId());
    }


    private void checkMobile(String mobile, Long userId) throws BusApiException {
        if(StringUtils.isBlank(mobile)) {
            return;
        }
        User user = userService.findByMobile(mobile);
        if(user != null) {
            if(!user.getUserId().equals(userId)) {
                throw new BusApiException("手机号不能重复");
            }
        }
    }

    private void checkUsername(String username, Long userId) throws BusApiException {
        if(StringUtils.isBlank(username)) {
            return;
        }
        User user = userService.findByName(username);
        if(user != null) {
            if(!user.getUserId().equals(userId)) {
                throw new BusApiException("手机号不能重复");
            }
        }
    }

    @DistributedLock(prefix = "addUser")
    @Log("新增员工")
    @PostMapping
    @Validated({OnAdd.class})
    @ApiOperation(value = "新增员工", notes = "新增员工", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "POST")
    //@RequiresPermissions("user:add")
    public void addUser(@RequestBody @Valid User user) throws BusApiException {
        checkUser(user);
        try {
            if(StringUtils.isBlank(user.getPassword())) {
                //设置默认密码
                user.setPassword(User.DEFAULT_PASSWORD);
            }
            user.setUserType(UserTypeEnum.ADMIN);
            this.userService.createUser(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusApiException(e.getMessage());
        }
    }

    @Log("删除用户")
    @DeleteMapping("/{userIds}")
    @ApiOperation(value = "删除用户", notes = "删除用户", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "DELETE")
    //@RequiresPermissions("user:delete")
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws Exception {
        String[] ids = userIds.split(StringPool.COMMA);
        this.userService.deleteUsers(ids);
    }

    @Validated({OnUpdate.class})
    @ApiOperation(value = "更新个人信息", notes = "更新个人信息", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "PUT")
    @PutMapping("profile")
    public void updateProfile(@RequestBody @Valid User user) throws BusApiException {
        try {
            this.userService.updateProfile(user);
        } catch (Exception e) {
            String message = "修改个人信息失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "更新个人头像", notes = "更新个人头像", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "PUT")
    @PutMapping("avatar")
    public void updateAvatar(@RequestBody @Valid AvatarVO avatarVO) throws BusApiException {
        try {
            this.userService.updateAvatar(avatarVO.getUserName(), avatarVO.getAvatar());
        } catch (Exception e) {
            String message = "修改头像失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "更新用户配置", notes = "更新用户配置", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "PUT")
    @PutMapping("userconfig")
    public void updateUserConfig(@RequestBody @Valid UserConfig userConfig) throws BusApiException {
        try {
            this.userConfigService.update(userConfig);
        } catch (Exception e) {
            String message = "修改个性化配置失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "检查密码", notes = "检查密码", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "GET")
    @GetMapping("password/check")
    public boolean checkPassword(@Valid LoginVO loginVO) {
        String encryptPassword = MD5Util.encrypt(loginVO.getUsername(), loginVO.getPassword());
        User user = userService.findByName(loginVO.getUsername());
        if (user != null) {
            return StringUtils.equals(user.getPassword(), encryptPassword);
        }
        return false;
    }

    @ApiOperation(value = "更新密码", notes = "更新密码", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "PUT")
    @PutMapping("password")
    public void updatePassword(@RequestBody @Valid LoginVO loginVO) throws BusApiException {
        User user = userService.findByName(loginVO.getUsername());
        if(user == null) {
            throw new BusApiException("用户不存在");
        }
        try {
            userService.updatePassword(loginVO.getUsername(), loginVO.getPassword());
        } catch (Exception e) {
            String message = "修改密码失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "使用手机号更新密码", notes = "使用手机号更新密码", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "PUT")
    @PutMapping("/mobile/password")
    public void updatePasswordByMobile(@RequestBody @Valid LoginMobilePasswordVO loginVO) throws Exception {
        User user = userService.findByMobile(loginVO.getMobile());
        if(user == null) {
            throw new BusApiException("用户不存在");
        }
        userService.updatePassword(user.getUsername(), loginVO.getPassword());
    }

    @ApiOperation(value = "重置密码", notes = "重置密码", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "PUT")
    @PutMapping("password/reset")
    //@RequiresPermissions("user:reset")
    public void resetPassword(@RequestBody UsernameResetVO usernameResetVO) throws BusApiException {
        try {
            String usernames = usernameResetVO.getUsernames();
            String[] usernameArr = usernames.split(StringPool.COMMA);
            this.userService.resetPassword(usernameArr);
        } catch (Exception e) {
            String message = "重置用户密码失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "导出excel数据", notes = "导出excel数据", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "POST")
    @PostMapping("export")
    //@RequiresPermissions("user:export")
    public void export(@RequestBody @Valid User user, HttpServletResponse response) throws BusApiException {
        try {
            List<User> users = this.userService.listUserDetail(user);
            ExcelKit.$Export(User.class, response).downXlsx(users, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "下载excel模版", notes = "下载excel模版", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "GET")
    @GetMapping(value = "/down-template")
    public void downTemplate(HttpServletResponse response) {
        List<User> list = userService.list();
        ExcelKit.$Export(User.class, response).downXlsx(list, true);
    }

    @PostMapping("/import")
    //@RequiresPermissions("user:add")
    @ApiOperation(value = "导入员工信息", notes = "导入员工信息", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "POST")
    public void importUser(@RequestParam(name = "file") MultipartFile file) {
        try {
            List<User> list = ExcelUtil.readExcel(Objects.requireNonNull(file.getOriginalFilename()), file.getInputStream(), User.class.getName(),1);
            log.info("import studentList length: {}", list.size());
            userService.batchImport(list);
        }catch (Exception e) {
            log.error("导入失败", e);
        }
    }

    @GetMapping("/change-mobile-captcha/{mobile}")
    @ApiOperation(value = "获取修改手机号验证码", notes = "获取修改手机号验证码", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "GET")
    public BusApiResponse changeMobile(@PathVariable String mobile) throws Exception {
        // 验证当前登录用户手机号是否是该手机号
        User user = userService.findByMobile(mobile);
        if(user == null) {
            throw new BusApiException("该手机号不存在");
        }
        if(!mobile.equals(BusApiUtil.getCurrentUser().getMobile())) {
            throw new BusApiException("该手机号已存在");
        }
        // 生成验证码
        String random = BusApiUtil.getRandom();
        // 缓存验证码
        this.cacheService.saveCaptcha(mobile, random);
        // 异步发送验证码
        mqService.sendCaptcha(mobile);
        return new BusApiResponse().message("发送成功").data(random);
    }

    @GetMapping("/check-mobile-captcha")
    @ApiOperation(value = "检查修改手机号的验证码", notes = "检查修改手机号的验证码", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "GET")
    public BusApiResponse checkChangeMobileCaptcha(@NotBlank(message = "{required}") @RequestParam(name = "mobile") String mobile,
                                            @NotBlank(message = "{required}") @RequestParam(name = "captcha") String captcha) throws Exception {
        boolean isSuccess = false;
        String message = "验证码错误";
        String captchaSource = cacheService.getCaptcha(mobile);
        if(captcha.equals(captchaSource))  {
            isSuccess = true;
            message = "验证成功";
        }
        return new BusApiResponse().data(isSuccess).message(message);
    }

    @ApiOperation(value = "更新手机号", notes = "更新手机号", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "PUT")
    @PutMapping("/mobile/{mobile}")
    public BusApiResponse updateMobile(@PathVariable String mobile) throws Exception {
        userService.updateMobile(mobile);
        //TODO 通知其他监护人
        return new BusApiResponse().message("修改成功");
    }

    @GetMapping("/password/status")
    @ApiOperation(value = "获得登录用户设置密码状态", notes = "获得登录用户设置密码状态，有密码返回true，没有密码返回false", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "GET")
    public BusApiResponse getPasswordStatus() throws Exception {
        User user = userService.findByName(BusApiUtil.getCurrentUser().getUsername());
        boolean isNotBlank = StringUtils.isNotBlank(user.getPassword());
        return new BusApiResponse().data(isNotBlank);
    }

    @PostMapping("/{mobile}/register-to-im")
    @ApiOperation(value = "将本系统用户注册到im", notes = "将本系统用户注册到im", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "POST")
    public BusApiResponse registerToIm(@PathVariable String mobile) throws Exception {
        User user = userService.findByMobile(mobile);
        if(user == null) {
            throw new BusApiException("用户不存在");
        }
        CommonResultEntity result = userService.registerToImServer(user);
        return new BusApiResponse().data(result);
    }


    @ApiOperation(value = "更新RegistrationID", notes = "更新RegistrationID", tags = ApiTagsConstant.TAG_SYSUSER, httpMethod = "PUT")
    @PutMapping("/jiguang/{registrationId}")
    public BusApiResponse updateRegistrationId(@PathVariable String registrationId) throws BusApiException {
        User user = userService.findByName(BusApiUtil.getCurrentUser().getUsername());
        if(user == null) {
            throw new BusApiException("用户不存在");
        }
        userService.updateRegistrationId(registrationId, user.getUserId());
        return new BusApiResponse().message("成功");
    }
}
