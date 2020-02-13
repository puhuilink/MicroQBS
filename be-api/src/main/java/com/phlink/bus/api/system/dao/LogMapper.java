package com.phlink.bus.api.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.system.domain.SysLog;
import org.apache.ibatis.annotations.Param;

public interface LogMapper extends BaseMapper<SysLog> {
    IPage<SysLog> listLogs(Page<SysLog> page, @Param("sysLog") SysLog sysLog);
}