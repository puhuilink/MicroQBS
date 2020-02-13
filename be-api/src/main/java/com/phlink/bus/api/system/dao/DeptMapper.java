package com.phlink.bus.api.system.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.phlink.bus.api.system.domain.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptMapper extends BaseMapper<Dept> {

	/**
	 * 递归删除部门
	 *
	 * @param deptId deptId
	 */
	void deleteDepts(String deptId);

	/**
	 * 递归获得所有子部门
	 * @param deptId
	 * @return
	 */
    List<Dept> listChildrenDepts(Long deptId);

	/**
	 * 递归查询获得部门列表
	 * @param queryWrapper
	 * @return
	 */
	List<Dept> listDepts(@Param(Constants.WRAPPER) QueryWrapper<Dept> queryWrapper);
}