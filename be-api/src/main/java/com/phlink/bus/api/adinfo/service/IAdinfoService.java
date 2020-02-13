package com.phlink.bus.api.adinfo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.adinfo.domain.Adinfo;
import com.phlink.bus.api.adinfo.domain.AdinfoData;
import com.phlink.bus.api.adinfo.domain.VO.AdinfoVo;
import com.phlink.bus.api.common.domain.QueryRequest;

import java.util.List;

/**
 * @author Maibenben
 */
public interface IAdinfoService extends IService<Adinfo> {


    /**
    * 获取详情
    */
    Adinfo findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param adinfo
    * @return
    */
    IPage<Adinfo> listAdinfos(QueryRequest request, AdinfoVo adinfo);

    /**
    * 新增
    * @param adinfo
    */
    AdinfoData createAdinfo(Adinfo adinfo);

    /**
    * 修改
    * @param adinfo
    */
    AdinfoData modifyAdinfo(Adinfo adinfo);

    /**
    * 批量删除
    * @param adinfoIds
    */
    void deleteAdinfos(String[] adinfoIds);

    AdinfoData modifyAdinfoLine(Adinfo adinfo);

    void modifyAdinfoTape(Adinfo adinfo);

    List<Adinfo> findList(QueryRequest request, AdinfoVo adinfo);
}
