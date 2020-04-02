/*
 * @Author: sevncz.wen
 * @Date: 2020-04-01 18:18:24
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-04-02 15:32:05
 */
package com.phlink.demo.transfer.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_kpi_current")
@ApiModel(value = "KPI")
public class KpiCurrent {

    private Long kpiCode;
    private Long ciId;
    private String instanceId;
    private Long taskId;
    private Integer arisingIndex;
    private Integer kpiValueNum;
    private String kpiValueTxt;
    private LocalDateTime arisingTime;
    private LocalDateTime insertTime;
    private String agentId;
    private String notes;

}
