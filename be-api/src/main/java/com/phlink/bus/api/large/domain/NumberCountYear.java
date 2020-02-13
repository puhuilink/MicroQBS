package com.phlink.bus.api.large.domain;

import lombok.Data;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/12$ 16:18$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/12$ 16:18$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class NumberCountYear {

    private Long ydrs=Long.valueOf(0);

    private Long sdrs=Long.valueOf(0);

    private Long qjrs=Long.valueOf(0);

    private String months;

}
