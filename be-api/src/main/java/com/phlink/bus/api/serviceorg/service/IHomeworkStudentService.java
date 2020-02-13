package com.phlink.bus.api.serviceorg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.serviceorg.domain.HomeClassPage;
import com.phlink.bus.api.serviceorg.domain.HomeStudentPage;
import com.phlink.bus.api.serviceorg.domain.HomeworkStudent;

/**
 * @author Administrator
 */
public interface IHomeworkStudentService extends IService<HomeworkStudent> {

	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param homeworkStudent
	 * @return
	 */
	IPage<HomeworkStudent> listHomeworkStudents(QueryRequest request, HomeworkStudent homeworkStudent);

	/**
	 * 新增
	 * 
	 * @param homeworkStudent
	 */
	void createHomeworkStudent(HomeworkStudent homeworkStudent);

	/**
	 * 修改
	 * 
	 * @param homeworkStudent
	 */
	void modifyHomeworkStudent(HomeworkStudent homeworkStudent);

	/**
	 * 批量删除
	 * 
	 * @param homeworkStudentIds
	 */
	void deleteHomeworkStudentIds(String[] homeworkStudentIds);

	IPage<HomeClassPage> queryHomework(QueryRequest request, Long userId, String queryDate, Long classId,
			Long homeworkId);

	String updateHomeworkStudentById(Long userId, Long id, Long homeworkId, Integer homeworkLevel, String img,
			String homeworkStatus, Long studentId);

	IPage<HomeStudentPage> querystudentHomeworkPage(QueryRequest request,String queryDate, Long studentId);
}
