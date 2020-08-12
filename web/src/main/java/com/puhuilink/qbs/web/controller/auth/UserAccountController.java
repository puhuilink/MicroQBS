/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:04:20
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-05-18 18:04:20
 */
package com.puhuilink.qbs.web.controller.auth;

import javax.validation.Valid;

import com.puhuilink.qbs.core.base.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.base.constant.CommonConstant;
import com.puhuilink.qbs.core.base.enums.LogType;
import com.puhuilink.qbs.core.common.validate.tag.OnAdd;
import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.web.controller.vo.UserRegistVO;
import com.puhuilink.qbs.web.entity.User;
import com.puhuilink.qbs.web.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags = "用户账户相关接口")
@RequestMapping("/api/auth/account")
@Transactional
public class UserAccountController {

    @Autowired
    private UserService userService;

    @Validated({ OnAdd.class })
    @PostMapping("/regist")
    @ApiOperation(value = "注册用户")
    @SystemLogTrace(description = "用户注册", type = LogType.OPERATION)
    public Result regist(
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
        return Result.ok().data(u);
    }

}
