package com.phlink.bus.api.banner.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.banner.domain.Banner;
import com.phlink.bus.api.banner.domain.VO.BannerVO;
import com.phlink.bus.api.banner.service.IBannerService;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.system.controller.FileSupportController;
import com.phlink.bus.core.response.Result;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/banner")
@Api(tags = ApiTagsConstant.TAG_BANNER)
public class BannerController extends BaseController {
    @Autowired
    public IBannerService bannerService;
    @Autowired
    private FileSupportController fileSupportController;

    @GetMapping("/list")
//    //@RequiresPermissions("banner:view")
    @ApiOperation(value = "轮播图列表", notes = "轮播图列表", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "GET")
    public Map<String, Object> listBanner(QueryRequest request, BannerVO bannerVO) {
        return getDataTable(this.bannerService.listBanners(request, bannerVO));
    }

    //查询上线轮播图
    @GetMapping("/getImg")
//    //@RequiresPermissions("banner:view")
    @ApiOperation(value = "查询轮播图", notes = "查询轮播图", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "GET")
    public List<Banner> getImg() {
        return bannerService.getImg();
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("banner:get")
    @ApiOperation(value = "轮播图详情", notes = "轮播图详情", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "GET")
    public Banner detail(@PathVariable Long id) {
        return this.bannerService.findById(id);
    }

    @Log("添加轮播图")
    @PostMapping("/add")
    //@RequiresPermissions("banner:add")
    @ApiOperation(value = "添加轮播图", notes = "添加轮播图", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "POST")
    public Result addBanner(@RequestBody @Valid Banner banner) throws BusApiException {
        return this.bannerService.createBanner(banner);
    }

    @Log("修改轮播图")
    @PutMapping("/update")
    //@RequiresPermissions("banner:update")
    @ApiOperation(value = "修改轮播图", notes = "修改轮播图", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "PUT")
    public Result updateBanner(@RequestBody @Valid Banner banner) throws BusApiException {
       return bannerService.modifyBanner(banner);
    }
    @Log("上线轮播图")
    @PutMapping("/updateLine")
    //@RequiresPermissions("banner:update")
    @ApiOperation(value = "上线轮播图", notes = "上线轮播图", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "PUT")
    public Result updateBannerLine(@RequestBody @Valid Banner banner) throws BusApiException {
        return bannerService.modifyBannerLine(banner);
    }
    @Log("下线轮播图")
    @PutMapping("/updateTape")
    //@RequiresPermissions("banner:update")
    @ApiOperation(value = "下线轮播图", notes = "下线轮播图", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "PUT")
    public void updateBannerLineTape(@RequestBody @Valid Banner banner) throws BusApiException {
        this.bannerService.modifyBannerLineTape(banner);
    }

    @Log("删除轮播图")
    @DeleteMapping("/{bannerIds}")
    //@RequiresPermissions("banner:delete")
    @ApiOperation(value = "删除轮播图", notes = "删除轮播图", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "DELETE")
    public void deleteBanner(@NotBlank(message = "{required}") @PathVariable String bannerIds) throws BusApiException {
        String[] ids = bannerIds.split(StringPool.COMMA);
        this.bannerService.deleteBanners(ids);
    }

        @PostMapping("/export")
    //@RequiresPermissions("banner:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "POST")
    public void exportBanner(QueryRequest request, @RequestBody BannerVO banner, HttpServletResponse response) throws BusApiException {
        try {
            List<Banner> banners = this.bannerService.findList(request, banner);
            ExcelKit.$Export(Banner.class, response).downXlsx(banners, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
    //拖动轮播图
    @Log("修改轮播图顺序")
    @PutMapping("/updateSort")
    //@RequiresPermissions("banner:update")
    @ApiOperation(value = "修改轮播图", notes = "修改轮播图", tags = ApiTagsConstant.TAG_BANNER, httpMethod = "PUT")
    public void updateSort(@RequestBody @Valid String banner) throws JSONException {
      bannerService.updateSort(banner);
    }
}