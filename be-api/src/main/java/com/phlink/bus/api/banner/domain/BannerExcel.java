package com.phlink.bus.api.banner.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/5$ 11:09$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/5$ 11:09$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class BannerExcel {

    private static final long serialVersionUID = 1L;

    @ExcelField("编号")
    private Long id;
    @ExcelField("轮播图标题")
    private String title;

    @ExcelField("附件路径")
    private String fileUrl;

    @ExcelField("链接路径")
    private String linkAddr;
    @ExcelField(value="状态",writeConverterExp = "1=已上线,2=已下线,3=草稿")
    private Integer status;

    @ExcelField("发布时间")
    private LocalDate pubDate;

}
