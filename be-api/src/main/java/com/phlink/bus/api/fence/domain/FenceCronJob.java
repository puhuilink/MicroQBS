package com.phlink.bus.api.fence.domain;

import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class FenceCronJob {

    @ApiModelProperty("启动任务id")
    @NotNull(message = "{required}", groups = {OnUpdate.class})
    private Long startJobId;

    @ApiModelProperty(value = "启动任务时间",example ="08:30")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private LocalTime startCron;

    @ApiModelProperty("停止任务id")
    @NotNull(message = "{required}", groups = {OnUpdate.class})
    private Long stopJobId;

    @ApiModelProperty(value = "停止任务时间",example ="08:30")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private LocalTime stopCron;

    @ApiModelProperty("停止任务删除标识")
    @NotNull(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private Boolean deleted;

/*    public static void main(String[] args) {
        List<FenceCronJob> list=new ArrayList<>();
        for (int i = 1; i <5 ; i++) {
            FenceCronJob cron = new FenceCronJob();
            cron.setDeleted(false);
            cron.setStartCron(LocalTime.of(5+i,30));
            cron.setStopCron(LocalTime.of(6+i,30));
            list.add(cron);
        }
        System.out.println(JSON.toJSONString(list));
    }*/
}


