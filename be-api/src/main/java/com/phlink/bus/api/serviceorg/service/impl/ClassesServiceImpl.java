package com.phlink.bus.api.serviceorg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.serviceorg.dao.ClassesMapper;
import com.phlink.bus.api.serviceorg.domain.Classes;
import com.phlink.bus.api.serviceorg.domain.VO.ClassesViewVO;
import com.phlink.bus.api.serviceorg.service.IClassesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouyi
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ClassesServiceImpl extends ServiceImpl<ClassesMapper, Classes> implements IClassesService {

    @Override
    public Classes findById(Long id) {
        return this.getById(id);
    }

    @Override
    public IPage<Classes> listClassess(QueryRequest request, ClassesViewVO classesViewVO){
        Page<Classes> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", BusApiConstant.ORDER_DESC, true);
        return baseMapper.listClassess(page,classesViewVO);
    }

    @Override
    public List<Classes> listClassess(ClassesViewVO classesViewVO){
        Page<Classes> page = new Page<>();
        page.setSearchCount(false);
        page.setSize(-1);
        page.setDesc("id");
        baseMapper.listClassess(page,classesViewVO);
        return page.getRecords();
    }

    @Override
    @Transactional
    public void createClasses(Classes classes) throws BusApiException {
        try {
            Classes classesOne = getOne(classes.getSchoolId(), classes.getEnrollYear(), classes.getGrade(), classes.getClassLevel());

            if(classesOne != null) {
                throw new BusApiException("班级已存在");
            }
        }catch (Exception e) {
            throw new BusApiException("班级已存在");
        }
        classes.setCreateTime(LocalDateTime.now());
        classes.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        // 默认gradePro 和 grade 一致
        classes.setGradePro(classes.getGrade());
        // 默认为6年制小学
        classes.setSchoolSystem(6);
        this.save(classes);
    }

    @Override
    @Transactional
    public void modifyClasses(Classes classes) throws BusApiException {
        try {
            Classes classesOne = getOne(classes.getSchoolId(), classes.getEnrollYear(), classes.getGrade(), classes.getClassLevel());

            if(classesOne != null && classesOne.getId().equals(classes.getId())) {
                throw new BusApiException("班级已存在");
            }
        }catch (Exception e) {
            throw new BusApiException("班级已存在");
        }

        classes.setModifyTime(LocalDateTime.now());
        classes.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(classes);
    }

    @Override
    public void deleteClassess(String[] classesIds) {
        List<Long> list = Stream.of(classesIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void gradeUp() {
        this.baseMapper.gradeUp();
        this.baseMapper.gradeProUp();
    }

    @Override
    public List<Classes> getClassesBySchoolId(Long schoolId) {
        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Classes::getSchoolId, schoolId);
        return this.list(queryWrapper);
    }

    @Override
    public String listGradeClassesCascade(Long schoolId) {

        return this.baseMapper.listGradeClassesCascade(schoolId);
    }

    @Override
    public Classes getOneByQuery(Long schoolId, Integer grade, Integer classLevel) {
        return baseMapper.getOne(schoolId, grade, classLevel);
    }

    @Override
    public Classes getOne(Long schoolId, Integer startYear, Integer grade, Integer classLevel) {
        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Classes::getSchoolId, schoolId);
        queryWrapper.lambda().eq(Classes::getEnrollYear, startYear);
        queryWrapper.lambda().eq(Classes::getGrade, grade);
        queryWrapper.lambda().eq(Classes::getClassLevel, classLevel);
        return getOne(queryWrapper);
    }
}
