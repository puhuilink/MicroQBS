package com.phlink.bus.api.bus.service;

import com.phlink.bus.api.bus.domain.MyTest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
/**
 * @author wen
 */
public interface IMyTestService extends IService<MyTest> {


    /**
    * 获取详情
    */
    MyTest findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param myTest
    * @return
    */
    IPage<MyTest> listMyTests(QueryRequest request, MyTest myTest);

    /**
    * 新增
    * @param myTest
    */
    void createMyTest(MyTest myTest);

    /**
    * 修改
    * @param myTest
    */
    void modifyMyTest(MyTest myTest);

    /**
    * 批量删除
    * @param myTestIds
    */
    void deleteMyTestIds(String[] myTestIds);
}
