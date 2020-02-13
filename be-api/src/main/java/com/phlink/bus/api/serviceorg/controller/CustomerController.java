package com.phlink.bus.api.serviceorg.controller;

import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.im.service.IImService;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.VO.CustomerUpdateVO;
import com.phlink.bus.api.serviceorg.domain.VO.UserGuardianVo;
import com.phlink.bus.api.serviceorg.domain.VO.UserVisitorVo;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.UserService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/customer")
@Api(value = ApiTagsConstant.TAG_CUSTOMER)
public class CustomerController extends BaseController {

    @Autowired
    public UserService userService;
    @Autowired
    public IGuardianService guardianService;
    @Autowired
    public IImService imService;

    @GetMapping
    //@RequiresPermissions("customer:view")
    @ApiOperation(value = "监护人列表", notes = "监护人列表", tags = ApiTagsConstant.TAG_CUSTOMER, httpMethod = "GET")
    public Map<String, Object> listGuardian(QueryRequest queryRequest, User user) {
        return getDataTable(userService.listGuardianDetail(user, queryRequest));
    }

    @GetMapping("/visitor")
    //@RequiresPermissions("customer:view")
    @ApiOperation(value = "游客列表", notes = "游客列表", tags = ApiTagsConstant.TAG_CUSTOMER, httpMethod = "GET")
    public Map<String, Object> listVisitor(QueryRequest queryRequest, User user) {
        return getDataTable(userService.listVisitorDetail(user, queryRequest));
    }

    @Log("监护人信息编辑")
    @PutMapping
    //@RequiresPermissions("customer:update")
    @ApiOperation(value = "监护人信息编辑", notes = "监护人信息编辑", tags = ApiTagsConstant.TAG_CUSTOMER, httpMethod = "PUT")
    public void updateCustomer(@Valid @RequestBody CustomerUpdateVO customerUpdateVO) throws Exception {
        User user = userService.getById(customerUpdateVO.getUserId());
        if (user != null) {
            user.setRealname(customerUpdateVO.getRealname());
            user.setMobile(customerUpdateVO.getMobile());
            user.setIdcard(customerUpdateVO.getIdcard());
            userService.updateProfile(user);
            imService.updateCustomer(user);
        }else {
            throw new BusApiException("用户不存在");
        }

        // 更新家长
        Guardian guardian = guardianService.getByGuardianId(customerUpdateVO.getUserId());
        if (guardian != null) {
//            throw new BusApiException("家长信息不存在");
            guardian.setIdcard(customerUpdateVO.getIdcard());
            guardianService.updateById(guardian);
        }

    }

    @PostMapping("export")
    //@RequiresPermissions("customer:export")
    @ApiOperation(value = "监护人列表导出", notes = "监护人列表导出", tags = ApiTagsConstant.TAG_CUSTOMER, httpMethod = "POST")
    public void exportGuardian(@RequestBody @Valid User user, HttpServletResponse response) throws BusApiException {
        try {
            List<User> guardianList = this.userService.listGuardianDetail(user);

            List<UserGuardianVo> vos = guardianList.stream().map( g -> {
                UserGuardianVo vo = new UserGuardianVo();
                BeanUtils.copyProperties(g, vo);
                return vo;
            }).collect(Collectors.toList());

            ExcelKit.$Export(UserGuardianVo.class, response).downXlsx(vos, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("/visitor/export")
    //@RequiresPermissions("customer:export")
    @ApiOperation(value = "监护人列表导出", notes = "监护人列表导出", tags = ApiTagsConstant.TAG_CUSTOMER, httpMethod = "POST")
    public void exportVisitor(@RequestBody @Valid User user, HttpServletResponse response) throws BusApiException {
        try {
            List<User> visitorList = this.userService.listVisitorDetail(user);
            List<UserVisitorVo> vos = visitorList.stream().map( g -> {
                UserVisitorVo vo = new UserVisitorVo();
                BeanUtils.copyProperties(g, vo);
                return vo;
            }).collect(Collectors.toList());

            ExcelKit.$Export(UserVisitorVo.class, response).downXlsx(vos, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

}
