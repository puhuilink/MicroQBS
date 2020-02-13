package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.bus.dao.MyTestMapper;
import com.phlink.bus.api.bus.domain.MyTest;
import com.phlink.bus.api.bus.service.IMyTestService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author wen
*/
@Service
    public class MyTestServiceImpl extends ServiceImpl<MyTestMapper, MyTest> implements IMyTestService {


    @Override
    public MyTest findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<MyTest> listMyTests(QueryRequest request, MyTest myTest){
        QueryWrapper<MyTest> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        if (myTest.getCreateTimeFrom()!=null){
            queryWrapper.lambda().ge(MyTest::getCreateTime, myTest.getCreateTimeFrom());
        }
        if (myTest.getCreateTimeTo()!=null ){
            queryWrapper.lambda().le(MyTest::getCreateTime, myTest.getCreateTimeTo());
        }
        Page<MyTest> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createMyTest(MyTest myTest) {
        myTest.setCreateTime(LocalDateTime.now());
        myTest.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(myTest);
    }

    @Override
    @Transactional
    public void modifyMyTest(MyTest myTest) {
        myTest.setModifyTime(LocalDateTime.now());
        myTest.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(myTest);
    }

    @Override
    public void deleteMyTestIds(String[] myTestIds) {
        List<Long> list = Stream.of(myTestIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
        }
    }
