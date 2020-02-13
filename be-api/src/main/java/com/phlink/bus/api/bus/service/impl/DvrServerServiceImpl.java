package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.DvrServerMapper;
import com.phlink.bus.api.bus.domain.DvrServer;
import com.phlink.bus.api.bus.service.IDvrServerService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author wen
*/
@Service
public class DvrServerServiceImpl extends ServiceImpl<DvrServerMapper, DvrServer> implements IDvrServerService {


    @Override
    public DvrServer findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<DvrServer> listDvrServers(QueryRequest request, DvrServer dvrServer){
        QueryWrapper<DvrServer> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(dvrServer.getHost())) {
            queryWrapper.lambda().like(DvrServer::getHost, dvrServer.getHost());
        }
        Page<DvrServer> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort= StringUtils.isNotBlank(request.getSortOrder())?request.getSortOrder():"id";
        String order=StringUtils.isNotBlank(request.getSortField())?request.getSortField():BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createDvrServer(DvrServer dvrServer) throws BusApiException {
        DvrServer check = getByHostAndPort(dvrServer.getHost(), dvrServer.getPort());
        if(check != null) {
            throw new BusApiException("ip和端口已存在");
        }
        dvrServer.setCreateTime(LocalDateTime.now());
        dvrServer.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(dvrServer);
    }

    @Override
    public DvrServer getByHostAndPort(String host, String port) {
        LambdaQueryWrapper<DvrServer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DvrServer::getHost, host);
        queryWrapper.eq(DvrServer::getPort, port);
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void modifyDvrServer(DvrServer dvrServer) {
        dvrServer.setModifyTime(LocalDateTime.now());
        dvrServer.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(dvrServer);
    }

    @Override
    public void deleteDvrServers(String[] dvrServerIds) {
        List<Long> list = Stream.of(dvrServerIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }
}
