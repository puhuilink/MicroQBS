package com.phlink.bus.api.banner.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
/**
* 
*
* @author Maibenben
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_banner")
@Excel("轮播图信息")
public class Banner extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;
    @ExcelField("轮播图标题")
    private String title;

    @TableField("file_url")
    @ExcelField("附件路径")
    private String fileUrl;

    @TableField("linkaddr")
    @ExcelField("链接路径")
    private String linkAddr;
    @ExcelField(value="状态",writeConverterExp = "1=已上线,2=已下线,3=草稿")
    private Integer status;

    @TableField("pubdate")
    @ExcelField("发布时间")
    private LocalDate pubDate;

    private Integer sort;

}
