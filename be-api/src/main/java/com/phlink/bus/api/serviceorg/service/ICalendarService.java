package com.phlink.bus.api.serviceorg.service;

import com.phlink.bus.api.serviceorg.domain.Calendar;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
/**
 * @author wen
 */
public interface ICalendarService extends IService<Calendar> {


    /**
    * 获取详情
    */
    Calendar findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param calendar
    * @return
    */
    IPage<Calendar> listCalendars(QueryRequest request, Calendar calendar);

    /**
    * 新增
    * @param calendar
    */
    void createCalendar(Calendar calendar);

    /**
    * 修改
    * @param calendar
    */
    void modifyCalendar(Calendar calendar);

    /**
    * 批量删除
    * @param calendarIds
    */
    void deleteCalendars(String[] calendarIds);
}
