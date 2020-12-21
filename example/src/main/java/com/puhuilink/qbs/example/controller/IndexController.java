package com.puhuilink.qbs.example.controller;

import com.puhuilink.qbs.core.base.vo.Result;
import com.puhuilink.qbs.core.limiter.annotation.Limiter;
import com.puhuilink.qbs.core.logtrace.annotation.SystemLogTrace;
import com.puhuilink.qbs.example.service.IndexService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${qbs.entrypoint.base}" + "/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @SystemLogTrace(description = "index欢迎")
    @Limiter(QPS = 1, key = "index:hello")
    @ApiOperation("Hello world")
    @GetMapping
    public Result hello() {
        return Result.ok().data("hello world");
    }

    @SystemLogTrace(description = "retry")
    @Limiter(QPS = 1, key = "index:retry")
    @ApiOperation("name")
    @GetMapping("/retry/{name}")
    public Result retry(@PathVariable String name) {
        indexService.getByNameRetry(name);
        return Result.ok().data("retry");
    }

}
