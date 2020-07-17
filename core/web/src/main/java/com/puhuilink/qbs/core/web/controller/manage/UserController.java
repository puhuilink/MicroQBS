/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:05:14
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:08:01
 */
package com.puhuilink.qbs.core.web.controller.manage;

import com.alibaba.excel.EasyExcel;
import com.puhuilink.qbs.core.base.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.base.enums.LogType;
import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.core.web.controller.vo.UserData;
import com.puhuilink.qbs.core.web.entity.User;
import com.puhuilink.qbs.core.web.service.DepartmentService;
import com.puhuilink.qbs.core.web.service.RoleService;
import com.puhuilink.qbs.core.web.service.UserService;
import com.puhuilink.qbs.core.web.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@RestController
@Api(tags = "User相关接口")
@RequestMapping("/api/manage/user")
@CacheConfig(cacheNames = "user")
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
    @Autowired
    private RedissonClient redissonClient;

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
    public Result<Object> resetPass(@RequestParam String[] ids) {
        for (String id : ids) {
            User u = userService.getById(id);
            u.setPassword(new BCryptPasswordEncoder().encode("123456"));
            userService.updateById(u);
            redissonClient.getBucket("user::" + u.getUsername()).delete();
        }
        return Result.ok("操作成功");
    }

    @GetMapping("/excel/download")
    @SystemLogTrace(description = "用户数据Excel下载", type = LogType.OPERATION)
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("系统用户列表", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), UserData.class).sheet("用户信息").doWrite(userService.listUserData());
    }
}
