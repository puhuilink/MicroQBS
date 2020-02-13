package com.phlink.bus.api.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.device.domain.VO.BindingDeviceVO;
import com.phlink.bus.api.device.domain.VO.DeviceViewVO;

import java.util.List;

/**
 * @author zy
 */
public interface IDeviceService extends IService<Device> {
    /**
     * 查询设备列表
     *
     * @param request
     * @param deviceViewVO
     * @return
     */
    Page<Device> listDevices(QueryRequest request, DeviceViewVO deviceViewVO);

    List<Device> listDevices(DeviceViewVO deviceViewVO);

    /**
     * 新增设备
     *
     * @param device
     */
    void createDevice(Device device) throws BusApiException, RedisConnectException;

    /**
     * 修改设备
     *
     * @param device
     */
    void modifyDevice(Device device);

    /**
     * 批量删除设备
     *
     * @param deviceIds
     */
    void deleteDeviceIds(String[] deviceIds) throws BusApiException, RedisConnectException;

    /**
     * 绑定设备
     *
     * @param bindingDeviceVO
     */
    void bindingDevice(BindingDeviceVO bindingDeviceVO) throws BusApiException;

    /**
     * 解绑设备
     *
     * @param bindingDeviceVO
     */
    void unbindingDevice(BindingDeviceVO bindingDeviceVO) throws BusApiException;

    /**
     * 根据deviceCode获取数据
     *
     * @param deviceCode
     * @return
     */
    Device findByDeviceCode(String deviceCode);

    /**
     * 根据deviceCode删除设备
     *
     * @param deviceCode
     * @return
     */
    void deleteByDeviceCode(String deviceCode) throws BusApiException;

    /**
     * 根据学生ID获取设备信息
     * @param studentId
     * @return
     */
    Device getByStudentId(Long studentId);

    /**
     * 根据学生信息查询设备信息
     * @param studentId
     * @param studentName
     * @param schoolName
     * @param busCode
     * @param numberPlate
     * @return
     */
    List<Device> listByStudentInfo(Long studentId, String studentName, String schoolName, String busCode, String numberPlate);

    /**
     * 统计已绑定的设备
     * @return
     */
    int countBindDevice();

    void deviceHeartBeat();

    void batchRegisterToGaode(List<Device> devices);
}
