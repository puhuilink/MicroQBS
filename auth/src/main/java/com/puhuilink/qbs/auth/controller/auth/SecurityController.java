/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:04:26
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:07:31
 */
package com.puhuilink.qbs.auth.controller.auth;

import com.google.gson.Gson;
import com.puhuilink.qbs.core.base.vo.Result;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "Security相关接口")
@RequestMapping("/api/auth")
public class SecurityController {

    @Autowired
    private Gson gson;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/swagger/login")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", defaultValue = "admin"),
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "123456")
    })
    @ApiOperation(value = "Swagger接口文档专用登录接口 方便测试")
    public Result swaggerLogin(@RequestParam String username,
                               @RequestParam String password,
                               @ApiParam("记住密码") @RequestParam(required = false, defaultValue = "true") Boolean saveLogin,
                               @ApiParam("可自定义登录接口地址") @RequestParam(required = false, defaultValue = "http://127.0.0.1:8899/api/auth/login") String loginUrl) {

        Map<String, Object> params = new HashMap<>(16);
        params.put("username", username);
        params.put("password", password);
        params.put("saveLogin", saveLogin);
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        header.put(HttpHeaders.ACCEPT, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        HttpEntity<String> request = new HttpEntity<>(gson.toJson(params));
        ResponseEntity<String> exchangeResult = restTemplate.exchange(loginUrl, HttpMethod.POST, request, String.class);
        return Result.ok().data(exchangeResult.getBody());
    }
}
