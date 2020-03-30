/*
 * @Author: sevncz.wen
 * @Date: 2020-03-30 21:57:24
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-03-30 22:14:25
 */
package com.phlink.module.workflow.controller;

import java.util.ArrayList;
import java.util.List;

import com.phlink.module.workflow.service.MyFlowableService;

import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * MyFlowableController
 */
@RestController
public class MyFlowableController {
    @Autowired
    private MyFlowableService myFlowableService;

    @RequestMapping(value="/process", method= RequestMethod.POST)
    public void startProcessInstance() {
        myFlowableService.startProcess();
    }

    @RequestMapping(value="/tasks", method= RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<TaskRepresentation> getTasks() {
        List<Task> tasks = myFlowableService.listAll();
        List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }

    static class TaskRepresentation {

        private String id;
        private String name;

        public TaskRepresentation(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    }

}
