package com.puhuilink.qbs.example.controller;

import com.puhuilink.qbs.core.base.annotation.SystemLogTrace;
import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.core.limiter.annotation.Limiter;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("index")
public class IndexController {

    @SystemLogTrace()
    @Limiter(QPS = 1, key = "index:hello")
    @ApiOperation("Hello world")
    @GetMapping
    public Result hello() {
        return Result.ok("hello world");
    }

}
