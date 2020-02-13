package com.phlink.bus.api.banner.service.impl;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.banner.dao.BannerMapper;
import com.phlink.bus.api.banner.domain.Banner;
import com.phlink.bus.api.banner.domain.BannerData;
import com.phlink.bus.api.banner.domain.VO.BannerVO;
import com.phlink.bus.api.banner.service.IBannerService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.core.response.Result;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author Maibenben
*/
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {
    @Autowired
    private BannerMapper bannerMaper;

    @Override
    public Banner findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<Banner> listBanners(QueryRequest request, BannerVO bannerVO){
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        if (bannerVO.getStartTime()!=null && !bannerVO.getStartTime().equals("")){
            queryWrapper.lambda().ge(Banner::getPubDate, bannerVO.getStartTime());
        }
        if (bannerVO.getEndTime()!=null && !bannerVO.getStartTime().equals("")){
            queryWrapper.lambda().le(Banner::getPubDate, bannerVO.getEndTime());
        }
        if(bannerVO.getStatus()!=null){
            queryWrapper.lambda().eq(Banner::getStatus,bannerVO.getStatus());
        }
        if(bannerVO.getId()!=null){
            queryWrapper.lambda().eq(Banner::getId,bannerVO.getId());
        }
        //TODO:查询条件
        Page<Banner> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort= StringUtils.isNotBlank(request.getSortOrder())?request.getSortOrder():"id";
        String order=StringUtils.isNotBlank(request.getSortField())?request.getSortField():BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.page(page, queryWrapper);
    }
    @Override
    @Transactional
    public Result createBanner(Banner banner) {
        if(banner.getStatus().equals(3)){
            this.save(banner);
            return Result.success();
        }else if(banner.getStatus().equals(1)){
           int i= bannerMaper.getBannerListCount();
           if(i<6){
               banner.setPubDate(LocalDate.now());
               banner.setSort(i+1);
               this.save(banner);
               return Result.success();
           }
            return Result.error();
        }
        return Result.error();
    }
    @Override
    @Transactional
    public Result modifyBanner(Banner banner) {
        if(banner.getStatus()==1){
            int i= bannerMaper.getBannerListCount();
            if(i<6) {
                banner.setPubDate(LocalDate.now());
                this.updateById(banner);
                return Result.success();
            }
            return Result.error(1,"超过6张");
        }
        this.updateById(banner);
        return Result.success();
    }
    @Override
    public void deleteBanners(String[] bannerIds) {
        List<Long> list = Stream.of(bannerIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    @Transactional
    public void updateSort(String banner){
        Banner banner2 = new Banner();
       List<Banner> bannerLists = JSONArray.parseArray(banner, Banner.class);
        for (int i = 0; i < bannerLists.size(); i++) {
            Banner banner1 = bannerLists.get(i);
            banner2.setSort(i);
            banner2.setId(banner1.getId());
            bannerMaper.updateSortById(banner2);
        }
    }

    @Override
    public List<Banner> getImg() {
        return bannerMaper.getimg();
    }

    @Override
    public List<Banner> findList(QueryRequest request,BannerVO banner) {
        return bannerMaper.list();
    }

    @Override
    @Transactional
    public Result modifyBannerLine(Banner banner) {
        BannerData jsonData = new BannerData();
        int i= bannerMaper.getBannerListCount();
        if(i<6){
            banner.setStatus(1);
            banner.setPubDate(LocalDate.now());
            this.updateById(banner);
            return Result.success();
        }
        return Result.error();
    }

    @Override
    @Transactional
    public void modifyBannerLineTape(Banner banner) {
        bannerMaper.modifyBannerLineTape(banner);
    }
}
