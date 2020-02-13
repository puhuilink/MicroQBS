package com.phlink.bus.api.serviceorg.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.serviceorg.domain.Classes;
import com.phlink.bus.api.serviceorg.domain.School;
import com.phlink.bus.api.serviceorg.domain.VO.SchoolViewVO;
import com.phlink.bus.api.serviceorg.service.IClassesService;
import com.phlink.bus.api.serviceorg.service.ISchoolService;
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
 * @author wen
 */
@Slf4j
@RestController
@RequestMapping("/school")
@Api(tags = ApiTagsConstant.TAG_SCHOOL)
public class SchoolController extends BaseController {

    @Autowired
    public ISchoolService schoolService;
    @Autowired
    public IClassesService classesService;

    @GetMapping
    //@RequiresPermissions("school:view")
    @ApiOperation(value = "分页列表", notes = "分页列表", tags = ApiTagsConstant.TAG_SCHOOL, httpMethod = "GET")
    public Map<String, Object> listSchool(QueryRequest request, SchoolViewVO schoolViewVO) {
        return getDataTable(this.schoolService.listSchools(request, schoolViewVO));
    }

    @GetMapping("list")
    //@RequiresPermissions("school:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_SCHOOL, httpMethod = "GET")
    public List<School> listSchool(School school) {
        return this.schoolService.listSchools(school);
    }

    @GetMapping("check/{shoolName}")
    public boolean checkShoolName(@NotBlank(message = "{required}") @PathVariable String schoolName) {
        return this.schoolService.findByName(schoolName) == null;
    }

    @Log("添加学校")
    @PostMapping
    //@RequiresPermissions("school:add")
    @ApiOperation(value = "添加", notes = "添加", tags = ApiTagsConstant.TAG_SCHOOL, httpMethod = "POST")
    public void addSchool(@Valid @RequestBody School school) throws BusApiException {
        try {
            this.schoolService.createSchool(school);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改学校")
    @PutMapping
    //@RequiresPermissions("school:update")
    @ApiOperation(value = "修改", notes = "修改", tags = ApiTagsConstant.TAG_SCHOOL, httpMethod = "PUT")
    public void updateSchool(@RequestBody @Valid School school) throws BusApiException {
        try {
            this.schoolService.modifySchool(school);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除学校")
    @DeleteMapping("/{schoolIds}")
    //@RequiresPermissions("school:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = ApiTagsConstant.TAG_SCHOOL, httpMethod = "DELETE")
    public void deleteSchool(@NotBlank(message = "{required}") @PathVariable String schoolIds) throws BusApiException {
        try {
            String[] ids = schoolIds.split(StringPool.COMMA);
            this.schoolService.deleteSchoolIds(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("school:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_SCHOOL, httpMethod = "POST")
    public void exportSchool(QueryRequest request, @RequestBody @Valid SchoolViewVO schoolViewVO, HttpServletResponse response) throws BusApiException {
        try {
            List<School> schools = this.schoolService.listSchools(request, schoolViewVO).getRecords();
            ExcelKit.$Export(School.class, response).downXlsx(schools, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @GetMapping("{schoolId}/classes")
    //@RequiresPermissions("classes:list")
    @ApiOperation(value = "根据学校id获取班级列表", notes = "根据学校id获取班级列表", tags = ApiTagsConstant.TAG_CLASSES, httpMethod = "GET")
    public List<Classes> listClassesBySchoolId(@Valid @NotBlank(message = "{required}") @PathVariable("schoolId") Long schoolId) {
        return this.classesService.getClassesBySchoolId(schoolId);
    }

    @GetMapping("{schoolId}/grade-class-cascade")
    @ApiOperation(value = "年级班级级联", notes = "根据学校id获取年级班级级联", tags = ApiTagsConstant.TAG_CLASSES, httpMethod = "GET")
    public String listGradeClassesCascade(@Valid @NotBlank(message = "{required}") @PathVariable("schoolId") Long schoolId) {
        return this.classesService.listGradeClassesCascade(schoolId);
    }

}
