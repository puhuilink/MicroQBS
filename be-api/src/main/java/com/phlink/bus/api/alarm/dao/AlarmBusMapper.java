package com.phlink.bus.api.alarm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.alarm.domain.AlarmBus;
import com.phlink.bus.api.alarm.domain.AlarmBusVO;
import com.phlink.bus.api.alarm.domain.CodeRule;
import com.phlink.bus.api.alarm.domain.enums.AlarmSubTypeEnum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhouyi
 */
public interface AlarmBusMapper extends BaseMapper<AlarmBus> {

    List<CodeRule> listBusCode();

    IPage<AlarmBus> listAlarmBuss(Page page, @Param("alarmBusVO") AlarmBusVO alarmBusVO);

    AlarmBus getLastAlarmInfo(@Param("subType") AlarmSubTypeEnum subType, @Param("busCode") String busCode);
}
