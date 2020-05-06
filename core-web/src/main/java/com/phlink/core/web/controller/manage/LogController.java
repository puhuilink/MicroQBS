package com.phlink.core.web.controller.manage;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phlink.core.base.vo.PageVO;
import com.phlink.core.base.vo.SearchVO;
import com.phlink.core.web.entity.LogTrace;
import com.phlink.core.web.service.LogTraceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wen
 */
@Slf4j
@RestController
@Api(tags = "日志管理接口")
@RequestMapping("/api/manage/log")
@Transactional
public class LogController {

    @Autowired
    private LogTraceService logService;

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页获取全部")
    public PageInfo<LogTrace> page(@RequestParam(required = false) Integer type,
                                   @RequestParam String key,
                                   SearchVO searchVo,
                                   PageVO pageVo) {
        PageInfo<LogTrace> page = PageHelper.startPage(pageVo.getPageNumber(), pageVo.getPageSize(), pageVo.getSort() + " " + pageVo.getOrder())
                .doSelectPageInfo(() -> logService.listByCondition(type, key, searchVo));
        return page;
    }

    // @DeleteMapping(value = "/{ids}")
    // @ApiOperation(value = "批量删除")
    // public String delByIds(@PathVariable String[] ids) {

    //     for (String id : ids) {
    //         logService.removeById(id);
    //     }
    //     return "删除成功";
    // }

    // @DeleteMapping(value = "/all")
    // @ApiOperation(value = "全部删除")
    // public String deleteAll() {
    //     logService.removeAll();
    //     return "删除成功";
    // }
}
