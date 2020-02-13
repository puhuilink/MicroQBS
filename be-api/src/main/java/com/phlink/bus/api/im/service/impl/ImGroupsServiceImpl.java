package com.phlink.bus.api.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.im.dao.ImGroupsMapper;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.domain.enums.GroupTypeEnum;
import com.phlink.bus.api.im.response.CommonResultEntity;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.im.service.IImService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author wen
*/
@Slf4j
@Service
public class ImGroupsServiceImpl extends ServiceImpl<ImGroupsMapper, ImGroups> implements IImGroupsService {
    @Lazy
    @Autowired
    private IImService imService;

    @Override
    public ImGroups findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<ImGroups> listImGroupss(QueryRequest request, ImGroups imGroups){
        Page<ImGroups> page = new Page<>(request.getPageNum(), request.getPageSize());
        return baseMapper.list(page, imGroups);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public CommonResultEntity createImGroups(ImGroups imGroups) throws RedisConnectException {
        imGroups.setCreateTime(LocalDateTime.now());
        imGroups.setGroupId(IdWorker.getIdStr());
//        imGroups.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(imGroups);
        if(GroupTypeEnum.CUSTOMIZE.equals(imGroups.getType())) {
            return imService.addGroup(imGroups);
        }
        return null;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public CommonResultEntity modifyImGroups(ImGroups imGroups) throws RedisConnectException, BusApiException {
        CommonResultEntity entity = null;
        ImGroups oldImgroups = baseMapper.selectById(imGroups.getId());
        if(oldImgroups != null) {
            if(imGroups.getManagerId() != null && !oldImgroups.getManagerId().equals(imGroups.getManagerId())) {
                if(!ArrayUtils.contains(oldImgroups.getMemberIds(), imGroups.getManagerId())) {
                    throw new BusApiException("群主不在群成员内，修改失败");
                }

                oldImgroups.setManagerId(imGroups.getManagerId());
                entity = imService.transferGroupManager(oldImgroups.getGroupId(), String.valueOf(oldImgroups.getManagerId()));
            }
            if(!oldImgroups.getName().equals(imGroups.getName())) {
                oldImgroups.setName(imGroups.getName());
                entity = imService.updateGroupName(oldImgroups.getGroupId(), oldImgroups.getName());
            }
            oldImgroups.setModifyTime(LocalDateTime.now());
            oldImgroups.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
            this.updateById(oldImgroups);
        }
        return entity;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteImGroupss(String[] imGroupsIds) {
        for(int i=0;i<imGroupsIds.length;i++) {
            try {
                Long id = Long.parseLong(imGroupsIds[i]);
                ImGroups groups = baseMapper.selectById(id);
                CommonResultEntity commonResultEntity = imService.deleteGroup(groups.getGroupId());
                log.info("[imgroup transferGroupManager]解散群，同步 result[{}]", JSON.toJSONString(commonResultEntity));
                if("10000".equals(commonResultEntity.getCode())) {
                    baseMapper.deleteById(id);
                }else {
                    log.error("[imgroup deleteImGroupss]解散群失败 result[{}]", JSON.toJSONString(commonResultEntity));
                }
            } catch (Exception e) {
                log.error("解散群组失败 id = {}", imGroupsIds[i]);
            }
        }
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public CommonResultEntity updateManager(String groupId, Long newManagerId) throws BusApiException, RedisConnectException {
        CommonResultEntity commonResultEntity = imService.transferGroupManager(String.valueOf(groupId), String.valueOf(newManagerId));
        log.info("[imgroup transferGroupManager]修改群主{}，同步 result[{}]", newManagerId, JSON.toJSONString(commonResultEntity));
        if("10000".equals(commonResultEntity.getCode())) {
            try {
                ImGroups groups = getByGroupId(groupId);
                if(groups != null) {
                    groups.setManagerId(newManagerId);
                    groups.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
                    groups.setModifyTime(LocalDateTime.now());
                    saveOrUpdate(groups);
                }
            }catch (Exception e) {
                log.error("{}非系统管理群组", groupId);
            }
        }else {
            log.error("[imgroup transferGroupManager]修改群主{}失败 result[{}]", newManagerId, JSON.toJSONString(commonResultEntity));
        }
        return commonResultEntity;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public CommonResultEntity invite(String groupId, Long[] ids) throws RedisConnectException {
        // 保存到im服务器
        CommonResultEntity commonResultEntity = imService.invite(groupId, ids);
        log.info("[imgroup invite]邀请用户，同步群组成员 result[{}]", JSON.toJSONString(commonResultEntity));
        try {
            if("10000".equals(commonResultEntity.getCode()) || "10606".equals(commonResultEntity.getCode())) {
                ImGroups groups = getByGroupId(groupId);
                if(groups == null) {
                    Long gid = Long.parseLong(groupId);
                    groups = findById(gid);
                }
                if(groups != null) {
                    groups.addMembers(ids);
                    saveOrUpdate(groups);
                }
            }else {
                log.error("[imgroup invite]邀请用户失败 result[{}]", JSON.toJSONString(commonResultEntity));
            }
        }catch (Exception e) {
            log.error("[imgroup invite] " + groupId + " 非系统管理群组");
        }
        return commonResultEntity;
    }

    @Override
    public CommonResultEntity remove(String groupId, Long[] ids) throws RedisConnectException {
        // 保存到im服务器
        CommonResultEntity commonResultEntity = imService.remove(groupId, ids);
        log.info("[imgroup remove]移除用户，同步群组成员 result[{}]", JSON.toJSONString(commonResultEntity));
        try {
            if("10000".equals(commonResultEntity.getCode())) {
                ImGroups groups = getByGroupId(groupId);
                if(groups != null) {
                    groups.removeMembers(ids);
                    saveOrUpdate(groups);
                }
            }else{
                log.error("[imgroup remove]移除用户失败 result[{}]", JSON.toJSONString(commonResultEntity));
            }
        }catch (Exception e) {
            log.error("[imgroup remove]" + groupId + " 非系统管理群组");
        }
        return commonResultEntity;
    }

    @Override
    public ImGroups getByDepartId(Long deptId) {
        LambdaQueryWrapper<ImGroups> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImGroups::getDepartId, String.valueOf(deptId));
        queryWrapper.eq(ImGroups::getDeleted, false);
        return getOne(queryWrapper);
    }

    @Override
    public List<ImGroups> listByDepartIds(String[] departIds) {
        LambdaQueryWrapper<ImGroups> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ImGroups::getDepartId, departIds);
        queryWrapper.eq(ImGroups::getDeleted, false);
        return list(queryWrapper);
    }

    @Override
    public ImGroups getByGroupId(String groupId) {
        LambdaQueryWrapper<ImGroups> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImGroups::getGroupId, groupId);
        return getOne(queryWrapper);
    }

    @Override
    public void removeByGroupId(String[] groupIds) {
        if(groupIds == null || groupIds.length == 0) {
            return;
        }
        baseMapper.removeByGroupId(groupIds);
    }
}
