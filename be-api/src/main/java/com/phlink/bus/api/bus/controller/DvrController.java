package com.phlink.bus.api.bus.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.bus.domain.Dvr;
import com.phlink.bus.api.bus.domain.DvrCameraConfig;
import com.phlink.bus.api.bus.domain.DvrServer;
import com.phlink.bus.api.bus.domain.VO.DvrGuardianViewInfo;
import com.phlink.bus.api.bus.domain.VO.DvrInfo;
import com.phlink.bus.api.bus.domain.VO.DvrRtmpInfo;
import com.phlink.bus.api.bus.manager.ZaDvrManager;
import com.phlink.bus.api.bus.response.DvrRtmpResponse;
import com.phlink.bus.api.bus.service.IDvrCameraConfigService;
import com.phlink.bus.api.bus.service.IDvrServerService;
import com.phlink.bus.api.bus.service.IDvrService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
* @author wen
*/
@Slf4j
@RestController
@RequestMapping("/dvr")
@Api(tags ="DVR设备", hidden = true)
public class DvrController extends BaseController {

    @Autowired
    public IDvrService dvrService;
    @Autowired
    public ZaDvrManager zaDvrManager;
    @Autowired
    public IDvrServerService dvrServerService;
    @Autowired
    public IDvrCameraConfigService dvrCameraConfigService;

    @Log("添加DVR设备")
    @PostMapping
//    //@RequiresPermissions("dvr:add")
    @ApiOperation(value = "添加", notes = "添加", tags = "dvr设备", httpMethod = "POST")
    public void addDvr(@RequestBody @Valid Dvr dvr) throws BusApiException {
        this.dvrService.createDvr(dvr);
    }

    @GetMapping("{dvrCode}")
//    //@RequiresPermissions("dvr:update")
    @ApiOperation(value = "详情", notes = "详情", tags = "dvr设备", httpMethod = "GET")
    public Dvr getByCode(@PathVariable String dvrCode) throws BusApiException{
        return dvrService.getByDvrCode(dvrCode);
    }

    @Log("修改DVR设备")
    @PutMapping
//    //@RequiresPermissions("dvr:update")
    @ApiOperation(value = "修改", notes = "修改", tags = "dvr设备", httpMethod = "PUT")
    public void updateDvr(@RequestBody @Valid Dvr dvr) throws BusApiException{
        this.dvrService.modifyDvr(dvr);
    }

    @Log("删除DVR设备")
    @DeleteMapping("/{dvrIds}")
//    //@RequiresPermissions("dvr:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = "dvr设备", httpMethod = "DELETE")
    public void deleteDvr(@NotBlank(message = "{required}") @PathVariable String dvrIds) throws BusApiException{
        try {
            String[] ids = dvrIds.split(StringPool.COMMA);
            this.dvrService.deleteDvrs(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
//    //@RequiresPermissions("dvr:export")
    @ApiOperation(value = "导出", notes = "导出", tags = "dvr设备", httpMethod = "POST")
    public void exportDvr(QueryRequest request, @RequestBody Dvr dvr, HttpServletResponse response) throws BusApiException{
        try {
            List<Dvr> dvrs = this.dvrService.listDvrs(request, dvr).getRecords();
            ExcelKit.$Export(Dvr.class, response).downXlsx(dvrs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @GetMapping("/rtmp-info/{dvrCode}")
//    //@RequiresPermissions("dvr:update")
    @ApiOperation(value = "获取视频播放信息", notes = "获取视频播放信息", tags = "dvr设备", httpMethod = "GET")
    public DvrRtmpResponse getRtmpInfo(@PathVariable String dvrCode,
                                       @NotBlank(message = "{required}") @RequestParam(name = "channel") Integer channel
                           ) throws BusApiException{
        Dvr dvr = dvrService.getByDvrCode(dvrCode);
        if(dvr == null) {
            throw new BusApiException("DVR设备未录入系统");
        }
        if(dvr.getDvrServerId() == null) {
            String message = "DVR 设备" + dvr.getDvrCode() + "未配置视频服务地址";
            log.error(message);
            throw new BusApiException(message);
        }
        DvrServer dvrServer = dvrServerService.getById(dvr.getDvrServerId());
        return zaDvrManager.getDvrRtmpInfo(channel, 1, dvrCode, 1, dvrServer);
    }

    @GetMapping("/bus/{busId}/rtmp-info")
//    //@RequiresPermissions("dvr:update")
    @ApiOperation(value = "根据车辆ID获取视频播放信息", notes = "根据车辆ID获取视频播放信息", tags = "dvr设备", httpMethod = "GET")
    public DvrRtmpInfo getRtmpInfo(@PathVariable Long busId) throws BusApiException{
        Dvr dvr = dvrService.getByBusId(busId);
        if(dvr == null) {
            throw new BusApiException("该车辆未绑定DVR");
        }
        if(dvr.getDvrServerId() == null) {
            String message = "DVR 设备" + dvr.getDvrCode() + "未配置视频服务地址";
            log.error(message);
            throw new BusApiException(message);
        }
        DvrServer dvrServer = dvrServerService.getById(dvr.getDvrServerId());


        List<CompletableFuture<DvrRtmpResponse>> futures = new ArrayList<>();
        int channel = dvr.getChannelNumber();
        for(int i = 0; i<channel; i++) {
            CompletableFuture<DvrRtmpResponse> resultFuture = zaDvrManager.getDvrRtmpInfoAsync(i, 1, dvr.getDvrCode(), 1, dvrServer);
            futures.add(resultFuture);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        Map<Integer, DvrRtmpResponse> result = new HashedMap();
        futures.forEach( f -> {
            try {
                DvrRtmpResponse response = f.get(10, TimeUnit.SECONDS);
                if(response != null) {
                    result.put(response.getChannelCode(), response);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        });


        DvrRtmpInfo dvrRtmpInfo = new DvrRtmpInfo();
        dvrRtmpInfo.setChannelNumber(dvr.getChannelNumber());
        dvrRtmpInfo.setDvrCode(dvr.getDvrCode());
        dvrRtmpInfo.setData(result);
        return dvrRtmpInfo;
    }


    @GetMapping("/bus/{busId}")
//    //@RequiresPermissions("dvr:update")
    @ApiOperation(value = "根据车辆ID获取DVR信息", notes = "根据车辆ID获取DVR信息", tags = "dvr设备", httpMethod = "GET")
    public DvrInfo getDvr(@PathVariable Long busId) throws BusApiException{
        Dvr dvr = dvrService.getByBusId(busId);
        if(dvr == null) {
            throw new BusApiException("该车辆未绑定DVR");
        }
        if(dvr.getDvrServerId() == null) {
            String message = "DVR 设备" + dvr.getDvrCode() + "未配置视频服务地址";
            log.error(message);
            throw new BusApiException(message);
        }

        return getDvrInfoDetail(dvr);
    }

    @GetMapping("/detail/{dvrCode}")
//    //@RequiresPermissions("dvr:update")
    @ApiOperation(value = "根据Dvr编号获取DVR信息", notes = "根据Dvr编号获取DVR信息", tags = "dvr设备", httpMethod = "GET")
    public DvrInfo getDvr(@PathVariable String dvrCode) throws BusApiException{
        Dvr dvr = dvrService.getByDvrCode(dvrCode);
        if(dvr == null) {
            throw new BusApiException("该DVR未绑定");
        }
        if(dvr.getDvrServerId() == null) {
            String message = "DVR 设备" + dvr.getDvrCode() + "未配置视频服务地址";
            log.error(message);
            throw new BusApiException(message);
        }

        return getDvrInfoDetail(dvr);
    }

    public DvrInfo getDvrInfoDetail(Dvr dvr) {
        DvrServer dvrServer = dvrServerService.getById(dvr.getDvrServerId());
        String token = zaDvrManager.getDvrServerToken(dvrServer.getHost(), dvrServer.getPort());

        List<DvrCameraConfig> configs = dvrCameraConfigService.listByBusId(dvr.getBusId());

        DvrInfo dvrInfo = new DvrInfo();
        dvrInfo.setChannelNumber(dvr.getChannelNumber());
        dvrInfo.setDvrCode(dvr.getDvrCode());
        dvrInfo.setDvrServer(dvrServer);
        dvrInfo.setChannelConfig(configs);
        dvrInfo.setToken(token);
        return dvrInfo;
    }


    @GetMapping("/bus/{busId}/guardian-view")
//    //@RequiresPermissions("dvr:update")
    @ApiOperation(value = "根据车辆ID获取家长视角的DVR信息", notes = "根据车辆ID获取家长视角的DVR信息", tags = "dvr设备", httpMethod = "GET")
    public DvrGuardianViewInfo getGuardianViewDvr(@PathVariable Long busId) throws BusApiException{
        Dvr dvr = dvrService.getByBusId(busId);
        String message = "";
        if(dvr == null) {
            message = "该车辆未绑定DVR";
            throw new BusApiException(message);
        }
        if(dvr.getDvrServerId() == null) {
            message = "DVR 设备" + dvr.getDvrCode() + "未配置视频服务地址";
            log.error(message);
            throw new BusApiException(message);
        }
        if(dvr.getOnline() == null || dvr.getOnline() != 1) {
            message = "该车辆设备不在线";
            log.error(message + "busId: {} online: {}", busId, dvr.getOnline());
            throw new BusApiException(message);
        }

        DvrServer dvrServer = dvrServerService.getById(dvr.getDvrServerId());
        String token = zaDvrManager.getDvrServerToken(dvrServer.getHost(), dvrServer.getPort());

        DvrCameraConfig config = dvrCameraConfigService.getGuardianLocationConfig(busId);

        DvrGuardianViewInfo dvrInfo = new DvrGuardianViewInfo();
        dvrInfo.setDvrCode(dvr.getDvrCode());
        dvrInfo.setDvrServer(dvrServer);
        dvrInfo.setDvrCamera(config);
        dvrInfo.setToken(token);
        return dvrInfo;
    }

    //查询所有drv
    @GetMapping("/drv/List")
    @ApiOperation(value = "获取drv信息", notes = "获取drv信息", tags = "dvr设备", httpMethod = "GET")
    public List<Dvr> drvList(){
        List<Dvr> list= dvrService.listDrv();
        return list;
    }
}