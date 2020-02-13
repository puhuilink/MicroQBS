package com.phlink.bus.api.im.service;

import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.im.domain.ImGroups;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.im.response.CommonResultEntity;

import java.util.List;

/**
 * @author wen
 */
public interface IImGroupsService extends IService<ImGroups> {


    /**
    * 获取详情
    */
    ImGroups findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param imGroups
    * @return
    */
    IPage<ImGroups> listImGroupss(QueryRequest request, ImGroups imGroups);

    /**
    * 新增
     * @param imGroups
     */
    CommonResultEntity createImGroups(ImGroups imGroups) throws RedisConnectException;

    /**
    * 修改
     * @param imGroups
     */
    CommonResultEntity modifyImGroups(ImGroups imGroups) throws RedisConnectException, BusApiException;

    /**
    * 批量删除
    * @param imGroupsIds
    */
    void deleteImGroupss(String[] imGroupsIds) throws RedisConnectException;

    /**
     * 设置群主
     * @param gourpId
     * @param newManagerId
     */
    CommonResultEntity updateManager(String gourpId, Long newManagerId) throws BusApiException, RedisConnectException;

    /**
     * 添加成员
     * @param groupId
     * @param ids
     */
    CommonResultEntity invite(String groupId, Long[] ids) throws RedisConnectException;

    /**
     * 移除成员
     * @param groupId
     * @param ids
     * @return
     */
    CommonResultEntity remove(String groupId, Long[] ids) throws RedisConnectException;

    /**
     * 根据departId获得群组
     * @param id
     * @return
     */
    ImGroups getByDepartId(Long id);

    /**
     * 根据部门ID列表获取群组，departId = routeOperationId
     * @param departIds
     * @return
     */
    List<ImGroups> listByDepartIds(String[] departIds);

    ImGroups getByGroupId(String groupId);

    void removeByGroupId(String[] groupIds);
}
