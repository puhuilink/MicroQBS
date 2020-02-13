package com.phlink.bus.api.serviceorg.service.impl;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.serviceorg.dao.HomeworkMapper;
import com.phlink.bus.api.serviceorg.domain.HomeWorkPage;
import com.phlink.bus.api.serviceorg.domain.Homework;
import com.phlink.bus.api.serviceorg.domain.HomeworkStudent;
import com.phlink.bus.api.serviceorg.service.IHomeworkService;
import com.phlink.bus.api.serviceorg.service.IHomeworkStudentService;

/**
 * @author wen
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements IHomeworkService {
	@Autowired
	private HomeworkMapper homeworkMapper;
	@Autowired
	private IStudentService studentService;
	@Autowired
	private IHomeworkStudentService homeworkStudentService;

	@Override
	public IPage<Homework> listHomeworks(QueryRequest request, Homework homework) {
		QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
		// TODO:查询条件
		if (homework.getCreateTimeFrom() != null) {
			queryWrapper.lambda().ge(Homework::getCreateTime, homework.getCreateTimeFrom());
		}
		if (homework.getCreateTimeTo() != null) {
			queryWrapper.lambda().le(Homework::getCreateTime, homework.getCreateTimeTo());
		}
		Page<Homework> page = new Page<>(request.getPageNum(), request.getPageSize());
		SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
		return this.page(page, queryWrapper);
	}

	@Override
	@Transactional
	public void createHomework(Homework homework) {
		homework.setCreateTime(LocalDateTime.now());
		this.save(homework);
	}

	@Override
	@Transactional
	public void modifyHomework(Homework homework) {
		homework.setModifyTime(LocalDateTime.now());
		homework.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
		this.updateById(homework);
	}

	@Override
	public void deleteHomeworkIds(String[] homeworkIds) {
		List<Long> list = Stream.of(homeworkIds).map(Long::parseLong).collect(Collectors.toList());
		removeByIds(list);
	}

	@Override
	public IPage<HomeWorkPage> queryHomework(Long id, @NotBlank(message = "{required}") String createTime,
			QueryRequest request) {
		// TODO Auto-generated method stub
		Page<HomeWorkPage> page = new Page<>();
		SortUtil.handlePageSort(request, page, "classId", BusApiConstant.ORDER_ASC, false);
		return homeworkMapper.queryHomework(page, id, createTime);

	}

	@Override
	@Transactional
	public void pushHomework(List<Homework> list) throws ParseException {
		// TODO Auto-generated method stub
		// 插入作业表
		Date startDate = DateUtil.localDateToDate(list.get(0).getHomeworkStartDate());
		Date endDate = DateUtil.localDateToDate(list.get(0).getHomeworkEndDate());

		for (Homework homework : list) {
			this.save(homework);
			// 插入学生作业表
			Long classId = homework.getClassId();
			List<Student> students = studentService.getStudentByClassId(classId);
			if (students != null && students.size() > 0) {
				HomeworkStudent bean = null;
				for (Student student : students) {
					int diff = DateUtil.daysBetween(startDate, endDate);

					for (int i = 0; i < diff; i++) {
						bean = new HomeworkStudent();
						bean.setCreateBy(list.get(0).getCreateBy());
						bean.setCreateTime(LocalDateTime.now());
						bean.setDeleted(false);
						bean.setHomeworkDate(DateUtil.dateToLocalDate(DateUtil.addDay(startDate, i)));
						bean.setHomeworkStatus("1");
						bean.setHomeworkId(homework.getId());
						bean.setStuId(student.getId());
						homeworkStudentService.save(bean);
					}

				}
			}
		}

	}

}
