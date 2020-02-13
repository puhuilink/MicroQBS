package com.phlink.bus.api.banner.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Description: java类作用描述
 * @Author: 贾志斌
 * @CreateDate: 2019/12/2$ 19:08$
 * @UpdateUser: 贾志斌
 * @UpdateDate: 2019/12/2$ 19:08$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum BannerStatusEnum implements IEnum<String> {
    HASBEENLAUNCHED("1", "已上线"),
    NOTONLINE("2", "未上线"),
    INSERTINGCOIL("3", "已下线"),
    DRAFT("4","草稿")
    ;

    BannerStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    @JsonValue
    @Override
    public String getValue() {
        return this.code;
    }
}
