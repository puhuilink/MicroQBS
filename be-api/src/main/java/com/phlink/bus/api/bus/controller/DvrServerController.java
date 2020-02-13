package com.phlink.bus.api.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.phlink.bus.api.common.controller.BaseController;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.dao.DvrServerMapper;
import com.phlink.bus.api.bus.domain.DvrServer;
import com.phlink.bus.api.bus.service.IDvrServerService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


/**
 * @author wen
 */
@Slf4j
@RestController
@RequestMapping("/dvr-server")
@Api(tags = "DVR视频服务器")
public class DvrServerController extends BaseController {

    @Autowired
    public IDvrServerService dvrServerService;

    @GetMapping
//    //@RequiresPermissions("dvrServer:view")
    @ApiOperation(value = "分页列表", notes = "分页列表", tags = "DVR视频服务器", httpMethod = "GET")
    public Map<String, Object> listDvrServer(QueryRequest request, DvrServer dvrServer) {
        return getDataTable(this.dvrServerService.listDvrServers(request, dvrServer));
    }

    @GetMapping("/all")
//    //@RequiresPermissions("dvrServer:view")
    @ApiOperation(value = "所有列表", notes = "所有列表", tags = "DVR视频服务器", httpMethod = "GET")
    public List<DvrServer> listDvrServer(DvrServer dvrServer) {
        QueryWrapper<DvrServer> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(dvrServer.getHost())) {
            queryWrapper.lambda().likeRight(DvrServer::getHost, dvrServer.getHost());
        }
        return this.dvrServerService.list(queryWrapper);
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("dvrServer:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "DVR视频服务器", httpMethod = "GET")
    public DvrServer detail(@PathVariable Long id) {
        return this.dvrServerService.findById(id);
    }

    @Log("添加DVR视频服务器")
    @PostMapping
//    //@RequiresPermissions("dvrServer:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "DVR视频服务器", httpMethod = "POST")
    public void addDvrServer(@RequestBody @Valid DvrServer dvrServer) throws BusApiException {
        this.dvrServerService.createDvrServer(dvrServer);
    }

    @Log("修改DVR视频服务器")
    @PutMapping
//    //@RequiresPermissions("dvrServer:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "DVR视频服务器", httpMethod = "PUT")
    public void updateDvrServer(@RequestBody @Valid DvrServer dvrServer) throws BusApiException {
        this.dvrServerService.modifyDvrServer(dvrServer);
    }

    @Log("删除DVR视频服务器")
    @DeleteMapping("/{dvrServerIds}")
//    //@RequiresPermissions("dvrServer:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "DVR视频服务器", httpMethod = "DELETE")
    public void deleteDvrServer(@NotBlank(message = "{required}") @PathVariable String dvrServerIds) throws BusApiException {
        try {
            String[] ids = dvrServerIds.split(StringPool.COMMA);
            this.dvrServerService.deleteDvrServers(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
//    //@RequiresPermissions("dvrServer:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "DVR视频服务器", httpMethod = "POST")
    public void exportDvrServer(QueryRequest request, @RequestBody DvrServer dvrServer, HttpServletResponse response) throws BusApiException {
        try {
            List<DvrServer> dvrServers = this.dvrServerService.listDvrServers(request, dvrServer).getRecords();
            ExcelKit.$Export(DvrServer.class, response).downXlsx(dvrServers, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
