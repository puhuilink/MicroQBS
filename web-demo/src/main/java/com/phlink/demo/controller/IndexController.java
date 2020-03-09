package com.phlink.demo.controller;

import com.phlink.core.common.exception.BizException;
import com.phlink.core.entity.User;
import com.phlink.core.util.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/")
@Validated
public class IndexController {
    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping("/welcome")
    @ApiOperation(value = "欢迎", notes = "欢迎", httpMethod = "GET")
    public String welcome(){

        User u = securityUtil.getCurrUser();
        return "欢迎" + u.getUsername();
    }

}
