package com.phlink.demo.transfer.controller;

import java.util.List;

import com.phlink.core.web.base.vo.PageVO;
import com.phlink.core.web.entity.User;
import com.phlink.core.web.util.SecurityUtil;
import com.phlink.demo.transfer.entity.KpiCurrent;
import com.phlink.demo.transfer.service.KpiCurrentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/kpi")
@Validated
public class IndexController {
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private KpiCurrentService kpiCurrentService;

    @GetMapping("/welcome")
    @ApiOperation(value = "欢迎", notes = "欢迎", httpMethod = "GET")
    public Object welcome() {
        User u = securityUtil.getCurrUser();
        return "欢迎" + u.getUsername();
    }

    @GetMapping("/offset")
    @ApiOperation(value = "kpi分页列表", notes = "kpi分页列表", httpMethod = "GET")
    public List<KpiCurrent> offset(PageVO pageVo) {
        if (StrUtil.isBlank(pageVo.getSort())) {
            pageVo.setSort("kpi_code");
        }
        if (StrUtil.isBlank(pageVo.getOrder())) {
            pageVo.setOrder("asc");
        }
        return kpiCurrentService.listByOffset(pageVo);
    }

}
