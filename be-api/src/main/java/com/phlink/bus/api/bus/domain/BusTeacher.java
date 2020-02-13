package com.phlink.bus.api.bus.domain;


import com.phlink.bus.api.common.domain.ApiBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@ApiModel("随车老师")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BusTeacher extends ApiBaseEntity {

    /**
     * 工号
     */
//    private String workNumber;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String realname;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String contactNumber;

}
