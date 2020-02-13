package com.phlink.bus.api.leave.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.leave.dao.LeaveMapper;
import com.phlink.bus.api.leave.domain.Leave;
import com.phlink.bus.api.leave.domain.vo.LeaveDetailVO;
import com.phlink.bus.api.leave.domain.vo.LeaveVO;
import com.phlink.bus.api.leave.domain.vo.StudentByDayLeaveInfoVO;
import com.phlink.bus.api.leave.service.ILeaveService;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouyi
 */
@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements ILeaveService {

    @Autowired
    private LeaveMapper leaveMapper;

    @Autowired
    private IStudentService studentService;

    @Override
    public Leave findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<LeaveDetailVO> listLeaves(QueryRequest request, LeaveVO leaveVO) {
        Page<LeaveDetailVO> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.leaveMapper.getLeaveList(page, leaveVO);
    }

    @Override
    public List<LeaveDetailVO> listLeaves(LeaveVO leaveVO) {
        Page<LeaveDetailVO> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        this.leaveMapper.getLeaveList(page, leaveVO);
        return page.getRecords();
    }

    @Override
    @Transactional
    public Map<String, Object> createLeave(Leave leave) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        String message = null;
        boolean flag = false;
        //查询该学生是否已经请过假
        List<Leave> list = this.leaveMapper.findIsLeaveBystudenId(leave.getStudentId(),
                leave.getLeaveDateStart(), leave.getLeaveDateEnd(), leave.getBusTime());
        if (list.size() > 0) {
            message = "该学生在本段时间内已有相同班次的请假了";
            returnMap.put("message", message);
            returnMap.put("flag", flag);
            return returnMap;
        }
        //查询请假人的是否有资格请假
        Student stu = studentService.getStudentInfoById(leave.getStudentId());
        if (stu != null) {
            if (!leave.getApplyId().equals(stu.getLeaveGuardianId())) {
                message = "用户没有权限申请此学生的请假";
                returnMap.put("message", message);
                returnMap.put("flag", flag);
                return returnMap;
            }
        } else {
            message = "找不到该学生信息";
            returnMap.put("message", message);
            returnMap.put("flag", flag);
            return returnMap;
        }
        leave.setCreateTime(LocalDateTime.now());
//        leave.setApplyId(BusApiUtil.getCurrentUser().getUserId());
        this.save(leave);
        flag = true;
        returnMap.put("message", message);
        returnMap.put("flag", flag);
        return returnMap;
    }

    @Override
    @Transactional
    public void modifyLeave(Leave leave) {
        this.updateById(leave);
    }

    @Override
    public void deleteLeaves(String[] leaveIds) {
        List<Long> list = Stream.of(leaveIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public List<StudentByDayLeaveInfoVO> listBusTimeInfo(String day, Long studentId) {
        LocalDate localDate = DateUtil.stringToLocalDate(day);
        return baseMapper.listBusTimeInfo(localDate, studentId);
    }

    @Override
    public List<LocalDate> listDay(String dayStart, String dayEnd, Long studentId) {
        LocalDate start = DateUtil.stringToLocalDate(dayStart);
        LocalDate end = DateUtil.stringToLocalDate(dayEnd);
        List<Leave> leaves = baseMapper.listDay(start, end, studentId);


        List<LocalDate> days = new ArrayList<>();
        for(Leave leave : leaves) {
            List<LocalDate> allLeaveDays = allDay(leave.getLeaveDateStart(), leave.getLeaveDateEnd());
            for(LocalDate leaveDay : allLeaveDays) {
                if(leaveDay.isEqual(start) || leaveDay.isEqual(end)) {
                    days.add(leaveDay);
                }else if(leaveDay.isAfter(start) && leaveDay.isBefore(end)) {
                    days.add(leaveDay);
                }
            }

        }

        return days.stream().distinct().collect(Collectors.toList());
    }


    private List<LocalDate> allDay(LocalDate start, LocalDate end){
        List<LocalDate> allDays = new ArrayList<>();
        allDays.add(start);
        int index = 0;
        while (true) {
            LocalDate newDay = start.plusDays(index);
            allDays.add(newDay);
            if(end.isEqual(newDay)) {
                break;
            }else{
                index++;
            }
        }
        return allDays;
    }

    public static void main(String[] args) {
        String time = "1,2,3,4";
        String[] timebus = time.split(",");
        System.out.println(timebus);
    }
}
