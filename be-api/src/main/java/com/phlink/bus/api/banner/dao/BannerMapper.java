package com.phlink.bus.api.banner.dao;

import com.phlink.bus.api.banner.domain.Banner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author Maibenben
 */
public interface BannerMapper extends BaseMapper<Banner> {

    int getBannerListCount();

    List<Banner> list();

    List<Banner> getimg();

    void updateSortById(Banner banner2);

    void modifyBannerLineTape(Banner banner);
}
