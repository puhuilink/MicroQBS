package com.phlink.bus.api.banner.domain.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/2$ 18:22$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/2$ 18:22$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class BannerVO {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "附件url路径")
    private String fileUrl;

    @ApiModelProperty(value = "链接地址")
    private String linkAddr;
    @ApiModelProperty(value = "轮播图状态")
    private String status;
    @ApiModelProperty(value="轮播图发布时间")
    private LocalDate pudDate;

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

    private Integer sort;
}
