package com.phlink.bus.api.bus.dao;

import com.phlink.bus.api.bus.domain.Dvr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wen
 */
public interface DvrMapper extends BaseMapper<Dvr> {

    Dvr getByStudentId(@Param("studentId") Long studentId);
}
