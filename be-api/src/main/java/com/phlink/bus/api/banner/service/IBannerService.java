package com.phlink.bus.api.banner.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.banner.domain.Banner;
import com.phlink.bus.api.banner.domain.VO.BannerVO;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.core.response.Result;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;

/**
 * @author Maibenben
 */
public interface IBannerService extends IService<Banner> {


    /**
    * 获取详情
    */
    Banner findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param banner
    * @return
    */
    IPage<Banner> listBanners(QueryRequest request, BannerVO bannerVO);

    /**
    * 新增
     * @param banner
     * @return
     */
    Result createBanner(Banner banner);

    /**
    * 修改
    * @param banner
    */
    Result modifyBanner(Banner banner);

    /**
    * 批量删除
    * @param bannerIds
    */
    void deleteBanners(String[] bannerIds);
   //拖动轮播图
    void updateSort(String banner) throws JSONException;
    //查询上线轮播图
    List<Banner> getImg();

    List<Banner> findList(QueryRequest request,BannerVO banner);

    Result modifyBannerLine(Banner banner);

    void modifyBannerLineTape(Banner banner);
}
