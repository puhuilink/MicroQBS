package com.phlink.bus.api.device.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.bus.api.device.domain.DeviceRelation;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhouyi
 */
public interface DeviceRelationMapper extends BaseMapper<DeviceRelation> {

    DeviceRelation getByDeviceCode(@Param("deviceCode") String deviceCode);
}
