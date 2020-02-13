package com.phlink.bus.api.route.service;

import com.phlink.bus.api.route.domain.StopAttendance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.domain.vo.StudentAttendanceVO;

import java.util.List;

/**
 * @author wen
 */
public interface IStopAttendanceService extends IService<StopAttendance> {


    /**
    * 获取详情
    */
    StopAttendance findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param stopAttendance
    * @return
    */
    IPage<StopAttendance> listStopAttendances(QueryRequest request, StopAttendance stopAttendance);

    /**
    * 新增
    * @param stopAttendance
     * @throws BusApiException 
    */
    void createStopAttendance(StopAttendance stopAttendance) throws BusApiException;
    
    
    /**
    * 新增
    * @param stopAttendance
     * @throws BusApiException 
    */
    void createSchoolAttendance(StopAttendance stopAttendance) throws BusApiException;

    /**
    * 修改
    * @param stopAttendance
    */
    void modifyStopAttendance(StopAttendance stopAttendance);

    /**
    * 批量删除
    * @param stopAttendanceIds
    */
    void deleteStopAttendances(String[] stopAttendanceIds);

    /**
     * 学生使用打卡机打卡
     * @param studentAttendanceVO
     */
    void studentAttendance(StudentAttendanceVO studentAttendanceVO);

    List<StopAttendance> listTodayAttendanceByType(Long tripId, String type);
}
