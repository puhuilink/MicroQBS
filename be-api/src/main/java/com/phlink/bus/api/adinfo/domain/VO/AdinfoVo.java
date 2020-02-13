package com.phlink.bus.api.adinfo.domain.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/3$ 14:03$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/3$ 14:03$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class AdinfoVo {
    /**
     * 公告标题
     */
    @ApiModelProperty(value = "公告标题")
    private String title;

    /**
     * 公告类型
     */
    @ApiModelProperty(value = "公告类型")
    private Integer type;

    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容")
    private String content;

    /**
     * 公告状态
     */
    @ApiModelProperty(value = "公告状态")
    private Integer status;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    @TableField("pubDate")
    private LocalDate pubDate;
    /**
     * 开始发布发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    /**
     * 结束发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;
    @TableField("text")
    private String text;
}
