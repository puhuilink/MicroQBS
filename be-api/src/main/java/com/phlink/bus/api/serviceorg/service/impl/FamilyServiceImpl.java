package com.phlink.bus.api.serviceorg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.serviceorg.dao.FamilyMapper;
import com.phlink.bus.api.serviceorg.domain.Classes;
import com.phlink.bus.api.serviceorg.domain.Family;
import com.phlink.bus.api.serviceorg.service.IFamilyService;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author wen
*/
@Service
public class FamilyServiceImpl extends ServiceImpl<FamilyMapper, Family> implements IFamilyService {

    @Autowired
    private FamilyMapper familyMapper;


    @Override
    public Family findById(Long id){
        return this.getById(id);
    }

    @Override
    @Transactional
    public void createFamily(Family family) {
        this.save(family);
    }

    @Override
    @Transactional
    public void modifyFamily(Family family) {
        this.updateById(family);
    }

    @Override
    public void deleteFamilys(String[] familyIds) {
        List<Long> list = Stream.of(familyIds)
        .map(Long::parseLong)
        .collect(Collectors.toList());
        removeByIds(list);
    }

    @Override
    public Family getByStudentAndMainGuardian(Long studentId, Long mainGuardianId) {
        return familyMapper.getByStudentAndMainGuardian(studentId, mainGuardianId);
    }
}
