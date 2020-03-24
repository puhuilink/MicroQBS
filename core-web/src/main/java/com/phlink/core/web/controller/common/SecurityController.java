package com.phlink.core.web.controller.common;

import cn.hutool.http.HttpUtil;
import com.phlink.core.web.base.enums.ResultCode;
import com.phlink.core.web.base.exception.BizException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "Security相关接口")
@RequestMapping("/common")
public class SecurityController {

    @GetMapping(value = "/needLogin")
    @ApiOperation(value = "没有登录")
    public void needLogin() {
        throw new BizException(ResultCode.SIGNATURE_NOT_MATCH);
    }

    @GetMapping(value = "/swagger/login")
    @ApiOperation(value = "Swagger接口文档专用登录接口 方便测试")
    public String swaggerLogin(@RequestParam String username, @RequestParam String password,
                               @ApiParam("图片验证码ID") @RequestParam(required = false) String captchaId,
                               @ApiParam("验证码") @RequestParam(required = false) String code,
                               @ApiParam("记住密码") @RequestParam(required = false, defaultValue = "true") Boolean saveLogin,
                               @ApiParam("可自定义登录接口地址")
                               @RequestParam(required = false, defaultValue = "http://127.0.0.1:8888/login")
                                       String loginUrl) {

        Map<String, Object> params = new HashMap<>(16);
        params.put("username", username);
        params.put("password", password);
        params.put("captchaId", captchaId);
        params.put("code", code);
        params.put("saveLogin", saveLogin);
        return HttpUtil.post(loginUrl, params);
    }
}