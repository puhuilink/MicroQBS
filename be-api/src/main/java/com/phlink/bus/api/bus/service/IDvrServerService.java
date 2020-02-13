package com.phlink.bus.api.bus.service;

import com.phlink.bus.api.bus.domain.DvrServer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;

/**
 * @author wen
 */
public interface IDvrServerService extends IService<DvrServer> {


    /**
    * 获取详情
    */
    DvrServer findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param dvrServer
    * @return
    */
    IPage<DvrServer> listDvrServers(QueryRequest request, DvrServer dvrServer);

    /**
    * 新增
    * @param dvrServer
    */
    void createDvrServer(DvrServer dvrServer) throws BusApiException;

    DvrServer getByHostAndPort(String host, String port);

    /**
    * 修改
    * @param dvrServer
    */
    void modifyDvrServer(DvrServer dvrServer);

    /**
    * 批量删除
    * @param dvrServerIds
    */
    void deleteDvrServers(String[] dvrServerIds);
}
