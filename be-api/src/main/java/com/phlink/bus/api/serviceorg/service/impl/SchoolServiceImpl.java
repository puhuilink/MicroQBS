package com.phlink.bus.api.serviceorg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.serviceorg.dao.SchoolMapper;
import com.phlink.bus.api.serviceorg.domain.School;
import com.phlink.bus.api.serviceorg.domain.VO.SchoolViewVO;
import com.phlink.bus.api.serviceorg.service.ISchoolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wen
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {

    @Override
    public IPage<School> listSchools(QueryRequest request, SchoolViewVO schoolViewVO) {
        Page<School> page = new Page<>();
        page.setSize(request.getPageSize());
        page.setCurrent(request.getPageNum());
        return baseMapper.listSchools(page, schoolViewVO);
    }

    @Override
    public List<School> listSchools(SchoolViewVO schoolViewVO) {
        Page<School> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        baseMapper.listSchools(page, schoolViewVO);
        return page.getRecords();
    }

    @Override
    public List<School> listSchools(School school) {
        QueryWrapper<School> queryWrapper = new QueryWrapper<>();
        if (school.getSchoolName() != null){
            queryWrapper.lambda().like(School::getSchoolName, school.getSchoolName());
        }
        queryWrapper.orderByAsc("school_name");
        return this.list(queryWrapper);
    }

    @Override
    @Transactional
    public void createSchool(School school) throws BusApiException {
        if(this.findByName(school.getSchoolName()) != null) {
            throw new BusApiException("学校名重复");
        }
        school.setCreateTime(LocalDateTime.now());
        school.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(school);
    }

    @Override
    @Transactional
    public void modifySchool(School school) {
        school.setModifyTime(LocalDateTime.now());
        school.setModifyBy(BusApiUtil.getCurrentUser().getUserId());
        this.updateById(school);
    }

    @Override
    public void deleteSchoolIds(String[] schoolIds) {
        List<Long> list = Stream.of(schoolIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public School findByName(String schoolName) {
        LambdaQueryWrapper<School> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(School::getSchoolName, schoolName);
        queryWrapper.last("limit 1");
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public School createSchool(String schoolName) {
        School school = this.findByName(schoolName);
        if(school != null) {
            return school;
        }
        school = new School();
        school.setSchoolName(schoolName);
        school.setCreateTime(LocalDateTime.now());
        school.setCreateBy(BusApiUtil.getCurrentUser().getUserId());
        this.save(school);
        return school;
    }
}
