package com.phlink.bus.api.serviceorg.dao;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.serviceorg.domain.HomeWorkPage;
import com.phlink.bus.api.serviceorg.domain.Homework;

/**
 * @author Administrator
 */
public interface HomeworkMapper extends BaseMapper<Homework> {

	IPage<HomeWorkPage> queryHomework(Page page,@Param("userId") Long id, @Param("createTime") String createTime);

	void updateHomeworkBrowseNumByID(@Param("homeworkId") Long homeworkId, @Param("modifyBy") Long modifyBy,
			@Param("modifyTime") LocalDateTime modifyTime);

	void updateHomeworkDownNumNumByID(@Param("homeworkId") Long homeworkId, @Param("modifyBy") Long modifyBy,
			@Param("modifyTime") LocalDateTime modifyTime);

	 

}
