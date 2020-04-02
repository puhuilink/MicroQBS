package com.phlink.demo.transfer.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phlink.core.web.base.vo.PageVO;
import com.phlink.core.web.base.vo.SearchVO;
import com.phlink.core.web.entity.User;
import com.phlink.core.web.util.SecurityUtil;
import com.phlink.demo.transfer.entity.KpiCurrent;
import com.phlink.demo.transfer.service.KpiCurrentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
@Validated
public class IndexController {
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private KpiCurrentService kpiCurrentService;

    @GetMapping("/welcome")
    @ApiOperation(value = "欢迎", notes = "欢迎", httpMethod = "GET")
    public String welcome() {
        User u = securityUtil.getCurrUser();
        return "欢迎" + u.getUsername();
    }

    @GetMapping("/page")
    @ApiOperation(value = "kpi分页列表", notes = "kpi分页列表", httpMethod = "GET")
    public PageInfo<KpiCurrent> page(SearchVO searchVo, PageVO pageVo) {
        PageInfo<KpiCurrent> page = PageHelper
                .startPage(pageVo.getPageNumber(), pageVo.getPageSize(), pageVo.getSort() + " " + pageVo.getOrder())
                .doSelectPageInfo(() -> kpiCurrentService.listByCondition(searchVo));
        return page;
    }

}
