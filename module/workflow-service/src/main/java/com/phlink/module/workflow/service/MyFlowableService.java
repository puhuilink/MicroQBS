/*
 * @Author: sevncz.wen
 * @Date: 2020-03-30 21:57:45
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-03-30 22:14:10
 */
package com.phlink.module.workflow.service;

import java.util.List;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MyFlowableService
 */
@Service
public class MyFlowableService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Transactional
    public void startProcess() {
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
    }

    @Transactional
    public List<Task> listAll() {
        return taskService.createTaskQuery().list();
    }

}
