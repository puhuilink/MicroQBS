package com.phlink.bus.api.system.dao;

import com.phlink.bus.api.system.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    IPage<User> findUserDetail(Page page, @Param("user") User user);

    /**
     * 获取单个用户详情
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findDetail(String username);

    IPage<User> listGuardianDetail(Page<User> page, @Param("user") User user);

    /**
     * 根据部门ID列表获取所有用户
     * @param deptIdList
     * @return
     */
    List<User> listByDeptList(@Param("deptIdList") Long[] deptIdList);
    
    /**
     * 根据手机号查询出信息
     * @param mobile
     * @return
     */
    User findDetalByMobile(@Param("mobile") String mobile);

    List<User> listOnRouteByWorkerId(@Param("busTeacherId") Long busTeacherId);

    List<User> listOnRouteByBusId(@Param("busId") Long busId);

    IPage<User> listVisitorDetail(Page<User> page, @Param("user") User user);

    /**
     * 获得车队队长信息
     * @param busCode
     * @return
     */
    List<User> listBusLeaderByBus(@Param("busCode") String busCode);

    /**
     * 获取车辆绑定的司机信息
     * @param busCode
     * @return
     */
    User getDriverByBusCode(@Param("busCode") String busCode);

    List<User> listGuardianByStudent(@Param("studentId") Long studentId);

    List<User> listByRoleId(@Param("roleId") Long roleId);
}