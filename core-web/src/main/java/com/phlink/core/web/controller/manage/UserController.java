package com.phlink.core.web.controller.manage;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.excel.EasyExcel;
import com.phlink.core.base.annotation.SystemLogTrace;
import com.phlink.core.base.constant.CommonConstant;
import com.phlink.core.base.enums.LogType;
import com.phlink.core.base.utils.ResultUtil;
import com.phlink.core.base.validation.tag.OnAdd;
import com.phlink.core.base.vo.Result;
import com.phlink.core.web.controller.vo.UserData;
import com.phlink.core.web.controller.vo.UserRegistVO;
import com.phlink.core.web.entity.User;
import com.phlink.core.web.service.DepartmentService;
import com.phlink.core.web.service.RoleService;
import com.phlink.core.web.service.UserService;
import com.phlink.core.web.utils.SecurityUtil;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wen
 */
@Slf4j
@RestController
@Api(tags = "User相关接口")
@RequestMapping("")
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

    @Validated({ OnAdd.class })
    @PostMapping("/api/noauth/user/regist")
    @ApiOperation(value = "注册用户")
    @SystemLogTrace(description = "用户注册", type = LogType.OPERATION)
    public User regist(
            @RequestBody @Valid @ApiParam(name = "用户注册表单", value = "传入json格式", required = true) UserRegistVO userRegistVo) {
        User u = new User();
        String encryptPass = new BCryptPasswordEncoder().encode(userRegistVo.getPassword());
        u.setPassword(encryptPass);
        u.setType(CommonConstant.USER_TYPE_NORMAL);
        u.setUsername(userRegistVo.getUsername());
        u.setEmail(userRegistVo.getEmail());
        u.setMobile(userRegistVo.getMobile());
        u.setRealname(userRegistVo.getRealname());
        userService.save(u);
        return u;
    }

    @GetMapping("/api/manage/user/info")
    @ApiOperation(value = "已登录用户")
    public User userInfo() {
        User u = securityUtil.getCurrUser();
        u.setPassword(null);
        return u;
    }

    @PostMapping(value = "/api/manage/user/pwd/reset")
    @ApiOperation(value = "重置密码")
    @SystemLogTrace(description = "重置密码", type = LogType.OPERATION)
    public Result<Object> resetPass(@RequestParam String[] ids) {
        for (String id : ids) {
            User u = userService.getById(id);
            u.setPassword(new BCryptPasswordEncoder().encode("123456"));
            userService.updateById(u);
            redissonClient.getBucket("user::" + u.getUsername()).delete();
        }
        return ResultUtil.success("操作成功");
    }

    @GetMapping("/api/manage/user/excel/download")
    @SystemLogTrace(description = "用户数据Excel下载", type = LogType.OPERATION)
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("系统用户列表", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), UserData.class).sheet("用户信息").doWrite(userService.listUserData());
    }
}
