package com.phlink.bus.api.serviceorg.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* 家庭信息，暂时不用
*
* @author wen
*/
@Deprecated
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_family")
public class Family extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 孩子ID列表
     */
    @ApiModelProperty(value = "孩子ID列表")
    private Long[] childrenIds;

    /**
     * 家长ID列表
     */
    @ApiModelProperty(value = "家长ID列表")
    private Long[] guardianIds;

    /**
     * 主责ID
     */
    @ApiModelProperty(value = "主责ID")
    private Long mainGuardianId;

    /**
     * 请假人ID，暂时不启用
     */
    @Deprecated
    @ApiModelProperty(value = "请假人ID")
    private Long leavePermissions;


    public void addChildren(Long studentId) {
        if(studentId == null) {
            return;
        }
        if(this.childrenIds == null) {
            this.childrenIds = new Long[]{studentId};
        }else{
            List<Long> studentIdList = new ArrayList<>(Arrays.asList(this.childrenIds));
            studentIdList.add(studentId);
            this.childrenIds = studentIdList.stream().distinct().filter(Objects::nonNull).collect(Collectors.toList()).toArray(new Long[]{});
        }
    }

    public void addGuardian(Long guardianId) {
        if(guardianId == null) {
            return;
        }
        if(this.guardianIds == null) {
            this.guardianIds = new Long[]{guardianId};
        }else{
            List<Long> guardianIdList = new ArrayList<>(Arrays.asList(this.guardianIds));
            guardianIdList.add(guardianId);
            this.guardianIds = guardianIdList.stream().distinct().filter(Objects::nonNull).collect(Collectors.toList()).toArray(new Long[]{});
        }
    }
}
