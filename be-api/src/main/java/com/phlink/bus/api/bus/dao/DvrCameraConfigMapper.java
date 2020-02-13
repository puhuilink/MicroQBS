package com.phlink.bus.api.bus.dao;

import com.phlink.bus.api.bus.domain.DvrCameraConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.bus.api.bus.domain.VO.DvrBusLocationInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wen
 */
public interface DvrCameraConfigMapper extends BaseMapper<DvrCameraConfig> {

    List<DvrBusLocationInfoVO> listDvrBusLocationInfo(@Param("busId") Long busId);

    DvrBusLocationInfoVO getDvrBusLocationInfoByGuardian(@Param("busId") Long busId);

    DvrCameraConfig getGuardianLocationConfig(@Param("busId") Long busId);
}
