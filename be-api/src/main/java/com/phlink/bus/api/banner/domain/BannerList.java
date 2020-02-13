package com.phlink.bus.api.banner.domain;

import lombok.Data;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/5$ 17:03$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/5$ 17:03$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class BannerList {

    private List<Banner> bannerList;

    private Integer sortD;

}
