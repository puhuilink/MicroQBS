package com.phlink.bus.api.bus.service;

import com.phlink.bus.api.bus.domain.Dvr;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;

import java.util.List;

/**
 * @author wen
 */
public interface IDvrService extends IService<Dvr> {


    /**
    * 获取详情
    */
    Dvr findById(Long id);

    /**
    * 查询列表
    * @param request
    * @param dvr
    * @return
    */
    IPage<Dvr> listDvrs(QueryRequest request, Dvr dvr);

    /**
    * 新增
    * @param dvr
    */
    void createDvr(Dvr dvr) throws BusApiException;

    /**
    * 修改
    * @param dvr
    */
    void modifyDvr(Dvr dvr);

    /**
    * 批量删除
    * @param dvrIds
    */
    void deleteDvrs(String[] dvrIds);

    /**
     * 根据dvrCode获取DVR设备
     * @param dvrCode
     * @return
     */
    Dvr getByDvrCode(String dvrCode);

    /**
     * 根据dvrCode获取DVR设备
     * @param busId
     * @return
     */
    Dvr getByBusId(Long busId);

    /**
     * 根据学生ID获得所在车辆的DVR
     * @param studentId
     * @return
     */
    Dvr getByStudentId(Long studentId);

    /**
     * 返回在线的DVR数量
     * @return
     */
    int countRunning();

    List<Dvr> listDrv();
}
