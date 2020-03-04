package com.phlink.core.controller.manage;

import com.phlink.core.common.constant.CommonConstant;
import com.phlink.core.common.utils.ResultUtil;
import com.phlink.core.common.validation.tag.OnAdd;
import com.phlink.core.common.vo.Result;
import com.phlink.core.controller.vo.UserRegistVO;
import com.phlink.core.entity.User;
import com.phlink.core.service.DepartmentService;
import com.phlink.core.service.RoleService;
import com.phlink.core.service.UserService;
import com.phlink.core.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(tags = "User相关接口")
@RequestMapping("/manage/user")
@CacheConfig(cacheNames = "user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private RedissonClient redissonClient;

    @Validated({OnAdd.class})
    @PostMapping("/regist")
    @ApiOperation(value = "注册用户")
    public User regist(@RequestBody @Valid @ApiParam(name = "用户注册表单", value = "传入json格式", required = true) UserRegistVO userRegistVo) {
        User u = new User();
        String encryptPass = new BCryptPasswordEncoder().encode(u.getPassword());
        u.setPassword(encryptPass);
        u.setType(CommonConstant.USER_TYPE_NORMAL);
        u.setUsername(userRegistVo.getUsername());
        u.setEmail(userRegistVo.getEmail());
        u.setMobile(userRegistVo.getMobile());
        u.setRealname(userRegistVo.getRealname());
        userService.save(u);
        return u;
    }

    @GetMapping("/info")
    @ApiOperation(value = "已登录用户")
    public User userInfo() {
        User u = securityUtil.getCurrUser();
        u.setPassword(null);
        return u;
    }

    @PostMapping(value = "/pwd/reset")
    @ApiOperation(value = "重置密码")
    public Result<Object> resetPass(@RequestParam String[] ids) {
        for (String id : ids) {
            User u = userService.getById(id);
            u.setPassword(new BCryptPasswordEncoder().encode("123456"));
            userService.updateById(u);
            redissonClient.getBucket("user::" + u.getUsername()).delete();
        }
        return ResultUtil.success("操作成功");
    }

}
