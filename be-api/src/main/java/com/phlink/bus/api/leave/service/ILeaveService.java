package com.phlink.bus.api.leave.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.leave.domain.Leave;
import com.phlink.bus.api.leave.domain.vo.LeaveDetailVO;
import com.phlink.bus.api.leave.domain.vo.LeaveVO;
import com.phlink.bus.api.leave.domain.vo.StudentByDayLeaveInfoVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author zhouyi
 */
public interface ILeaveService extends IService<Leave> {


    /**
     * 获取详情
     */
    Leave findById(Long id);

    /**
     * 查询列表
     *
     * @param request
     * @param leaveVO
     * @return
     */
    IPage<LeaveDetailVO> listLeaves(QueryRequest request, LeaveVO leaveVO);

    List<LeaveDetailVO> listLeaves(LeaveVO leaveVO);

    /**
     * 新增
     *
     * @param leave
     * @throws BusApiException
     */
    Map<String, Object> createLeave(Leave leave);

    /**
     * 修改
     *
     * @param leave
     */
    void modifyLeave(Leave leave);

    /**
     * 批量删除
     *
     * @param leaveIds
     */
    void deleteLeaves(String[] leaveIds);

    /**
     * 查询学生某一天的请假列表
     * @param date
     * @return
     */
    List<StudentByDayLeaveInfoVO> listBusTimeInfo(String date, Long studentId);

    /**
     * 获取一段时间内请假的日期
     * @param dayStart
     * @param dayEnd
     * @param studentId
     * @return
     */
    List<LocalDate> listDay(String dayStart, String dayEnd, Long studentId);
}
