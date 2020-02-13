package com.phlink.bus.api.device.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.device.domain.VO.DeviceViewVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zy
 */
public interface DeviceMapper extends BaseMapper<Device> {

    Page<Device> listDevices(Page page, @Param("deviceViewVO") DeviceViewVO deviceViewVO);

    List<String> getSchoolDevices(@Param("schoolId")Long schoolId);

    Device getByStudentId(@Param("studentId") Long studentId);

    List<Device> listByStudentInfo(@Param("studentId") Long studentId, @Param("studentName") String studentName, @Param("schoolName") String schoolName, @Param("busCode") String busCode, @Param("numberPlate") String numberPlate);
}
