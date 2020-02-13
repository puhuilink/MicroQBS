package com.phlink.bus.api.serviceorg.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.excel.ExcelUtil;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.route.service.IRouteService;
import com.phlink.bus.api.route.service.IStopService;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.School;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.domain.VO.ServiceInvalidateVO;
import com.phlink.bus.api.serviceorg.domain.VO.StudentBaseInfoVO;
import com.phlink.bus.api.serviceorg.domain.VO.StudentIdVO;
import com.phlink.bus.api.serviceorg.domain.VO.StudentViewVO;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.serviceorg.service.ISchoolService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author zhouyi
 */
@Slf4j
@RestController
@RequestMapping("/student")
@Api(tags = ApiTagsConstant.TAG_STUDENT)
public class StudentController extends BaseController {

    @Autowired
    public IStudentService studentService;
    @Autowired
    public IGuardianService guardianService;
    @Autowired
    public ISchoolService schoolService;
    @Autowired
    public IRouteService routeService;
    @Autowired
    public IStopService stopService;

    @GetMapping
    //@RequiresPermissions("student:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "GET")
    public Map<String, Object> listStudentsPage(QueryRequest request, StudentViewVO studentViewVO) {
        return getDataTable(this.studentService.listStudentsPage(request, studentViewVO));
    }

    @GetMapping("list")
    //@RequiresPermissions("student:list")
    @ApiOperation(value = "根据班级id获取学生列表", notes = "根据班级id获取学生列表", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "GET")
    public List<Student> getStudentsByClassId(@NotBlank(message = "{required}") @RequestParam("classId") Long classId) {
        return this.studentService.getStudentByClassId(classId);
    }

    @Log("添加学生")
    @PostMapping
    //@RequiresPermissions("student:add")
    @ApiOperation(value = "添加", notes = "添加", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "POST")
    public void addStudent(@RequestBody @Valid Student student) throws BusApiException {
        try {
            this.studentService.createStudent(student);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusApiException(e.getMessage());
        }
    }

    @Log("修改学生信息")
    @PutMapping
//    //@RequiresPermissions("student:update")
    @ApiOperation(value = "修改学生信息", notes = "修改学生信息", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "PUT")
    public void updateStudent(@RequestBody @Valid Student student) throws BusApiException {
        try {
            this.studentService.modifyStudent(student);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改学生基本信息")
    @PutMapping("/baseinfo")
//    //@RequiresPermissions("student:update")
    @ApiOperation(value = "修改学生基本信息", notes = "修改学生基本信息", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "PUT")
    public void updateStudent(@RequestBody @Valid StudentBaseInfoVO baseInfoVO) throws BusApiException {

        Student student = studentService.getById(baseInfoVO.getStudentId());
        if(student == null) {
            throw new BusApiException("学生不存在");
        }
        if(StringUtils.isNotBlank(baseInfoVO.getStudentName())) {
            student.setName(baseInfoVO.getStudentName());
        }
        if(StringUtils.isNotBlank(baseInfoVO.getAvatar())) {
            student.setAvatar(baseInfoVO.getAvatar());
        }
        if(StringUtils.isNotBlank(baseInfoVO.getSex())) {
            student.setSex(baseInfoVO.getSex());
        }
        if(StringUtils.isNotBlank(baseInfoVO.getSchoolName())) {
            // 学校信息添加
            School school = schoolService.createSchool(baseInfoVO.getSchoolName());
            student.setSchoolId(school.getId());
            student.setSchoolName(school.getSchoolName());
        }
        if(baseInfoVO.getAge() != null) {
            student.setAge(baseInfoVO.getAge());
        }
        if(baseInfoVO.getBirthday() != null) {
            student.setBirthday(baseInfoVO.getBirthday());
        }
        this.studentService.updateById(student);
    }

    @Log("删除学生")
    @DeleteMapping("/{studentIds}")
    //@RequiresPermissions("student:delete")
    @ApiOperation(value = "删除学生", notes = "删除学生", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "DELETE")
    public void deleteStudent(@NotBlank(message = "{required}") @PathVariable String studentIds) throws BusApiException {
        try {
            String[] ids = studentIds.split(StringPool.COMMA);
            this.studentService.deleteStudentIds(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("student:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "POST")
    public void exportStudent(@RequestBody @Valid StudentViewVO studentViewVO, HttpServletResponse response) throws BusApiException {
        try {
            List<Student> students = this.studentService.listStudentsPage(studentViewVO);
            ExcelKit.$Export(Student.class, response).downXlsx(students, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @GetMapping("guardian")
    //@RequiresPermissions("guardian:view")
    @ApiOperation(value = "学生的监护人信息列表", notes = "学生信息", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "GET")
    public List<Guardian> studentGuardianList(@NotBlank(message = "{required}") @RequestParam("studentId") Long studentId) {
        return this.guardianService.listByStudentId(studentId);
    }

    @GetMapping("/{studentId}")
    //@RequiresPermissions("student:detail")
    @ApiOperation(value = "学生详细信息", notes = "学生详细信息", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "GET")
    public Student getStudentDetail(@NotBlank(message = "{required}") @PathVariable Long studentId) {
        return this.studentService.getStudentDetail(studentId);
    }

    @GetMapping("/unbind")
    //@RequiresPermissions("stop:view")
    @ApiOperation(value = "未绑定的学生列表", notes = "未绑定的学生列表", tags = ApiTagsConstant.TAG_STOP, httpMethod = "GET")
    public List<Student> unbindList(@RequestParam(required = true) Long schoolId) throws BusApiException {
        return this.studentService.listStudentUnbindStopInSchool(schoolId);
    }

    @Log("学生服务失效")
    @PostMapping("/invalidate")
    //@RequiresPermissions("student:invalidate")
    @ApiOperation(value = "学生服务失效", notes = "学生服务失效", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "POST")
    public void invalidate(@RequestBody @Valid ServiceInvalidateVO serviceInvalidateVO) throws BusApiException {
        this.studentService.invalidate(serviceInvalidateVO);
    }

    @Log("学生服务恢复")
    @PutMapping("/effective")
    //@RequiresPermissions("student:effective")
    @ApiOperation(value = "学生服务恢复", notes = "学生服务恢复", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "PUT")
    public void effective(@RequestBody StudentIdVO studentIdVO) throws BusApiException {
        Student student = this.studentService.getById(studentIdVO.getStudentId());
        LocalDate now = LocalDate.now();
//        if (student.getServiceStartDate()!=null&&)
        if (now.compareTo(student.getServiceStartDate()) >= 0 && now.compareTo(student.getServiceEndDate()) <= 0) {
            this.studentService.effective(studentIdVO.getStudentId());
        } else {
            throw new BusApiException("服务时间未开始或已经结束!");
        }
    }

    @PostMapping("/import")
    //@RequiresPermissions("student:add")
    @ApiOperation(value = "导入学生信息", notes = "导入学生信息", tags = ApiTagsConstant.TAG_STUDENT, httpMethod = "POST")
    public void importUser(@RequestParam MultipartFile file) {
        try {
            List<Student> list = ExcelUtil.readExcel(Objects.requireNonNull(file.getOriginalFilename()), file.getInputStream(), Student.class.getName(), 1);
            log.info("import studentList length: {}", list.size());
            studentService.batchImport(list);
        }catch (Exception e) {
            log.error("导入失败", e);
        }
    }

    @RequestMapping(value = "/down-template", method = RequestMethod.GET)
    public void downTemplate(HttpServletResponse response) {
        List<Student> userList = studentService.list();
        ExcelKit.$Export(Student.class, response).downXlsx(userList, true);
    }
}
