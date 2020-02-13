package com.phlink.bus.api.job.controller;

import com.phlink.bus.api.common.annotation.Log;
import com.phlink.bus.api.common.controller.BaseController;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.job.domain.Job;
import com.phlink.bus.api.job.service.JobService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("job")
public class JobController extends BaseController {

    private String message;

    @Autowired
    private JobService jobService;

    @GetMapping
    //@RequiresPermissions("job:view")
    public Map<String, Object> jobList(QueryRequest request, Job job) {
        return getDataTable(this.jobService.findJobs(request, job));
    }

    @GetMapping("cron/check")
    public boolean checkCron(String cron) {
        try {
            return CronExpression.isValidExpression(cron);
        } catch (Exception e) {
            return false;
        }
    }

    @Log("新增定时任务")
    @PostMapping
    //@RequiresPermissions("job:add")
    public void addJob(@Valid Job job) throws BusApiException {
        try {
            this.jobService.createJob(job);
        } catch (Exception e) {
            message = "新增定时任务失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("删除定时任务")
    @DeleteMapping("/{jobIds}")
    //@RequiresPermissions("job:delete")
    public void deleteJob(@NotBlank(message = "{required}") @PathVariable String jobIds) throws BusApiException {
        try {
            String[] ids = jobIds.split(StringPool.COMMA);
            this.jobService.deleteJobs(ids);
        } catch (Exception e) {
            message = "删除定时任务失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("修改定时任务")
    @PutMapping
    //@RequiresPermissions("job:update")
    public void updateJob(@Valid Job job) throws BusApiException {
        try {
            this.jobService.updateJob(job);
        } catch (Exception e) {
            message = "修改定时任务失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("执行定时任务")
    @GetMapping("run/{jobId}")
    //@RequiresPermissions("job:run")
    public void runJob(@NotBlank(message = "{required}") @PathVariable String jobId) throws BusApiException {
        try {
            this.jobService.run(jobId);
        } catch (Exception e) {
            message = "执行定时任务失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("暂停定时任务")
    @GetMapping("pause/{jobId}")
    //@RequiresPermissions("job:pause")
    public void pauseJob(@NotBlank(message = "{required}") @PathVariable String jobId) throws BusApiException {
        try {
            this.jobService.pause(jobId);
        } catch (Exception e) {
            message = "暂停定时任务失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @Log("恢复定时任务")
    @GetMapping("resume/{jobId}")
    //@RequiresPermissions("job:resume")
    public void resumeJob(@NotBlank(message = "{required}") @PathVariable String jobId) throws BusApiException {
        try {
            this.jobService.resume(jobId);
        } catch (Exception e) {
            message = "恢复定时任务失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }

    @PostMapping("excel")
    //@RequiresPermissions("job:export")
    public void export(QueryRequest request, Job job, HttpServletResponse response) throws BusApiException {
        try {
            List<Job> jobs = this.jobService.findJobs(request, job).getRecords();
            ExcelKit.$Export(Job.class, response).downXlsx(jobs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new BusApiException(message);
        }
    }
}
