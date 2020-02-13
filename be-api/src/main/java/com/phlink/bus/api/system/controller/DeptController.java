package com.phlink.bus.api.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.Lists;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.system.domain.Dept;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.DeptService;
import com.phlink.bus.api.system.service.UserService;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("dept")
@Api(tags ="组织机构")
public class DeptController extends BaseController {
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "组织机构列表", notes = "组织机构列表", tags = ApiTagsConstant.TAG_DEPT, httpMethod = "GET")
    @GetMapping
    public Map<String, Object> deptList(QueryRequest request, Dept dept) {
        return this.deptService.findDepts(request, dept);
    }

    @ApiOperation(value = "组织机构下的用户列表", notes = "组织机构下的用户列表", tags = ApiTagsConstant.TAG_DEPT, httpMethod = "GET")
    @GetMapping("/user")
    public List<User> userList(@RequestParam(name = "deptId", required = false) Long deptId) {
        return this.userService.listByDept(deptId);
    }

    @ApiOperation(value = "新增组织机构", notes = "添加新的组织机构", tags = ApiTagsConstant.TAG_DEPT, httpMethod = "POST")
    @Log("新增组织机构")
    @PostMapping
    //@RequiresPermissions("dept:add")
    public void addDept(@RequestBody @Valid Dept dept) throws BusApiException {
        String message = "添加组织机构失败";
        Dept d = this.deptService.getByDeptName(dept.getDeptName());
        if(d != null) {
            message = "部门名称重复";
            throw new BusApiException(message);
        }
        try {
            this.deptService.createDept(dept);
        } catch (Exception e) {
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "删除组织机构", notes = "删除一个组织机构", tags = ApiTagsConstant.TAG_DEPT, httpMethod = "DELETE")
    @Log("删除组织机构")
    @DeleteMapping("/{deptIds}")
    //@RequiresPermissions("dept:delete")
    public void deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) throws BusApiException {
        try {
            String[] ids = deptIds.split(StringPool.COMMA);
            this.deptService.deleteDepts(ids);
        } catch (Exception e) {
            String message = "删除组织机构失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @ApiOperation(value = "修改组织机构", notes = "修改组织机构字段", tags = ApiTagsConstant.TAG_DEPT, httpMethod = "PUT")
    @Log("修改组织机构")
    @PutMapping
    //@RequiresPermissions("dept:update")
    public void updateDept(@RequestBody @Valid Dept dept) throws BusApiException {
        String message = "修改组织机构失败";
        Dept d = this.deptService.getByDeptName(dept.getDeptName());
        if(d != null && !d.getDeptId().equals(dept.getDeptId())) {
            message = "部门名称重复";
            throw new BusApiException(message);
        }
        try {
            this.deptService.updateDept(dept);
        } catch (Exception e) {
            log.error(message, e);
            throw new BusApiException(message);
        }
    }


    @ApiOperation(value = "导出组织机构数据", notes = "导出组织机构数据", tags = ApiTagsConstant.TAG_DEPT, httpMethod = "POST")
    @PostMapping("excel")
    //@RequiresPermissions("dept:export")
    public void export(@RequestBody Dept dept, QueryRequest request, HttpServletResponse response) throws BusApiException {
        try {
            List<Dept> depts = this.deptService.findDepts(dept, request);
            ExcelKit.$Export(Dept.class, response).downXlsx(depts, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @RequestMapping(value = "/down-template", method = RequestMethod.GET)
    public void downTemplate(HttpServletResponse response) {
        List<Dept> list = deptService.list();
        ExcelKit.$Export(Dept.class, response).downXlsx(list, true);
    }

    @PostMapping("/import")
    //@RequiresPermissions("dept:add")
    @ApiOperation(value = "导入组织机构信息", notes = "导入组织机构信息", tags = ApiTagsConstant.TAG_DEPT, httpMethod = "POST")
    public List importDept(@RequestParam(name = "file") MultipartFile file)
            throws IOException {
        long beginMillis = System.currentTimeMillis();

        List<Dept> successList = Lists.newArrayList();
        List<Map<String, Object>> errorList = Lists.newArrayList();

        ExcelKit.$Import(Dept.class)
                .readXlsx(file.getInputStream(), new ExcelReadHandler<Dept>() {

                    @Override
                    public void onSuccess(int sheetIndex, int rowIndex, Dept entity) {
                        successList.add(entity); // 单行读取成功，加入入库队列。
                    }

                    @Override
                    public void onError(int sheetIndex, int rowIndex,
                                        List<ExcelErrorField> errorFields) {
                        // 读取数据失败，记录了当前行所有失败的数据
//                        errorList.add(MapUtil.newHashMap(//
//                                "sheetIndex", sheetIndex,//
//                                "rowIndex", rowIndex,//
//                                "errorFields", errorFields//
//                        ));
                    }
                });

        // TODO: 执行successList的入库操作。
        log.info("deptList === {}", successList);
        return errorList;
    }


}
