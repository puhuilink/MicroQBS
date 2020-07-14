package com.puhuilink.qbs.example.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("index")
public class IndexController {

    @ApiOperation("Hello world")
    @GetMapping
    public String hello(){
        return "hello world";
    }

}
