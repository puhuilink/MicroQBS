package com.phlink.demo.controller;

import com.phlink.core.web.entity.User;
import com.phlink.core.web.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/demo")
@Validated
public class IndexController {
    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping("/welcome")
    @ApiOperation(value = "欢迎", notes = "欢迎", httpMethod = "GET")
    public String welcome() {

        User u = securityUtil.getCurrUser();
        return "欢迎" + u.getUsername();
    }

    @GetMapping("/index")
    @ApiOperation(value = "index", notes = "index", httpMethod = "GET")
    public void index() {
        log.info("index接口");
    }

}
