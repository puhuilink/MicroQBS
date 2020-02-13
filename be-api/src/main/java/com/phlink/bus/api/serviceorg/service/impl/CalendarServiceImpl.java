package com.phlink.bus.api.serviceorg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.serviceorg.dao.CalendarMapper;
import com.phlink.bus.api.serviceorg.domain.Calendar;
import com.phlink.bus.api.serviceorg.service.ICalendarService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
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
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CalendarServiceImpl extends ServiceImpl<CalendarMapper, Calendar> implements ICalendarService {

    @Override
    public Calendar findById(Long id){
        return this.getById(id);
    }

    @Override
    public IPage<Calendar> listCalendars(QueryRequest request, Calendar calendar){
        QueryWrapper<Calendar> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
        if (calendar.getStartDateFrom() != null){
            queryWrapper.lambda().ge(Calendar::getStartDate, calendar.getStartDateFrom());
        }
        if (calendar.getStartDateTo() != null){
            queryWrapper.lambda().le(Calendar::getStartDate, calendar.getStartDateTo());
        }
        Page<Calendar> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createCalendar(Calendar calendar) {
        this.save(calendar);
    }

    @Override
    @Transactional
    public void modifyCalendar(Calendar calendar) {
        this.updateById(calendar);
    }

    @Override
    public void deleteCalendars(String[] calendarIds) {
        List<Long> list = Stream.of(calendarIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }
}
