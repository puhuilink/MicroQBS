package com.phlink.bus.api.serviceorg.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.serviceorg.dao.HomeworkMapper;
import com.phlink.bus.api.serviceorg.dao.HomeworkStudentMapper;
import com.phlink.bus.api.serviceorg.domain.HomeClassPage;
import com.phlink.bus.api.serviceorg.domain.HomeStudentPage;
import com.phlink.bus.api.serviceorg.domain.HomeworkStudent;
import com.phlink.bus.api.serviceorg.service.IHomeworkStudentService;

/**
 * @author Administrator
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class HomeworkStudentServiceImpl extends ServiceImpl<HomeworkStudentMapper, HomeworkStudent>
		implements IHomeworkStudentService {
	@Autowired
	private HomeworkStudentMapper homeworkStudentMapper;
	@Autowired
	private HomeworkMapper homeworkMapper;

	@Override
	public IPage<HomeworkStudent> listHomeworkStudents(QueryRequest request, HomeworkStudent homeworkStudent) {
		QueryWrapper<HomeworkStudent> queryWrapper = new QueryWrapper<>();
		// TODO:查询条件
		if (homeworkStudent.getCreateTimeFrom() != null) {
			queryWrapper.lambda().ge(HomeworkStudent::getCreateTime, homeworkStudent.getCreateTimeFrom());
		}
		if (homeworkStudent.getCreateTimeTo() != null) {
			queryWrapper.lambda().le(HomeworkStudent::getCreateTime, homeworkStudent.getCreateTimeTo());
		}
		Page<HomeworkStudent> page = new Page<>(request.getPageNum(), request.getPageSize());
		SortUtil.handlePageSort(request, page, "createTime", BusApiConstant.ORDER_DESC, true);
		return this.page(page, queryWrapper);
	}

	@Override
	@Transactional
	public void createHomeworkStudent(HomeworkStudent homeworkStudent) {
		homeworkStudent.setCreateTime(LocalDateTime.now());
		this.save(homeworkStudent);
	}

	@Override
	@Transactional
	public void modifyHomeworkStudent(HomeworkStudent homeworkStudent) {
		homeworkStudent.setModifyTime(LocalDateTime.now());
		homeworkStudent.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
		this.updateById(homeworkStudent);
	}

	@Override
	public void deleteHomeworkStudentIds(String[] homeworkStudentIds) {
		List<Long> list = Stream.of(homeworkStudentIds).map(Long::parseLong).collect(Collectors.toList());
		removeByIds(list);
	}

	@Override
	public IPage<HomeClassPage> queryHomework(QueryRequest request, Long userId, String queryDate, Long classId,
			Long homeworkId) {
		// TODO Auto-generated method stub
		Page<HomeClassPage> page = new Page<>(request.getPageNum(), request.getPageSize());
		SortUtil.handlePageSort(request, page, "homeworkStatus", BusApiConstant.ORDER_ASC, true);
		return homeworkStudentMapper.queryHomework(page, userId, queryDate, classId, homeworkId);
	}

	@Override
	@Transactional
	public synchronized String updateHomeworkStudentById(Long userId, Long id, Long homeworkId, Integer homeworkLevel,
			String img, String homeworkStatus, Long studentId) {
		// TODO Auto-generated method stub
		if ("5".equals(homeworkStatus)) {
			if (homeworkLevel == null) {
				return "老师给作业评心，参数（homeworkLevel）不能为空";
			}
		}
		if ("4".equals(homeworkStatus)) {
			if (img == null || "".equals(img.trim())) {
				return "家长上传时，参数（img）不能为空";
			}
		}
		if ("3".equals(homeworkStatus)) {
			if (studentId == null) {
				return "家长下载时，参数（studentId）不能为空";
			}
		}
		// 2 已查看
		boolean isViewedBool = true;
		if ("2".equals(homeworkStatus)) {
			int isViewed = homeworkStudentMapper.findisViewedById(homeworkId, studentId);
			if(isViewed==0){
				isViewedBool=false;
				homeworkMapper.updateHomeworkBrowseNumByID(homeworkId, userId, LocalDateTime.now());
			}
		}
		//-3 已下载
		boolean isdownload = true;
		if ("3".equals(homeworkStatus)) {
			// 查询是否已下载，否-》 下载人数加1 ， 是 不变。
			int isDownload = homeworkStudentMapper.findIsdownloadById(homeworkId, studentId);
			if (isDownload == 0) {
				isdownload = false;
				homeworkMapper.updateHomeworkDownNumNumByID(homeworkId, userId, LocalDateTime.now());
			}
		}

		homeworkStudentMapper.updateHomeworkStudentById(userId, id, homeworkLevel, LocalDateTime.now(), homeworkStatus,
				img, isdownload,isViewedBool);
		return "更新作业状态成功";
	}

	@Override
	public IPage<HomeStudentPage> querystudentHomeworkPage(QueryRequest request, String queryDate, Long studentId) {
		// TODO Auto-generated method stub
		Page<HomeStudentPage> page = new Page<>(request.getPageNum(), request.getPageSize());
		SortUtil.handlePageSort(request, page, "homeworkDate", BusApiConstant.ORDER_DESC, true);
		return homeworkStudentMapper.querystudentHomeworkPage(page, queryDate, studentId);
	}

}
