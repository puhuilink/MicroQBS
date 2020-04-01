/*
 * @Author: sevncz.wen
 * @Date: 2020-04-01 18:18:24
 * @Last Modified by:   sevncz.wen
 * @Last Modified time: 2020-04-01 18:18:24
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

    private Integer kpiCode;
    private Integer ciId;
    private String instanceId;
    private Integer taskId;
    private Integer arisingIndex;
    private Integer kpiValueNum;
    private String kpiValueTxt;
    private LocalDateTime arisingTime;
    private LocalDateTime insertTime;
    private String agentId;
    private String notes;

}
