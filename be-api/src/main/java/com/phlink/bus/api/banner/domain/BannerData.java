package com.phlink.bus.api.banner.domain;

import lombok.Data;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/12$ 17:40$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/12$ 17:40$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class BannerData {

    private Integer code;

    private String msg;

    private List<BannerData> data;

}
