package com.phlink.bus.api.alarm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.alarm.domain.AlarmDevice;
import com.phlink.bus.api.alarm.domain.AlarmDeviceVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhouyi
 */
public interface AlarmDeviceMapper extends BaseMapper<AlarmDevice> {


    IPage<AlarmDevice> listAlarmDevices(Page page, @Param("alarmDeviceVO") AlarmDeviceVO alarmDeviceVO);
}
