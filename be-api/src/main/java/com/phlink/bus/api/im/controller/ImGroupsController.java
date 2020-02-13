package com.phlink.bus.api.im.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.domain.enums.GroupTypeEnum;
import com.phlink.bus.api.im.response.CommonResultEntity;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.im.service.IImService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Map;


/**
 * @author wen
 */
@Slf4j
@RestController
@Validated
@RequestMapping("/im-groups")
@Api(tags = "群组管理")
public class ImGroupsController extends BaseController {

    @Autowired
    public IImGroupsService imGroupsService;

    @Autowired
    public IImService imService;

    @GetMapping
//    //@RequiresPermissions("imGroups:view")
    @ApiOperation(value = "列表", notes = "列表", tags = "群组管理", httpMethod = "GET")
    public Map<String, Object> listImGroups(QueryRequest request, ImGroups imGroups) {
        return getDataTable(this.imGroupsService.listImGroupss(request, imGroups));
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("imGroups:get")
    @ApiOperation(value = "详情", notes = "详情", tags = "群组管理", httpMethod = "GET")
    public ImGroups detail(@PathVariable Long id) {
        return this.imGroupsService.findById(id);
    }

    @Log("创建自定义群")
    @Validated(value = {OnAdd.class})
    @PostMapping("/customize")
//    //@RequiresPermissions("imGroups:add")
    @ApiOperation(value = "创建自定义群", notes = "创建自定义群", tags = "群组管理", httpMethod = "POST")
    public void addImGroups(HttpServletRequest req, @RequestBody @Valid ImGroups imGroups) throws BusApiException, RedisConnectException {
        String onlyLogin = req.getHeader(BusApiConstant.UNIQUE_LOGIN);
        if(StringUtils.isNotBlank(onlyLogin)) {
            // app 添加成员不调IM
            imGroups.setCreateTime(LocalDateTime.now());
            imGroupsService.save(imGroups);
        }else{
            imGroups.setType(GroupTypeEnum.CUSTOMIZE);
            this.imGroupsService.createImGroups(imGroups);
        }
    }

    @Log("修改群名称")
    @PutMapping
    @Validated(value = {OnUpdate.class})
//    //@RequiresPermissions("imGroups:update")
    @ApiOperation(value = "修改群名称，同时指定群主也可以修改", notes = "修改群名称", tags = "群组管理", httpMethod = "PUT")
    public CommonResultEntity updateImGroups(HttpServletRequest req, @RequestBody @Valid ImGroups imGroups) throws BusApiException, RedisConnectException {

        CommonResultEntity resultEntity = new CommonResultEntity();
        String onlyLogin = req.getHeader(BusApiConstant.UNIQUE_LOGIN);
        if(StringUtils.isNotBlank(onlyLogin)) {
            // app 添加成员不调IM
            ImGroups groups = imGroupsService.getByGroupId(imGroups.getGroupId());
            if(groups != null) {
                imGroups.setId(groups.getId());
                imGroups.setModifyTime(LocalDateTime.now());
                imGroups.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
                imGroupsService.updateById(imGroups);
            }
            resultEntity.setCode("10000");
            resultEntity.setMsg("success");
        }else{
            ImGroups groups = imGroupsService.getById(imGroups.getId());
            if(groups != null) {
                resultEntity = imGroupsService.modifyImGroups(imGroups);
            }
//            resultEntity = this.imGroupsService.modifyImGroups(imGroups);
        }
        return resultEntity;
    }

    @Log("删除群组")
    @DeleteMapping("/{imGroupsIds}")
//    //@RequiresPermissions("imGroups:delete")
    @ApiOperation(value = "删除群组", notes = "删除群组", tags = "群组管理", httpMethod = "DELETE")
    public void deleteImGroups(HttpServletRequest req, @NotBlank(message = "{required}") @PathVariable String imGroupsIds) throws BusApiException, RedisConnectException {
        String onlyLogin = req.getHeader(BusApiConstant.UNIQUE_LOGIN);
        // app 添加成员不调IM
        String[] groupIds = imGroupsIds.split(StringPool.COMMA);
        if(StringUtils.isNotBlank(onlyLogin)) {
            // groupId
            imGroupsService.removeByGroupId(groupIds);
        }else{
            // id
            this.imGroupsService.deleteImGroupss(groupIds);
        }

    }

    @Log("指定群主")
    @PutMapping("/{groupId}/manager/{newManagerId}")
//    //@RequiresPermissions("imGroups:update")
    @ApiOperation(value = "指定群主", notes = "指定群主", tags = "群组管理", httpMethod = "PUT")
    public void updateManager(HttpServletRequest req, @PathVariable String groupId, @PathVariable Long newManagerId) throws BusApiException, RedisConnectException {
        String onlyLogin = req.getHeader(BusApiConstant.UNIQUE_LOGIN);
        ImGroups groups = imGroupsService.getByGroupId(groupId);
        if(StringUtils.isNotBlank(onlyLogin)) {
            // app 添加成员不调IM
            if(groups != null) {
                groups.setManagerId(newManagerId);
                groups.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
                groups.setModifyTime(LocalDateTime.now());
                imGroupsService.saveOrUpdate(groups);
            }
        }else{
            if(groups == null) {
                throw new BusApiException("群组不存在");
            }
            if(!ArrayUtils.contains(groups.getMemberIds(), newManagerId)) {
                throw new BusApiException("群主不在群成员内，修改失败");
            }
            groups.setManagerId(newManagerId);
            groups.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
            groups.setModifyTime(LocalDateTime.now());
            imGroupsService.saveOrUpdate(groups);
            imService.transferGroupManager(groups.getGroupId(), String.valueOf(newManagerId));
        }
    }

    @Log("添加群成员")
    @PostMapping("/{groupId}/invite")
//    //@RequiresPermissions("imGroups:update")
    @ApiOperation(value = "添加群成员", notes = "添加群成员", tags = "群组管理", httpMethod = "POST")
    public CommonResultEntity invite(HttpServletRequest req, @PathVariable String groupId, @RequestBody Long[] userIds) throws BusApiException, RedisConnectException {
        CommonResultEntity resultEntity = new CommonResultEntity();
        String onlyLogin = req.getHeader(BusApiConstant.UNIQUE_LOGIN);
        ImGroups groups = imGroupsService.getByGroupId(groupId);
        if(StringUtils.isNotBlank(onlyLogin)) {
            // app 添加成员不调IM
            if(groups != null) {
                groups.addMembers(userIds);
                imGroupsService.saveOrUpdate(groups);
            }
            resultEntity.setCode("10000");
            resultEntity.setMsg("success");
        }else{
            if(groups == null) {
                throw new BusApiException("群组不存在");
            }
            resultEntity = this.imGroupsService.invite(groupId, userIds);
        }

        return resultEntity;
    }

    @Log("移除群成员")
    @PostMapping("/{groupId}/remove")
//    //@RequiresPermissions("imGroups:update")
    @ApiOperation(value = "移除群成员", notes = "移除群成员", tags = "群组管理", httpMethod = "POST")
    public CommonResultEntity remove(HttpServletRequest req, @PathVariable String groupId, @RequestBody Long[] userIds) throws BusApiException, RedisConnectException {
        CommonResultEntity resultEntity = new CommonResultEntity();
        String onlyLogin = req.getHeader(BusApiConstant.UNIQUE_LOGIN);
        ImGroups groups = imGroupsService.getByGroupId(groupId);
        if(StringUtils.isNotBlank(onlyLogin)) {
            // app 添加成员不调IM
            if(groups != null) {
                groups.removeMembers(userIds);
                imGroupsService.saveOrUpdate(groups);
            }
            resultEntity.setCode("10000");
            resultEntity.setMsg("success");
        }else{
            if(groups == null) {
                throw new BusApiException("群组不存在");
            }
            resultEntity = this.imGroupsService.remove(groupId, userIds);
        }
        return resultEntity;
    }
}
