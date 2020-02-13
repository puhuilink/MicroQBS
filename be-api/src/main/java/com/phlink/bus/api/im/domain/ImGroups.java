package com.phlink.bus.api.im.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.phlink.bus.api.common.controller.validation.OnAdd;
import com.phlink.bus.api.common.controller.validation.OnUpdate;
import com.phlink.bus.api.common.domain.ApiBaseEntity;
import com.phlink.bus.api.im.domain.enums.GroupTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
* 对接IM的群组管理
*
* @author wen
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_im_groups")
public class ImGroups extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 0：自建群  1：部门官方群  2：公司官方群
     */
    @ApiModelProperty(value = "0：自建群  1：部门官方群  2：公司官方群")
    private GroupTypeEnum type;

    /**
     * 部门Id，如果是部门官方群，该参数必填
     */
    @ApiModelProperty(value = "部门Id，如果是部门官方群，该参数必填")
    private String departId;

    /**
     * 公司Id，如果是公司官方群，该参数必填
     */
    @ApiModelProperty(value = "公司Id，如果是公司官方群，该参数必填")
    private String companyId;

    /**
     * 群组名称
     */
    @ApiModelProperty(value = "群组名称")
    @NotNull(message = "{required}", groups = {OnUpdate.class, OnAdd.class})
    private String name;

    /**
     * 成员Id列表
     */
    @ApiModelProperty(value = "成员Id列表")
    private Long[] memberIds;

    /**
     * 群主Id，如果为空，默认第一个入群人员为群主
     */
    @ApiModelProperty(value = "群主Id，如果为空，默认第一个入群人员为群主")
    private Long managerId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("操作时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty("操作人")
    private Long modifyBy;


    @ApiModelProperty("开始时间")
    private transient String createTimeFrom;
    @ApiModelProperty("结束时间")
    private transient String createTimeTo;
    @ApiModelProperty("群组ID")
    @NotNull(message = "{required}", groups = {OnUpdate.class})
    private String groupId;

    @ApiModelProperty("群主名")
    private transient String managerName;
    @ApiModelProperty("群主手机号")
    private transient String managerMobile;

//    public LocalDateTime getCreateTimeFrom(){
//        return DateUtil.formatDateTimeStr(this.createTimeFrom);
//    }
//
//    public LocalDateTime getCreateTimeTo(){
//        return DateUtil.formatDateTimeStr(this.createTimeTo);
//    }

    public void addMembers(Long[] staffIds) {
        if(staffIds == null) {
            return;
        }
        if(this.memberIds == null) {
            this.memberIds = staffIds;
        }else{
            Set<Long> idSet = new HashSet<>(Arrays.asList(staffIds));
            idSet.addAll(Arrays.asList(this.memberIds));
            this.memberIds = idSet.toArray(new Long[0]);
        }
    }

    public void removeMembers(Long[] staffIds) {
        if(staffIds == null || this.memberIds == null) {
            return;
        }
//        List<Long> memberList = new ArrayList<>(Arrays.asList(this.memberIds));
//        memberList.removeAll(staffIds);
        this.memberIds = ArrayUtils.removeElements(this.memberIds, staffIds);
    }
}
