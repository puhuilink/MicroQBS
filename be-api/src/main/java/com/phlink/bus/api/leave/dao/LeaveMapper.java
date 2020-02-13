package com.phlink.bus.api.leave.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.leave.domain.Leave;
import com.phlink.bus.api.leave.domain.vo.LeaveDetailVO;
import com.phlink.bus.api.leave.domain.vo.LeaveVO;
import com.phlink.bus.api.leave.domain.vo.StudentByDayLeaveInfoVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhouyi
 */
public interface LeaveMapper extends BaseMapper<Leave> {

    IPage<LeaveDetailVO> getLeaveList(Page page, @Param("leaveVO") LeaveVO leaveVO);


    List<Leave> findIsLeaveBystudenId(@Param("studentId") Long studentId,
                                      @Param("leaveDateStart") LocalDate leaveDateStart, @Param("leaveDateEnd") LocalDate leaveDateEnd, @Param("busTime") String[] busTime);

    List<StudentByDayLeaveInfoVO> listBusTimeInfo(@Param("day") LocalDate day, @Param("studentId") Long studentId);

    List<Leave> listDay(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("studentId") Long studentId);
}
