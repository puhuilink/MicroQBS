package com.puhuilink.qbs.core.common.base;

import com.puhuilink.qbs.core.base.gson.annotation.Exclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordInfo<T> extends QbsBaseEntity {

    @Exclude
    String createBy;

    @Exclude
    LocalDateTime createTime;

    @Exclude
    String updateBy;

    @Exclude
    LocalDateTime updateTime;

    public T createdBy(String userId) {
        this.createBy = userId;
        this.createTime = LocalDateTime.now();
        return (T) this;
    }

    public void updatedBy(String userId) {
        this.updateBy = userId;
        this.updateTime = LocalDateTime.now();
    }
}