package com.phlink.bus.api.serviceorg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.serviceorg.domain.HomeClassPage;
import com.phlink.bus.api.serviceorg.domain.HomeStudentPage;
import com.phlink.bus.api.serviceorg.domain.HomeworkStudent;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * @author Administrator
 */
public interface HomeworkStudentMapper extends BaseMapper<HomeworkStudent> {

	IPage<HomeClassPage> queryHomework(Page page, @Param("userId") Long userId, @Param("queryDate") String queryDate,
			@Param("classId") Long classId, @Param("homeworkId") Long homeworkId);

	void updateHomeworkStudentById(@Param("userId") Long userId, @Param("homeworkId") Long homeworkId,
			@Param("homeworkLevel") Integer homeworkLevel, @Param("modifyTime") LocalDateTime modifyTime,
			@Param("homeworkStatus") String homeworkStatus, @Param("img") String img,
			@Param("isDownload") boolean isdownload, @Param("isViewedBool") boolean isViewedBool);

	IPage<HomeStudentPage> querystudentHomeworkPage(Page page, @Param("queryDate") String queryDate,
			@Param("studentId") Long studentId);

	int findIsdownloadById(@Param("homeworkId") Long homeworkId, @Param("studentId") Long studentId);

	int findisViewedById(@Param("homeworkId") Long homeworkId, @Param("studentId") Long studentId);

}
