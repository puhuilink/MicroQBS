package com.phlink.bus.api.serviceorg.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.DateUtil;
import com.phlink.bus.api.serviceorg.domain.Homework;
import com.phlink.bus.api.serviceorg.service.IHomeworkService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
@RestController
@RequestMapping("/homework")
@Api(value = "作业信息")
public class HomeworkController extends BaseController {

	@Autowired
	public IHomeworkService homeworkService;

	@GetMapping
	//@RequiresPermissions("homework:view")
	@ApiOperation(value = "作业信息列表", notes = "作业信息列表", tags = "作业信息", httpMethod = "GET")
	public Map<String, Object> ListHomework(QueryRequest request, Homework homework) {
		return getDataTable(this.homeworkService.listHomeworks(request, homework));
	}

	@Log("添加作业")
	@PostMapping
	//@RequiresPermissions("homework:add")
	@ApiOperation(value = "作业信息添加", notes = "作业信息添加", tags = "作业信息", httpMethod = "POST")
	public void addHomework(@RequestBody @Valid Homework homework) throws BusApiException {
		try {
			this.homeworkService.createHomework(homework);
		} catch (Exception e) {
			String message = "新增失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	@Log("修改作业")
	@PutMapping
	//@RequiresPermissions("homework:update")
	@ApiOperation(value = "作业信息修改", notes = "作业信息修改", tags = "作业信息", httpMethod = "PUT")
	public void updateHomework(@RequestBody @Valid Homework homework) throws BusApiException {
		try {
			this.homeworkService.modifyHomework(homework);
		} catch (Exception e) {
			String message = "修改失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	@Log("删除作业")
	@DeleteMapping("/{homeworkIds}")
	//@RequiresPermissions("homework:delete")
	@ApiOperation(value = "作业信息删除", notes = "作业信息删除", tags = "作业信息", httpMethod = "DELETE")
	public void deleteHomework(@NotBlank(message = "{required}") @PathVariable String homeworkIds)
			throws BusApiException {
		try {
			String[] ids = homeworkIds.split(StringPool.COMMA);
			this.homeworkService.deleteHomeworkIds(ids);
		} catch (Exception e) {
			String message = "删除失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	@PostMapping("export")
	//@RequiresPermissions("homework:export")
	@ApiOperation(value = "作业信息导出", notes = "作业信息导出", tags = "作业信息", httpMethod = "POST")
	public void exportHomework(QueryRequest request, @RequestBody @Valid Homework homework, HttpServletResponse response)
			throws BusApiException {
		try {
			List<Homework> homeworks = this.homeworkService.listHomeworks(request, homework).getRecords();
			ExcelKit.$Export(Homework.class, response).downXlsx(homeworks, false);
		} catch (Exception e) {
			String message = "导出Excel失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	@GetMapping("page")
	//@RequiresPermissions("homework:view")
	@ApiOperation(value = "作业管理主页面列表", notes = "作业管理主页面列表", tags = "作业信息", httpMethod = "GET")
	public Map<String, Object> queryHomework(QueryRequest request,
			@NotBlank(message = "{required}") @RequestParam("queryDate") String queryDate) throws BusApiException {
		try {
			User currentUser = BusApiUtil.getCurrentUser();
			return getDataTable(this.homeworkService.queryHomework(currentUser.getUserId(),queryDate, request));
		} catch (Exception e) {
			String message = "查询失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

	@Log("布置作业")
	@PostMapping("pushHomework")
	//@RequiresPermissions("homework:add")
	@ApiOperation(value = "布置作业", notes = "布置作业", tags = "作业信息", httpMethod = "POST")
	public void pushHomework(@RequestParam("classesId") String classesId,
			@RequestParam("homeworkNme") String homeworkName,
			@RequestParam("homeworkStartDate") String homeworkStartDate,
			@RequestParam("homeworkEndDate") String homeworkEndDate, @RequestParam("homeworkLink") String homeworkLink,
			@RequestParam("classNum") String classNum) throws BusApiException {
		try {
			User currentUser = BusApiUtil.getCurrentUser();
			List<Homework> list = new ArrayList<Homework>();
			String[] classIDStr = classesId.split(",");
			Homework bean = null;
			for (String classId : classIDStr) {
				bean = new Homework();
				bean.setBrowseNum(0);
				bean.setClassId(Long.parseLong(classId));
				bean.setClassNum(Integer.parseInt(classNum));
				bean.setCreateBy(currentUser.getUserId());
				bean.setCreateTime(LocalDateTime.now());
				bean.setDeleted(false);
				bean.setDownNum(0);
				bean.setHomeworkClassStatus("1");
				bean.setHomeworkEndDate(DateUtil.dateToLocalDate(DateUtil.formatStr(homeworkEndDate, "yyyy-MM-dd")));
				bean.setHomeworkLink(homeworkLink);
				bean.setHomeworkName(homeworkName);
				bean.setHomeworkStartDate(
						DateUtil.dateToLocalDate(DateUtil.formatStr(homeworkStartDate, "yyyy-MM-dd")));
				list.add(bean);
			}

			homeworkService.pushHomework(list);
		} catch (Exception e) {
			String message = "新增失败";
			log.error(message, e);
			throw new BusApiException(message);
		}
	}

}
