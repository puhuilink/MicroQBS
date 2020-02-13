package com.phlink.bus.api.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.alarm.domain.BusTripTime;
import com.phlink.bus.api.bus.dao.BusMapper;
import com.phlink.bus.api.bus.domain.*;
import com.phlink.bus.api.bus.domain.VO.*;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.bus.service.IDvrCameraConfigService;
import com.phlink.bus.api.bus.service.IDvrService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.device.domain.VO.BusDetailLocationVO;
import com.phlink.bus.api.map.domain.enums.MapTypeEnum;
import com.phlink.bus.api.map.service.IMapAmapService;
import com.phlink.bus.api.map.service.IMapBaiduService;
import com.phlink.bus.api.notify.event.BusCreateEvent;
import com.phlink.bus.api.serviceorg.dao.StudentMapper;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.domain.VO.StudentViewVO;
import com.phlink.bus.api.serviceorg.domain.enums.ServiceStatusEnum;
import com.phlink.bus.api.system.domain.Dept;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wen
 */
@Slf4j
@Service
public class BusServiceImpl extends ServiceImpl<BusMapper, Bus> implements IBusService {

    @Autowired
    private IMapAmapService mapAmapService;
    @Autowired
    private IMapBaiduService mapBaiduService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private IDvrService dvrService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private IDvrCameraConfigService dvrCameraConfigService;
    
    @Override
    public Page<Bus> listBus(QueryRequest request, BusViewVO busViewVO) {
        Page<Bus> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", BusApiConstant.ORDER_DESC, true);
        return this.baseMapper.listBus(page, busViewVO);
    }

    @Override
    public List<Bus> listBus(BusViewVO busViewVO) {
        Page<Bus> page = new Page<>();
        page.setSearchCount(false);
        page.setSize(-1);
        page.setDesc("id");
        this.baseMapper.listBus(page, busViewVO);
        return page.getRecords();
    }

    @Override
    public Bus findById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createBus(Bus bus) throws BusApiException {
        Dept dept = deptService.getById(bus.getDeptId());
        if(dept == null) {
            throw new BusApiException("部门不存在");
        }

        LambdaQueryWrapper<Bus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bus::getBusCode, bus.getBusCode());
        int checkBusCode = baseMapper.selectCount(queryWrapper);
        if(checkBusCode >= 1) {
            throw new BusApiException("车架号已存在");
        }
        if(StringUtils.isNotBlank(bus.getNumberPlate())) {
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Bus::getNumberPlate, bus.getNumberPlate());
            int checkNumberPlate = baseMapper.selectCount(queryWrapper);
            if(checkNumberPlate >= 1) {
                throw new BusApiException("车牌号已存在");
            }
        }
        bus.setCreateTime(LocalDateTime.now());
        bus.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(bus);
        saveOrUpdateDvr(bus);
        context.publishEvent(new BusCreateEvent(this, bus));
    }

    private void saveOrUpdateDvr(Bus bus) throws BusApiException {
        // 删除之前的DVR配置
        UpdateWrapper<Dvr> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Dvr::getBusId, null);
        wrapper.lambda().eq(Dvr::getBusId, bus.getId());
        dvrService.update(wrapper);
        // 创建或者修改DVR
        if (StringUtils.isNotBlank(bus.getDvrCode())) {
            Dvr dvr = dvrService.getByDvrCode(bus.getDvrCode());
            if (dvr == null) {
                dvr = new Dvr();
                dvr.setBusId(bus.getId());
                dvr.setDvrCode(bus.getDvrCode());
                if(bus.getChannelNumber() == null) {
                    dvr.setChannelNumber(8);
                }else{
                    dvr.setChannelNumber(bus.getChannelNumber());
                }
                dvr.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
                dvr.setCreateTime(LocalDateTime.now());
            } else {
                dvr.setBusId(bus.getId());
                // 检查有没有配置过摄像头
                List<DvrBusLocationInfoVO> vos = dvrCameraConfigService.listDvrBusLocationInfo(bus.getId());
                if(vos != null && bus.getChannelNumber() != null && vos.size() > bus.getChannelNumber()) {
                    throw new BusApiException("现在已配置的位置超过通道数量，请先删除没用的位置");
                }
                if(bus.getChannelNumber() == null) {
                    dvr.setChannelNumber(8);
                }else{
                    dvr.setChannelNumber(bus.getChannelNumber());
                }
            }
            dvrService.saveOrUpdate(dvr);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void modifyBus(Bus bus) throws BusApiException {
        Dept dept = deptService.getById(bus.getDeptId());
        if(dept == null) {
            throw new BusApiException("部门不存在");
        }
        LambdaQueryWrapper<Bus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bus::getBusCode, bus.getBusCode());
        List<Bus> checkBusCodes = baseMapper.selectList(queryWrapper);
        for (Bus b : checkBusCodes) {
            if(!b.getId().equals(bus.getId())) {
                throw new BusApiException("车架号已存在");
            }
        }

        if(StringUtils.isNotBlank(bus.getNumberPlate())) {
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Bus::getNumberPlate, bus.getNumberPlate());

            List<Bus> checkNumberPlates = baseMapper.selectList(queryWrapper);
            for (Bus b : checkNumberPlates) {
                if(!b.getId().equals(bus.getId())) {
                    throw new BusApiException("车牌号已存在");
                }
            }
        }
        bus.setModifyTime(LocalDateTime.now());
        bus.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(bus);
        saveOrUpdateDvr(bus);
    }

    @Override
    public void deleteBusIds(String[] busIds) {
        List<Long> list = Stream.of(busIds).map(Long::parseLong).collect(Collectors.toList());
        Collection<Bus> busList = listByIds(list);
        for (Bus bus : busList) {
            if (bus.getTid() != 0L) {
                mapAmapService.deleteAmapEntity(bus.getTid());
            }
            if (StringUtils.isNotBlank(bus.getEntityName())) {
                mapBaiduService.deleteBaiduEntitys(bus.getEntityName());
            }
        }
        removeByIds(list);
    }

    @Override
    public void updateBusTerminalId(String busCode, MapTypeEnum mapType, long terminalId)
            throws BusApiException {
        switch (mapType) {
            case AMAP:
                this.baseMapper.updateBusAmapTerminalId(busCode, terminalId);
            case BAIDU:
                this.baseMapper.updateBusBaiduTerminalId(busCode);
        }
    }

    @Override
    public List<Bus> unbindBusList(Bus bus) {
        return baseMapper.unbindRouteBusList(bus);
    }

    @Override
    public List<User> unbindDriverList(Driver driver) {
        return baseMapper.unbindRouteDriverList(driver, BusApiConstant.ROLE_DRIVER);
    }

    @Override
    public List<User> unbindBusTeacherList(BusTeacher busTeacher) {
        return baseMapper.unbindRouteBusTeacherList(busTeacher, BusApiConstant.ROLE_BUS_TEACHER);
    }

    @Override
    public void batchCreateBus(List<Bus> list) {
        for (Bus importBus : list) {

            Dept dept = deptService.getByDeptName(importBus.getDeptName());
            importBus.setDeptId(dept.getDeptId());

            Bus bus = getByBusCode(importBus.getBusCode());
            if(bus == null) {
                importBus.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
                importBus.setCreateTime(LocalDateTime.now());
                try {
                    createBus(importBus);
                } catch (BusApiException e) {
                    e.printStackTrace();
                }
            }else{
                importBus.setId(bus.getId());
                try {
                    modifyBus(importBus);
                } catch (BusApiException e) {
                    e.printStackTrace();
                }
            }
        }
        list.forEach( bus -> {
            context.publishEvent(new BusCreateEvent(this, bus));
        });
    }

    @Override
    public List<Student> listBindStudent(Long routeOperationId, String stopName, String studentName) {
        StudentViewVO studentViewVO = new StudentViewVO();
        studentViewVO.setRouteOperationId(routeOperationId);
        studentViewVO.setServiceStatus(ServiceStatusEnum.EFFECTIVE);
        if (StringUtils.isNotBlank(stopName)) {
            studentViewVO.setStopName(stopName);
        }
        if (StringUtils.isNotBlank(studentName)) {
            studentViewVO.setName(studentName);
        }
        return studentMapper.listStudents(studentViewVO);
    }

    @Override
    public boolean checkNumberPlate(String numberPlate) {
        int count = baseMapper.checkNumberPlate(numberPlate);
        return count == 0;
    }

    @Override
    public List<SchoolBusListVO> listSchoolBus(String numberPlate) {
        // 10-12 增加是否在线
        return baseMapper.listSchoolBus(numberPlate);
    }

    @Override
    public List<SchoolRouteBusListVO> listSchoolRouteBus(String numberPlate, String busCode) {
        // 10-12 增加是否在线
        return baseMapper.listSchoolRouteBus(numberPlate, busCode);
    }

    @Override
	public UserBusVO getUserAndBus(String mobile) {
		UserBusVO Vo = baseMapper.findUserAndBusByMobile(mobile);
		Bus bus = null;
		if(Vo != null) {
			bus = baseMapper.findBusByUserId(Vo.getUserId());
			if(bus == null) {
				bus = baseMapper.getBusByWorkerId(Vo.getUserId());
			}		
			if(bus != null) {
				Vo.setNumberPlate(bus.getNumberPlate());
				return Vo;
			}else {
				return null;
			}
		}else {
			return null;
		}
	}

    @Override
    public Bus getBusByWorkerId(Long busTeacherId) {
        return baseMapper.getBusByWorkerId(busTeacherId);
    }

    @Override
    public Bus getByBusCode(String busCode) {
        LambdaQueryWrapper<Bus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bus::getBusCode, busCode);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Bus> listNoRegisterToGaode() {
        LambdaQueryWrapper<Bus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bus::getTid, 0);
        return list(queryWrapper);
    }

    @Override
    public void batchRegisterToGaode(List<Bus> buses) {
        List<Bus> updateBus = new ArrayList<>();
        for(Bus bus : buses) {
            Long tid = mapAmapService.createAmapEntity(bus.getNumberPlate(), bus.getId().toString());
            bus.setTid(tid);
            updateBus.add(bus);
        }
        if(!updateBus.isEmpty()) {
            updateBatchById(updateBus);
        }
    }

    @Override
    public void batchRegisterToBaidu(List<Bus> buses) {
        List<Bus> updateBus = new ArrayList<>();
        for(Bus bus : buses) {
            boolean isSuccess = mapBaiduService.createBaiduEntity(bus.getNumberPlate(), bus.getBusCode());
            if(isSuccess) {
                bus.setEntityName(bus.getBusCode());
                updateBus.add(bus);
            }
        }
        if(!updateBus.isEmpty()) {
            updateBatchById(updateBus);
        }
    }

    @Override
    public List<BusDetailLocationVO> listDetailByIds(String[] busIdArray) {
        return baseMapper.listDetailByIds(busIdArray);
    }

    @Override
    public List<BusTripTime> listBusTripTime(Long busId) {
        return baseMapper.listBusTripTime(busId);
    }

    @Override
    public List<BindBusDetailInfo> listBindBusDetailInfo() {
        return baseMapper.listBindBusDetailInfo(null);
    }

    @Override
    public BindBusDetailInfo getBindBusDetailInfoByBusCode(String busCode) {
        return baseMapper.getBindBusDetailInfo(busCode, null);
    }

    @Override
    public BindBusDetailInfo getBindBusDetailInfoByWorkerId(Long userId) {
        return baseMapper.getBindBusDetailInfo(null, userId);
    }

    @Override
    public List<String> findEntitysByRouteId(Long routeId) {
        return baseMapper.findEntitysByids(routeId);
    }

    @Override
    public List<BindBusStoptimeDetailInfo> listBusStopTimeDetailInfos() {
        return baseMapper.listBusStopTimeDetailInfos();
    }
}
