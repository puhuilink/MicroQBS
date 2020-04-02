/*
 * @Author: sevncz.wen
 * @Date: 2020-04-01 21:35:42
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-04-01 21:37:13
 */
package com.phlink.demo.transfer.schedule;

import com.phlink.demo.transfer.service.KpiCurrentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * KpiSchedule
 */
@Component
public class KpiSchedule {

    @Autowired
    private KpiCurrentService kpiCurrentService;

    public void readFromPg(){

    }

}
