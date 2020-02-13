package com.phlink.bus.api.serviceorg.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.ApiTagsConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.serviceorg.domain.Feedback;
import com.phlink.bus.api.serviceorg.service.IFeedbackService;
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
@RequestMapping("/feedback")
@Api(tags = ApiTagsConstant.TAG_FEEDBACK)
public class FeedbackController extends BaseController {

    @Autowired
    public IFeedbackService feedbackService;

    @GetMapping("/list")
//    //@RequiresPermissions("feedback:view")
    @ApiOperation(value = "列表", notes = "列表", tags = ApiTagsConstant.TAG_FEEDBACK, httpMethod = "GET")
    public Map<String, Object> listFeedback(QueryRequest request, Feedback feedback) {
        return getDataTable(this.feedbackService.listFeedbacks(request, feedback));
    }

    @GetMapping("/{id}")
//    //@RequiresPermissions("feedback:get")
    @ApiOperation(value = "详情", notes = "详情", tags = ApiTagsConstant.TAG_FEEDBACK, httpMethod = "GET")
    public Feedback detail(@PathVariable Long id) {
        return this.feedbackService.findById(id);
    }

    @Log("添加评价建议")
    @PostMapping("/add")
//    //@RequiresPermissions("feedback:add")
    @ApiOperation(value = "添加", notes = "添加", tags = ApiTagsConstant.TAG_FEEDBACK, httpMethod = "POST")
    public void addFeedback(@RequestBody @Valid Feedback feedback) throws BusApiException {
        try {
            this.feedbackService.createFeedback(feedback);
        } catch (Exception e) {
            String message = "新增失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改评价建议")
    @PutMapping
//    //@RequiresPermissions("feedback:update")
    @ApiOperation(value = "修改", notes = "修改", tags = ApiTagsConstant.TAG_FEEDBACK, httpMethod = "PUT")
    public void updateFeedback(@RequestBody @Valid Feedback feedback) throws BusApiException {
        try {
            this.feedbackService.modifyFeedback(feedback);
        } catch (Exception e) {
            String message = "修改失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除评价建议")
    @DeleteMapping("/{feedbackIds}")
//    //@RequiresPermissions("feedback:delete")
    @ApiOperation(value = "删除", notes = "删除", tags = ApiTagsConstant.TAG_FEEDBACK, httpMethod = "DELETE")
    public void deleteFeedback(@NotBlank(message = "{required}") @PathVariable String feedbackIds) throws BusApiException {
        try {
            String[] ids = feedbackIds.split(StringPool.COMMA);
            this.feedbackService.deleteFeedbacks(ids);
        } catch (Exception e) {
            String message = "删除失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("export")
    //@RequiresPermissions("feedback:export")
    @ApiOperation(value = "导出", notes = "导出", tags = ApiTagsConstant.TAG_FEEDBACK, httpMethod = "POST")
    public void exportFeedback(QueryRequest request, @RequestBody Feedback feedback, HttpServletResponse response) throws BusApiException {
        try {
            List<Feedback> feedbacks = this.feedbackService.listFeedbacks(request, feedback).getRecords();
            ExcelKit.$Export(Feedback.class, response).downXlsx(feedbacks, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
