package com.phlink.bus.api.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.alarm.domain.BusTripTime;
import com.phlink.bus.api.bus.domain.BindBusDetailInfo;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.route.dao.StopAttendanceMapper;
import com.phlink.bus.api.route.domain.StopAttendance;
import com.phlink.bus.api.route.domain.TripStopTimeDetail;
import com.phlink.bus.api.route.domain.enums.TripRedirectEnum;
import com.phlink.bus.api.route.domain.vo.StudentAttendanceVO;
import com.phlink.bus.api.route.service.IStopAttendanceService;
import com.phlink.bus.api.route.service.ITripService;
import com.phlink.bus.api.system.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wen
 */
@Service
public class StopAttendanceServiceImpl extends ServiceImpl<StopAttendanceMapper, StopAttendance> implements IStopAttendanceService {

    @Autowired
    private StopAttendanceMapper stopAttendanceMapper;
    @Autowired
    private IBusService busService;
    @Lazy
    @Autowired
    private ITripService tripService;

    @Override
    public StopAttendance findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<StopAttendance> listStopAttendances(QueryRequest request, StopAttendance stopAttendance) {
        QueryWrapper<StopAttendance> queryWrapper = new QueryWrapper<>();
        //TODO:查询条件
//        if (stopAttendance.getCreateTimeFrom() != null){
//            queryWrapper.lambda().ge(StopAttendance::getStartTime, stopAttendance.getCreateTimeFrom());
//        }
//        if (stopAttendance.getCreateTimeTo() != null){
//            queryWrapper.lambda().le(StopAttendance::getStartTime, stopAttendance.getCreateTimeTo());
//        }
        Page<StopAttendance> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void createStopAttendance(StopAttendance stopAttendance) throws BusApiException {
		User teacherUser = BusApiUtil.getCurrentUser();
		Bus bus = busService.getBusByWorkerId(teacherUser.getUserId());
		if(bus == null) {
			throw new BusApiException("您还未绑定车辆");
		}

        String type = null;
        // 根据当前时间查询当前行程
        LocalTime now = LocalTime.now();
        BusTripTime busTripTime = baseMapper.getBusTripTimeByTime(now, stopAttendance.getStopId());
        if (busTripTime == null) {
            String message = "当前时刻没有运行的行程";
            throw new BusApiException(message);
        }
        //查询当前登录的用户和该站点是否是学校站点
        boolean flag = checkIsSchool(busTripTime.getDirectionId(), busTripTime.getTripId(), stopAttendance.getStopId());
        if (TripRedirectEnum.GO.equals(busTripTime.getDirectionId())) {
            if (flag) {
                type = "2";//下车打卡
            } else {
                type = "1";//上车打卡
            }
        } else {
            if (flag) {
                type = "1";//上车打卡
            } else {
                type = "2";//下车打卡
            }
        }
        //查询今天在该条行程上最新一条打卡记录
        StopAttendance att = stopAttendanceMapper.getTodayLastAttendanceByTripAndBus(stopAttendance.getStudentId(), stopAttendance.getTripId(), bus.getBusCode());
        if (att != null && type.equals(att.getType())) {
            String message = "该学生本次行程已经打过卡";
            throw new BusApiException(message);
        }
        stopAttendance.setTime(LocalDate.now());
        stopAttendance.setType(type);
		BindBusDetailInfo busDetailInfo = busService.getBindBusDetailInfoByBusCode(bus.getBusCode());
		if(busDetailInfo != null) {
			stopAttendance.setBusId(busDetailInfo.getId());
			stopAttendance.setBusCode(busDetailInfo.getBusCode());
			stopAttendance.setBusTeacherId(busDetailInfo.getBindBusTeacherId());
			stopAttendance.setBusTeacherName(busDetailInfo.getBusTeacherName());
			stopAttendance.setDriverId(busDetailInfo.getBindDriverId());
			stopAttendance.setDriverName(busDetailInfo.getDriverName());
			stopAttendance.setNumberPlate(busDetailInfo.getNumberPlate());
		}

        this.save(stopAttendance);
    }

    public boolean checkIsSchool(TripRedirectEnum directionId, Long tripId, Long stopId) {
        boolean flag = false;
        List<TripStopTimeDetail> tripStopTimeDetailList = tripService.listTripStopTimeDetailByTripId(tripId);
        if (tripStopTimeDetailList != null) {
			TripStopTimeDetail stopTimeDetail = null;
            if (TripRedirectEnum.GO.equals(directionId)) {
				stopTimeDetail = tripStopTimeDetailList.get(tripStopTimeDetailList.size() - 1);
            }
            if (TripRedirectEnum.BACK.equals(directionId)) {
				stopTimeDetail = tripStopTimeDetailList.get(0);
            }
			if(stopTimeDetail != null && stopId.equals(stopTimeDetail.getStopId())) {
				flag = true;
			}
        }
        return flag;
    }

    @Override
    @Transactional
    public void modifyStopAttendance(StopAttendance stopAttendance) {
        this.updateById(stopAttendance);
    }

    @Override
    public void deleteStopAttendances(String[] stopAttendanceIds) {
        List<Long> list = Stream.of(stopAttendanceIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public void studentAttendance(StudentAttendanceVO studentAttendanceVO) {
        //TODO 学生考勤
    }

    @Override
    public List<StopAttendance> listTodayAttendanceByType(Long tripId, String type) {
        QueryWrapper<StopAttendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StopAttendance::getTripId, tripId);
        queryWrapper.lambda().eq(StopAttendance::getTime, LocalDate.now());
        queryWrapper.lambda().eq(StopAttendance::getType, type);
        return list(queryWrapper);
    }

    @Override
    public void createSchoolAttendance(StopAttendance stopAttendance) throws BusApiException {
		User teacherUser = BusApiUtil.getCurrentUser();
		Bus bus = busService.getBusByWorkerId(teacherUser.getUserId());
		if(bus == null) {
			throw new BusApiException("您还未绑定车辆");
		}
		String type;
		// 根据当前时间查询当前行程
		LocalTime now = LocalTime.now();
		BusTripTime busTripTime = baseMapper.getBusTripTimeByTime(now, stopAttendance.getStopId());
		if (busTripTime == null) {
			String message = "当前时刻没有运行的行程";
			throw new BusApiException(message);
		}
		if (TripRedirectEnum.GO.equals(busTripTime.getDirectionId())) {
				type = "2";//下车打卡
		} else {
				type = "1";//上车打卡
		}
		//查询今天在该条行程上最新一条打卡记录
		StopAttendance att = stopAttendanceMapper.getTodayLastAttendanceByTripAndBus(stopAttendance.getStudentId(), stopAttendance.getTripId(), bus.getBusCode());
		if (att != null && type.equals(att.getType())) {
			String message = "该学生本次行程已经打过卡";
			throw new BusApiException(message);
		}
		stopAttendance.setTime(LocalDate.now());
		stopAttendance.setType(type);
		BindBusDetailInfo busDetailInfo = busService.getBindBusDetailInfoByBusCode(bus.getBusCode());
		if(busDetailInfo != null) {
			stopAttendance.setBusId(busDetailInfo.getId());
			stopAttendance.setBusCode(busDetailInfo.getBusCode());
			stopAttendance.setBusTeacherId(busDetailInfo.getBindBusTeacherId());
			stopAttendance.setBusTeacherName(busDetailInfo.getBusTeacherName());
			stopAttendance.setDriverId(busDetailInfo.getBindDriverId());
			stopAttendance.setDriverName(busDetailInfo.getDriverName());
			stopAttendance.setNumberPlate(busDetailInfo.getNumberPlate());
		}
		this.save(stopAttendance);
    }
}
