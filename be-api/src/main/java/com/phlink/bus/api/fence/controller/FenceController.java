package com.phlink.bus.api.fence.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.fence.domain.Fence;
import com.phlink.bus.api.fence.domain.FenceVO;
import com.phlink.bus.api.fence.domain.FenceViewVO;
import com.phlink.bus.api.fence.service.IFenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;


/**
 * @author zhouyi
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/fence")
@Api(tags = ApiTagsConstant.TAG_FENCE)
public class FenceController extends BaseController {

    @Autowired
    public IFenceService fenceService;

    @GetMapping("/view")
    //@RequiresPermissions("fence:view")
    @ApiOperation(value = "围栏列表", notes = "围栏列表", tags = ApiTagsConstant.TAG_FENCE, httpMethod = "GET")
    public Map<String, Object> listFence(QueryRequest request, FenceViewVO fenceViewVO) {
        return getDataTable(this.fenceService.listFences(request, fenceViewVO));
    }

    @GetMapping("/detail")
    //@RequiresPermissions("fence:get")
    @ApiOperation(value = "根据ID获取详情", notes = "根据ID获取详情", tags = ApiTagsConstant.TAG_FENCE, httpMethod = "GET")
    public FenceVO detail(@NotNull(message = "{required}") @RequestParam("id") Long id) throws BusApiException {
        return this.fenceService.findById(id);
    }

    @Log("添加电子围栏")
    @Validated(value = {OnAdd.class})
    @PostMapping("/add")
    //@RequiresPermissions("fence:add")
    @ApiOperation(value = "添加电子围栏", notes = "添加电子围栏", tags = ApiTagsConstant.TAG_FENCE, httpMethod = "POST")
    public Fence addFence(@RequestBody @Valid FenceVO fenceVO) throws BusApiException {
//        if (this.fenceService.checkFenceByRelation(fenceVO.getRelationId(), fenceVO.getRelationType())) {
//            throw new BusApiException("该类型围栏已存在!");
//        }
        if (this.fenceService.getByName(fenceVO.getFenceName()) != null) {
            throw new BusApiException("围栏" + fenceVO.getFenceName() + "已存在!");
        }
        return this.fenceService.createFence(fenceVO);
    }

    @GetMapping("/by-name/{name}")
    @ApiOperation(value = "根据围栏名字获得围栏", notes = "根据围栏名字获得围栏", tags = ApiTagsConstant.TAG_FENCE, httpMethod = "POST")
    public Fence checkFenceName(@PathVariable String name) {
        return this.fenceService.getByName(name);
    }

    @Log("修改电子围栏")
    @Validated(value = {OnUpdate.class})
    @PutMapping("/update")
    //@RequiresPermissions("fence:update")
    @ApiOperation(value = "修改电子围栏", notes = "修改电子围栏", tags = ApiTagsConstant.TAG_FENCE, httpMethod = "PUT")
    public void updateFence(@RequestBody @Valid FenceVO fenceVO) throws BusApiException {
        this.fenceService.modifyFence(fenceVO);
    }

    @Log("删除电子围栏")
    @DeleteMapping("/{ids}")
    //@RequiresPermissions("fence:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = ApiTagsConstant.TAG_FENCE, httpMethod = "DELETE")
    public void deleteFence(@NotBlank(message = "{required}") @PathVariable String ids) {
        String[] fids = ids.split(StringPool.COMMA);
        this.fenceService.deleteFences(fids);
    }

}
