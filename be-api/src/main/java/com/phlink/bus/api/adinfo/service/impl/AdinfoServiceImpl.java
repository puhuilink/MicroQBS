package com.phlink.bus.api.adinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.adinfo.dao.AdinfoMapper;
import com.phlink.bus.api.adinfo.domain.Adinfo;
import com.phlink.bus.api.adinfo.domain.AdinfoData;
import com.phlink.bus.api.adinfo.domain.VO.AdinfoVo;
import com.phlink.bus.api.adinfo.service.IAdinfoService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.SortUtil;
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
public class AdinfoServiceImpl extends ServiceImpl<AdinfoMapper, Adinfo> implements IAdinfoService {

    @Autowired
    private AdinfoMapper adinfoMapper;


    @Override
    public Adinfo findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<Adinfo> listAdinfos(QueryRequest request, AdinfoVo adinfo){
        QueryWrapper<Adinfo> queryWrapper = new QueryWrapper<>();
        if(adinfo.getType()!=null){
            queryWrapper.lambda().eq(Adinfo::getType,adinfo.getType());
        }
        if(adinfo.getStatus()!=null){
            queryWrapper.lambda().eq(Adinfo::getStatus,adinfo.getStatus());
        }
        if (adinfo.getStartTime()!=null && !adinfo.getStartTime().equals("")){
            queryWrapper.lambda().ge(Adinfo::getPubDate, adinfo.getStartTime());
        }
        if (adinfo.getEndTime()!=null&& !adinfo.getStartTime().equals("")){
            queryWrapper.lambda().le(Adinfo::getPubDate, adinfo.getEndTime());
        }
        //TODO:查询条件
        Page<Adinfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        String sort= StringUtils.isNotBlank(request.getSortOrder())?request.getSortOrder():"id";
        String order=StringUtils.isNotBlank(request.getSortField())?request.getSortField():BusApiConstant.ORDER_DESC;
        SortUtil.handlePageSort(request, page, sort, order, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public AdinfoData createAdinfo(Adinfo adinfo) {
        AdinfoData adinfoData = new AdinfoData();
        if(adinfo.getStatus()==1){
            int i=adinfoMapper.getCount();
            if(i<4) {
                adinfo.setPubDate(LocalDate.now());
                this.save(adinfo);
                adinfoData.setCode(0);
                adinfoData.setMsg("上线成功");
                return adinfoData;
            }
            adinfoData.setCode(1);
            adinfoData.setMsg("超过4张");
            return adinfoData;
        }else{
        this.save(adinfo);
            adinfoData.setCode(0);
            adinfoData.setMsg("新增成功");
            return adinfoData;
    }
    }

    @Override
    @Transactional
    public AdinfoData modifyAdinfo(Adinfo adinfo) {
        AdinfoData adinfoData = new AdinfoData();
        if(adinfo.getStatus()==1){
            int i=adinfoMapper.getCount();
            if(i<4) {
                adinfo.setPubDate(LocalDate.now());
                this.updateById(adinfo);
                adinfoData.setCode(0);
                adinfoData.setMsg("修改成功");
                return adinfoData;
            }
            adinfoData.setCode(1);
            adinfoData.setMsg("超过4张");
            return adinfoData;
        }
        this.updateById(adinfo);
        adinfoData.setCode(0);
        adinfoData.setMsg("修改成功");
        return adinfoData;
    }

    @Override
    public void deleteAdinfos(String[] adinfoIds) {
        List<Long> list = Stream.of(adinfoIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    @Transactional
    public AdinfoData modifyAdinfoLine(Adinfo adinfo) {
        AdinfoData adinfoData = new AdinfoData();
        int i=adinfoMapper.getCount();
        if(i<4){
            adinfo.setStatus(1);
            adinfo.setPubDate(LocalDate.now());
            this.updateById(adinfo);
            adinfoData.setCode(0);
            adinfoData.setMsg("修改成功");
            return adinfoData;
        }
        adinfoData.setCode(1);
        adinfoData.setMsg("超过4张");
        return adinfoData;
    }

    @Override
    @Transactional
    public void modifyAdinfoTape(Adinfo adinfo) {
        adinfo.setStatus(2);
        this.updateById(adinfo);
    }

    @Override
    public List<Adinfo> findList(QueryRequest request, AdinfoVo adinfo) {

        return adinfoMapper.findList();
    }
}
