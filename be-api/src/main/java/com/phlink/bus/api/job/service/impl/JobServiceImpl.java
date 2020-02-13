package com.phlink.bus.api.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.job.dao.JobMapper;
import com.phlink.bus.api.job.domain.Job;
import com.phlink.bus.api.job.service.JobService;
import com.phlink.bus.api.job.util.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service("JobService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<Job> scheduleJobList = this.baseMapper.queryList();
        // 如果不存在，则创建
        scheduleJobList.forEach(scheduleJob -> {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        });
    }

    @Override
    public Job findJob(Long jobId) {
        return this.getById(jobId);
    }

    @Override
    public IPage<Job> findJobs(QueryRequest request, Job job) {
        try {
            LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(job.getBeanName())) {
                queryWrapper.eq(Job::getBeanName, job.getBeanName());
            }
            if (StringUtils.isNotBlank(job.getMethodName())) {
                queryWrapper.eq(Job::getMethodName, job.getMethodName());
            }
            if (StringUtils.isNotBlank(job.getParams())) {
                queryWrapper.like(Job::getParams, job.getParams());
            }
            if (StringUtils.isNotBlank(job.getRemark())) {
                queryWrapper.like(Job::getRemark, job.getRemark());
            }
            if (StringUtils.isNotBlank(job.getStatus())) {
                queryWrapper.eq(Job::getStatus, job.getStatus());
            }
            Page<Job> page = new Page<>(request.getPageNum(), request.getPageSize());
            SortUtil.handlePageSort(request, page, "id", BusApiConstant.ORDER_DESC, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取任务失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void createJob(Job job) {
        job.setCreateTime(new Date());
        job.setStatus(Job.ScheduleStatus.PAUSE.getValue());
        this.save(job);
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    @Override
    @Transactional
    public void createAndRunJob(Job job) {
        job.setCreateTime(new Date());
        job.setStatus(Job.ScheduleStatus.NORMAL.getValue());
        this.save(job);
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    @Override
    @Transactional
    public void updateJob(Job job) {
        ScheduleUtils.updateScheduleJob(scheduler, job);
        this.baseMapper.updateById(job);
    }

    @Override
    @Transactional
    public void deleteJobs(String[] jobIds) {
        if(jobIds == null || jobIds.length == 0) {
            return;
        }
        List<Long> list = Stream.of(jobIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        list.forEach(jobId -> ScheduleUtils.deleteScheduleJob(scheduler, jobId));
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public int updateBatch(String jobIds, String status) {
        List<Long> list = Stream.of(jobIds.split(StringPool.COMMA))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        Job job = new Job();
        job.setStatus(status);
        return this.baseMapper.update(job, new LambdaQueryWrapper<Job>().in(Job::getJobId, list));
    }

    @Override
    @Transactional
    public void run(String jobIds) {
        String[] list = jobIds.split(StringPool.COMMA);
        Arrays.stream(list).forEach(jobId -> ScheduleUtils.run(scheduler, this.findJob(Long.valueOf(jobId))));
    }

    @Override
    @Transactional
    public void pause(String jobIds) {
        String[] list = jobIds.split(StringPool.COMMA);
        Arrays.stream(list).forEach(jobId -> ScheduleUtils.pauseJob(scheduler, Long.valueOf(jobId)));
        this.updateBatch(jobIds, Job.ScheduleStatus.PAUSE.getValue());
    }

    @Override
    @Transactional
    public void resume(String jobIds) {
        String[] list = jobIds.split(StringPool.COMMA);
        Arrays.stream(list).forEach(jobId -> ScheduleUtils.resumeJob(scheduler, Long.valueOf(jobId)));
        this.updateBatch(jobIds, Job.ScheduleStatus.NORMAL.getValue());
    }

    @Override
    public void deleteJobByParam(String param) {
        List<Job> list = this.queryJobByParam(param);
        list.forEach(job -> ScheduleUtils.deleteScheduleJob(scheduler, job.getJobId()));
        list.forEach(job -> this.removeById(job.getJobId()));
    }

    @Override
    public List<Job> queryJobByParam(String param) {
        QueryWrapper<Job> query = new QueryWrapper<>();
        query.lambda().eq(Job::getParams, param);
        return this.list(query);
    }

    @Override
    @Transactional
    public void saveJob(Job job, boolean deleted) {
        if (job.getJobId() != null && deleted) {
            String[] ids = {String.valueOf(job.getJobId())};
            deleteJobs(ids);
        }
        if (job.getJobId() == null && !deleted) {
            createAndRunJob(job);
        }
        if (job.getJobId() != null && !deleted) {
            updateJob(job);
        }
    }
}
