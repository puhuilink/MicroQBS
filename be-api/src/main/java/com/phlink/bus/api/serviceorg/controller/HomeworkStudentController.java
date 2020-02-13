package com.phlink.bus.api.serviceorg.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.serviceorg.domain.HomeworkStudent;
import com.phlink.bus.api.serviceorg.service.IHomeworkStudentService;
import com.phlink.bus.api.system.domain.User;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
@RestController
@RequestMapping("/homework-student")
@Api(value = "学生作业信息")
public class HomeworkStudentController extends BaseController {

	@Autowired
	public IHomeworkStudentService homeworkStudentService;

	@GetMapping
	//@RequiresPermissions("homeworkStudent:view")
	@ApiOperation(value = "学生作业信息列表", notes = "学生作业信息列表", tags = "学生作业信息", httpMethod = "GET")
	public Map<String, Object> ListHomeworkStudent(QueryRequest request, HomeworkStudent homeworkStudent) {
		return getDataTable(this.homeworkStudentService.listHomeworkStudents(request, homeworkStudent));
	}

	@Log("添加学生作业信息")
	@PostMapping
	//@RequiresPermissions("homeworkStudent:add")
	@ApiOperation(value = "学生作业信息添加", notes = "学生作业信息添加", tags = "学生作业信息", httpMethod = "POST")
	public void addHomeworkStudent(@RequestBody @Valid HomeworkStudent homeworkStudent) throws BusApiException {
		try {
			this.homeworkStudentService.createHomeworkStudent(homeworkStudent);
		} catch (Exception e) {
			String message = "新增失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	@Log("修改学生作业信息")
	@PutMapping
	//@RequiresPermissions("homeworkStudent:update")
	@ApiOperation(value = "学生作业信息修改", notes = "学生作业信息修改", tags = "学生作业信息", httpMethod = "PUT")
	public void updateHomeworkStudent(@RequestBody @Valid HomeworkStudent homeworkStudent) throws BusApiException {
		try {
			this.homeworkStudentService.modifyHomeworkStudent(homeworkStudent);
		} catch (Exception e) {
			String message = "修改失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	@Log("删除学生作业信息")
	@DeleteMapping("/{homeworkStudentIds}")
	//@RequiresPermissions("homeworkStudent:delete")
	@ApiOperation(value = "学生作业信息删除", notes = "学生作业信息删除", tags = "学生作业信息", httpMethod = "DELETE")
	public void deleteHomeworkStudent(@NotBlank(message = "{required}") @PathVariable String homeworkStudentIds)
			throws BusApiException {
		try {
			String[] ids = homeworkStudentIds.split(StringPool.COMMA);
			this.homeworkStudentService.deleteHomeworkStudentIds(ids);
		} catch (Exception e) {
			String message = "删除失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	@PostMapping("export")
	//@RequiresPermissions("homeworkStudent:export")
	@ApiOperation(value = "学生作业信息导出", notes = "学生作业信息导出", tags = "学生作业信息", httpMethod = "POST")
	public void exportHomeworkStudent(QueryRequest request, @RequestBody @Valid HomeworkStudent homeworkStudent,
									  HttpServletResponse response) throws BusApiException {
		try {
			List<HomeworkStudent> homeworkStudents = this.homeworkStudentService
					.listHomeworkStudents(request, homeworkStudent).getRecords();
			ExcelKit.$Export(HomeworkStudent.class, response).downXlsx(homeworkStudents, false);
		} catch (Exception e) {
			String message = "导出Excel失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	@GetMapping("teacherHomeworkPage")
	//@RequiresPermissions("homeworkStudent:view")
	@ApiOperation(value = "老师班级作业列表", notes = "老师班级作业列表", tags = "学生作业信息", httpMethod = "GET")
	public Map<String, Object> queryHomework(QueryRequest request,
			@NotBlank(message = "{required}") @RequestParam("queryDate") String queryDate,
			@NotBlank(message = "{required}") @RequestParam("classId") Long classId,
			@NotBlank(message = "{required}") @RequestParam("homeworkId") Long homeworkId) throws BusApiException {
		try {
			User currentUser = BusApiUtil.getCurrentUser();
			return getDataTable(this.homeworkStudentService.queryHomework(request,currentUser.getUserId(), queryDate, classId,
					homeworkId));
		} catch (Exception e) {
			String message = "查询失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	/**
	 * @param id
	 *            家长页面：学生作业关系表对应的id,家长端调用必须传递
	 * @param studentId
	 *             家长下载时，必须传递，更新下载状态
	 * @param homeworkId
	 *            作业表的ID
	 * @param homeworkLevel
	 *            homeworkStatus=5时，评心时传递
	 * @param img
	 *            家长上传图片，英文，分割
	 * @param homeworkStatus
	 *            2 已查看-3 已下载-4（家长）已上传-5已批阅
	 * @throws BusApiException
	 */
	@Log(" 更新作业状态")
	@PutMapping("updateHomeworkStudentById")
	//@RequiresPermissions("homeworkStudent:update")
	@ApiOperation(value = "更新作业状态", notes = "更新作业状态", tags = "学生作业信息", httpMethod = "PUT")
	public String updateHomeworkStudentById(@RequestParam("id") Long id,@RequestParam("studentId") Long studentId,
			@NotBlank(message = "{required}") @RequestParam("homeworkId") Long homeworkId,
			@RequestParam("homeworkLevel") Integer homeworkLevel, @RequestParam("img") String img,
			@NotBlank(message = "{required}") @RequestParam("homeworkStatus") String homeworkStatus)
			throws BusApiException {
		try {
			User currentUser = BusApiUtil.getCurrentUser();
			return homeworkStudentService.updateHomeworkStudentById(currentUser.getUserId(), id, homeworkId,
					homeworkLevel, img, homeworkStatus,studentId);
		} catch (Exception e) {
			String message = "修改失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	/**
	 * @param queryDate 预留条件
	 * @param studentId
	 * @return
	 * @throws BusApiException
	 */
	@GetMapping("studentHomeworkPage")
	//@RequiresPermissions("homeworkStudent:view")
	@ApiOperation(value = "家长作业列表", notes = "家长作业列表", tags = "学生作业信息", httpMethod = "GET")
	public Map<String, Object> studentHomeworkPage(QueryRequest request,
			 @RequestParam("queryDate") String queryDate,
			@NotBlank(message = "{required}") @RequestParam("studentId") Long studentId) throws BusApiException {
		try {
			 
			return getDataTable(this.homeworkStudentService.querystudentHomeworkPage( request,queryDate, studentId));
		} catch (Exception e) {
			String message = "查询失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}
}
