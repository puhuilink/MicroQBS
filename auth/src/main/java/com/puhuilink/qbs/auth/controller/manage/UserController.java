/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:05:14
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:08:01
 */
package com.puhuilink.qbs.auth.controller.manage;

import com.puhuilink.qbs.auth.entity.User;
import com.puhuilink.qbs.auth.service.UserService;
import com.puhuilink.qbs.auth.service.UserTokenService;
import com.puhuilink.qbs.auth.utils.SecurityConstant;
import com.puhuilink.qbs.auth.utils.SecurityUtil;
import com.puhuilink.qbs.core.base.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.base.enums.LogType;
import com.puhuilink.qbs.core.base.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@Api(tags = "User相关接口")
@RequestMapping("${qbs.api.path}" + "/manage/user")
@Transactional
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private UserTokenService userTokenService;

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
        userService.resetPassword(ids);
        return Result.ok("操作成功");
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户注销")
    @SystemLogTrace(description = "用户注销", type = LogType.LOGIN)
    public Result<Object> logout(HttpServletRequest request) {
        String accessToken = request.getHeader(SecurityConstant.HEADER_PARAM);
        User u = securityUtil.getCurrUser();
        userTokenService.logout(u.getId(), accessToken);
        SecurityContextHolder.clearContext();
        return Result.ok().msg("注销成功");
    }
}
