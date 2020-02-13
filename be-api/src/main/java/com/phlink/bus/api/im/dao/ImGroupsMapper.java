package com.phlink.bus.api.im.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.im.domain.ImGroups;
import org.apache.ibatis.annotations.Param;

/**
 * @author wen
 */
public interface ImGroupsMapper extends BaseMapper<ImGroups> {

    IPage<ImGroups> list(Page<ImGroups> page, @Param("imGroups") ImGroups imGroups);

    void removeByGroupId(@Param("groupIds") String[] groupIds);
}
