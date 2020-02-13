package com.phlink.bus.api.large.domain;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import lombok.Data;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/14$ 14:02$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/14$ 14:02$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class BusDetailVo extends Bus {

    private Long userId;
    private String stopName;
    private String driverName;
    private String driverMobile;
    private String busTeacherName;
    private String busTeacherMobile;
    private String tirpId;
    private DvrLocation location;

}
