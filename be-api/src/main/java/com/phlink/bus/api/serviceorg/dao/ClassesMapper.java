package com.phlink.bus.api.serviceorg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phlink.bus.api.serviceorg.domain.Classes;
import com.phlink.bus.api.serviceorg.domain.VO.ClassesViewVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author zhouyi
 */
public interface ClassesMapper extends BaseMapper<Classes> {

    String listGradeClassesCascade(Long schoolId);

    @Update("update t_classes set grade=grade+1 where grade<school_system;")
    void gradeUp();

    @Update("update t_classes set grade_pro=grade_pro+1;")
    void gradeProUp();

    Page<Classes> listClassess(Page page, @Param("classesViewVO") ClassesViewVO classesViewVO);

    Classes getOne(@Param("schoolId") Long schoolId, @Param("grade") Integer grade, @Param("classLevel") Integer classLevel);
}
