package com.phlink.core.controller.manage;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phlink.core.common.utils.ResultUtil;
import com.phlink.core.common.vo.PageVO;
import com.phlink.core.common.vo.Result;
import com.phlink.core.common.vo.SearchVO;
import com.phlink.core.entity.DictData;
import com.phlink.core.entity.LogTrace;
import com.phlink.core.service.LogTraceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "日志管理接口")
@RequestMapping("/manage/log")
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

    @DeleteMapping(value = "/delete/{ids}")
    @ApiOperation(value = "批量删除")
    public String delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            logService.removeById(id);
        }
        return "删除成功";
    }

    @DeleteMapping(value = "/deleteAll")
    @ApiOperation(value = "全部删除")
    public String deleteAll() {
        logService.removeAll();
        return "删除成功";
    }
}