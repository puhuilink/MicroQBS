package com.phlink.bus.api.fence.dao;

import com.phlink.bus.api.fence.domain.FenceTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wen
 */
public interface FenceTimeMapper extends BaseMapper<FenceTime> {

    void deleteByFenceId(@Param("fenceId") Long fenceId);
}
