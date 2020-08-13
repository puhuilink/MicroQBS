/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:05:14
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:08:01
 */
package com.puhuilink.qbs.auth.controller.manage;

import com.puhuilink.qbs.auth.entity.User;
import com.puhuilink.qbs.auth.service.DepartmentService;
import com.puhuilink.qbs.auth.service.RoleService;
import com.puhuilink.qbs.auth.service.UserService;
import com.puhuilink.qbs.auth.utils.SecurityUtil;
import com.puhuilink.qbs.core.base.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.base.enums.LogType;
import com.puhuilink.qbs.core.base.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "User相关接口")
@RequestMapping("/api/manage/user")
@Transactional
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping("/info")
    @ApiOperation(value = "已登录用户")
    public Result userInfo() {
        User u = securityUtil.getCurrUser();
        u.setPassword(null);
        return Result.ok().data(u);
    }

    @PostMapping(value = "/reset")
    @ApiOperation(value = "重置密码")
    @SystemLogTrace(description = "重置密码", type = LogType.OPERATION)
    public Result resetPass(@RequestParam String[] ids) {
        for (String id : ids) {
            User u = userService.getById(id);
            u.setPassword(new BCryptPasswordEncoder().encode("123456"));
            userService.updateById(u);
        }
        return Result.ok("操作成功");
    }
}
