package com.phlink.bus.api.adinfo.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.adinfo.domain.Adinfo;
import com.phlink.bus.api.adinfo.domain.AdinfoData;
import com.phlink.bus.api.adinfo.domain.VO.AdinfoVo;
import com.phlink.bus.api.adinfo.service.IAdinfoService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.BusApiResponse;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.system.controller.FileSupportController;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


/**
* @author Maibenben
*/
@Slf4j
@RestController
@RequestMapping("/adinfo")
@Api(tags = ApiTagsConstant.TAG_BANNER)
public class AdinfoController extends BaseController {
    @Autowired
    public IAdinfoService adinfoService;
    @Autowired
    private FileSupportController fileSupportController;

    @GetMapping("/list")
//    //@RequiresPermissions("adinfo:view")
    @ApiOperation(value = "公告列表", notes = "公告列表", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "GET")
    public Map<String, Object> listAdinfo(QueryRequest request, AdinfoVo adinfo) {
        return getDataTable(this.adinfoService.listAdinfos(request, adinfo));
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("adinfo:get")
    @ApiOperation(value = "公告详情", notes = "公告详情", tags =ApiTagsConstant.TAG_BANNER, httpMethod = "GET")
    public Adinfo detail(@PathVariable Long id) {
        return this.adinfoService.findById(id);
    }

    @Log("添加公告")
    @PostMapping("/add")
    //@RequiresPermissions("adinfo:add")
    @ApiOperation(value = "添加公告", notes = "添加公告", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "POST")
    public AdinfoData addAdinfo(@RequestBody @Valid Adinfo adinfo) throws BusApiException {
        AdinfoData adinfo1 = adinfoService.createAdinfo(adinfo);
        return adinfo1;
    }

    @Log("修改公告")
    @PutMapping("/update")
    //@RequiresPermissions("adinfo:update")
    @ApiOperation(value = "修改公告", notes = "修改公告", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "PUT")
    public AdinfoData updateAdinfo(@RequestBody @Valid Adinfo adinfo) throws BusApiException{
        AdinfoData adinfoData = adinfoService.modifyAdinfo(adinfo);
        return adinfoData;
    }

    @Log("上线公告")
    @PutMapping("/updateLine")
    //@RequiresPermissions("adinfo:update")
    @ApiOperation(value = "上线公告", notes = "上线公告", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "PUT")
    public AdinfoData updateLine(@RequestBody @Valid Adinfo adinfo) throws BusApiException{
        AdinfoData adinfoData = adinfoService.modifyAdinfoLine(adinfo);
        return  adinfoData;
    }
    @Log("下线公告")
    @PutMapping("/updateTape")
    //@RequiresPermissions("adinfo:update")
    @ApiOperation(value = "下线公告", notes = "下线公告", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "PUT")
    public void updateTape(@RequestBody @Valid Adinfo adinfo) throws BusApiException{
        this.adinfoService.modifyAdinfoTape(adinfo);
    }

    @Log("删除公告")
    @DeleteMapping("/{adinfoIds}")
    //@RequiresPermissions("adinfo:delete")
    @ApiOperation(value = "删除公告", notes = "删除公告", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "DELETE")
    public void deleteAdinfo(@NotBlank(message = "{required}") @PathVariable String adinfoIds) throws BusApiException{
        String[] ids = adinfoIds.split(StringPool.COMMA);
        this.adinfoService.deleteAdinfos(ids);
    }

    @PostMapping("export")
    //@RequiresPermissions("adinfo:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "POST")
    public void exportAdinfo(QueryRequest request, @RequestBody AdinfoVo adinfo, HttpServletResponse response) throws BusApiException{
        List<Adinfo> adinfos = this.adinfoService.findList(request, adinfo);
        ExcelKit.$Export(Adinfo.class, response).downXlsx(adinfos, false);
    }

    @PostMapping("/import")
    //@RequiresPermissions("banner:add")
    @ApiOperation(value = "导入公告", notes = "导入公告", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "POST")
    public String importUser(@RequestParam(name = "file") MultipartFile file, HttpServletRequest request)
            throws Exception {
        BusApiResponse busApiResponse = fileSupportController.singleFile(file, request);
        Object data = busApiResponse.get("data");
        return "http://111.200.216.79:18082"+data;
    }

}
