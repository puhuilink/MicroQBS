package com.phlink.bus.api.job.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phlink.bus.api.job.domain.Job;

import java.util.List;

public interface JobMapper extends BaseMapper<Job> {
	
	List<Job> queryList();

}