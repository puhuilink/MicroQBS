package com.phlink.bus.api.bus.domain;

    import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
    import com.phlink.bus.api.common.annotation.DistributedLockParam;
    import com.phlink.bus.api.common.domain.ApiBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
* 
*
* @author wen
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_my_test")
public class MyTest extends ApiBaseEntity {

    private static final long serialVersionUID = 1L;
    @DistributedLockParam(name = "name")
    private String name;

    private Integer[] arrayTest;

    private JSONObject jsonTest;

    private LocalDateTime createTime;

    private Long createBy;

    private LocalDateTime modifyTime;

    private Long modifyBy;
    /**
    * 排序字段
    */
    private transient String sortField;

    /**
    * 排序规则 ascend 升序 descend 降序
    */
    private transient String sortOrder;

    /**
    * 创建时间--开始时间
    */
    private transient Long createTimeFrom;

    /**
    * 创建时间--结束时间
    */
    private transient Long createTimeTo;


}
