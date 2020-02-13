package com.phlink.bus.api.adinfo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
/**
* 
*
* @author Maibenben
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_adinfo")
@Excel(value = "公告管理")
public class Adinfo extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 公告标题
     */
    @ExcelField(value="公告标题")
    @ApiModelProperty(value = "公告标题")
    private String title;

    /**
     * 公告类型
     */
    @ApiModelProperty(value = "公告类型")
    @ExcelField(value="公告类型",writeConverterExp = "1=通知,2=公告,3=新闻")
    private Integer type;

    /**
     * 公告内容
     */

    private String content;

    /**
     * 公告状态
     */
    @ApiModelProperty(value = "公告状态")
    @ExcelField(value="公告状态", writeConverterExp = "1=已上线,2=已下线,3=草稿")
    private Integer status;

    /**
     * 发布时间
     */
    @TableField("pubdate")
    @ExcelField("发布时间")
    private LocalDate pubDate;
    @ApiModelProperty(value = "公告内容")
    @ExcelField(value="公告内容")
    private String text;


}
