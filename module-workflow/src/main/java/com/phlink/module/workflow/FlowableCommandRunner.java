/*
 * @Author: sevncz.wen
 * @Date: 2020-03-30 21:58:05
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-03-30 21:58:05
 */
package com.phlink.module.workflow;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * FlowableCommandRunner
 */
@Slf4j
@Component
public class FlowableCommandRunner implements CommandLineRunner {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Number of process definitions : " + repositoryService.createProcessDefinitionQuery().count());
        log.info("Number of tasks : " + taskService.createTaskQuery().count());
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        log.info("Number of tasks after process start: " + taskService.createTaskQuery().count());

    }

}
