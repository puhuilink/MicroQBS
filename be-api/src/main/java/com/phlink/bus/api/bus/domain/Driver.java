package com.phlink.bus.api.bus.domain;


import com.phlink.bus.api.common.domain.ApiBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@ApiModel("司机")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Driver extends ApiBaseEntity {

    /**
     * ic识别卡号
     */
//    private String icCard;
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
