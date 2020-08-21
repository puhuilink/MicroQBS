package com.puhuilink.qbs.core.common.base;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordInfo<T> extends QbsBaseEntity{

    @Expose(serialize = false, deserialize = true)
    Long createBy;

    @Expose(serialize = false, deserialize = true)
    LocalDateTime createTime;

    @Expose(serialize = false, deserialize = true)
    Long updateBy;

    @Expose(serialize = false, deserialize = true)
    LocalDateTime updateTime;

    public T createdBy(Long userId) {
        this.createBy = userId;
        this.createTime = LocalDateTime.now();
        return (T) this;
    }

    public void updatedBy(Long userId) {
        this.updateBy = userId;
        this.updateTime = LocalDateTime.now();
    }
}