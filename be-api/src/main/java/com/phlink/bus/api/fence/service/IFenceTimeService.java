package com.phlink.bus.api.fence.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.fence.domain.FenceTime;

import java.util.List;

/**
 * @author wen
 */
public interface IFenceTimeService extends IService<FenceTime> {

    /**
    * 批量删除
    * @param fenceTimeIds
    */
    void deleteFenceTimes(String[] fenceTimeIds);

    void deleteByFenceId(Long fenceId);

    List<FenceTime> listByFenceId(Long fenceId);
}
