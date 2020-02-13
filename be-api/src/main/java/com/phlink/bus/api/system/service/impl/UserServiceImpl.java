package com.phlink.bus.api.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.BusApiException;
import com.phlink.bus.api.common.service.CacheService;
import com.phlink.bus.api.common.utils.BusApiUtil;
import com.phlink.bus.api.common.utils.MD5Util;
import com.phlink.bus.api.common.utils.SortUtil;
import com.phlink.bus.api.im.response.CommonResultEntity;
import com.phlink.bus.api.im.service.IImService;
import com.phlink.bus.api.serviceorg.domain.enums.GuardianStatusEnum;
import com.phlink.bus.api.system.dao.UserMapper;
import com.phlink.bus.api.system.dao.UserRoleMapper;
import com.phlink.bus.api.system.domain.Dept;
import com.phlink.bus.api.system.domain.Role;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.domain.UserRole;
import com.phlink.bus.api.system.domain.enums.UserTypeEnum;
import com.phlink.bus.api.system.manager.UserManager;
import com.phlink.bus.api.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Lazy
    @Autowired
    private UserManager userManager;
    @Lazy
    @Autowired
    private IImService imService;
    @Lazy
    @Autowired
    private DeptService deptService;


    @Override
    public User findByName(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public User findByMobile(String mobile) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile));
    }


    @Override
    public IPage<User> findUserDetail(User user, QueryRequest request) {
        Page<User> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "u.user_id", BusApiConstant.ORDER_DESC, false);
        return this.baseMapper.findUserDetail(page, user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateLoginTime(String username) throws Exception {
        User user = new User();
        user.setLastLoginTime(new Date());

        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        // 重新将用户信息加载到 redis中
        cacheService.saveUser(username);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createUser(User user) throws Exception {
        // 检查用户名和手机号
        User checkUser1 = findByName(user.getUsername());
        if (checkUser1 != null) {
            throw new BusApiException("用户名重复");
        }
        User checkUser2 = findByMobile(user.getMobile());
        if (checkUser2 != null) {
            throw new BusApiException("手机号重复");
        }
        if(StringUtils.isNotBlank(user.getIdcard())) {
            User checkUser3 = findeByIdcard(user.getIdcard());
            if (checkUser3 != null) {
                throw new BusApiException("身份证重复");
            }
        }
        // 创建用户
        user.setCreateTime(new Date());
        user.setAvatar(User.DEFAULT_AVATAR);
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(MD5Util.encrypt(user.getUsername(), user.getPassword()));
        }
        save(user);

        // 保存用户角色
        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));

        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);

        // 向IM服务器注册用户
        registerToImServer(user);
    }

    private User findeByIdcard(String idcard) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getIdcard, idcard));
    }

    @Override
    public CommonResultEntity registerToImServer(User user) {
        CommonResultEntity entity = null;
        if (UserTypeEnum.ADMIN.equals(user.getUserType())) {
            try {
                entity = imService.addStaff(user);
                if ("10506".equals(entity.getCode()) || "10000".equals(entity.getCode())) {
                    user.setIm(true);
                    updateProfile(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                entity = imService.addGuardian(user);
                if ("10506".equals(entity.getCode()) || "10000".equals(entity.getCode())) {
                    user.setIm(true);
                    updateProfile(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entity;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateUser(User user) throws Exception {
        // 更新用户
        user.setPassword(null);
        user.setModifyTime(new Date());
        updateById(user);

        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));

        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

        // 重新将用户信息，用户角色信息，用户权限信息 加载到 redis中
        cacheService.saveUser(user.getUsername());
        cacheService.saveRoles(user.getUsername());
        cacheService.savePermissions(user.getUsername());
        // 向IM服务器注册用户
        imService.updateStaff(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteUsers(String[] userIds) throws Exception {
        // 不能删除超级管理员
        List<User> superadminList = listByRoleId(BusApiConstant.ROLE_ADMIN);
        List<User> filterSuperAdmin = superadminList.stream().filter( u -> {
            return ArrayUtils.contains(userIds, u.getUserId().toString());
        }).collect(Collectors.toList());
        if(!filterSuperAdmin.isEmpty()) {
            throw new BusApiException("不能删除超级管理员用户");
        }
        // 先删除相应的缓存
        this.userManager.deleteUserRedisCache(userIds);

        List<Long> list = Stream.of(userIds)
                .map(Long::parseLong)
                .collect(Collectors.toList());

        removeByIds(list);

        // 删除用户角色
        this.userRoleService.deleteUserRolesByUserId(list.toArray(new Long[0]));
        // 删除用户个性化配置
        this.userConfigService.deleteByUserId(userIds);
        for (String userId : userIds) {
            // 向IM服务器删除用户
            imService.deleteStaff(userId);
        }
    }

    private List<User> listByRoleId(Long roleId) {
        return baseMapper.listByRoleId(roleId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateProfile(User user) throws Exception {
        updateById(user);
        // 重新缓存用户信息
        cacheService.saveUser(user.getUsername());
        // 向IM服务器更新用户
//        imService.updateStaff(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateAvatar(String username, String avatar) throws Exception {
        User user = new User();
        user.setAvatar(avatar);

        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        // 重新缓存用户信息
        cacheService.saveUser(username);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updatePassword(String username, String password) throws Exception {
        User user = new User();
        user.setPassword(MD5Util.encrypt(username, password));

        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        // 重新缓存用户信息
        cacheService.saveUser(username);
//        imService.updateStaff(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void registVisitor(String username, String password) throws Exception {
        User user = new User();
        user.setPassword(MD5Util.encrypt(username, password));
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setStatus(User.STATUS_VALID);
        user.setSsex(User.SEX_UNKNOW);
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setDescription("注册用户");
        this.save(user);

        UserRole ur = new UserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(2L); // 注册用户角色 ID
        this.userRoleMapper.insert(ur);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));
        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);

        // 向IM服务器注册用户
        registerToImServer(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public User registVisitor(String mobile) throws Exception {
        User user = new User();
        user.setUsername(mobile);
        user.setMobile(mobile);
        user.setCreateTime(new Date());
        user.setStatus(User.STATUS_VALID);
        user.setSsex(User.SEX_UNKNOW);
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setDescription("游客");
        user.setUserType(UserTypeEnum.MEMBER);
        this.save(user);

        UserRole ur = new UserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(BusApiConstant.ROLE_VISTOR);
        this.userRoleMapper.insert(ur);

        // 创建用户默认的个性化配置
        userConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));
        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);

        // 向IM服务器注册用户
        registerToImServer(user);

        return user;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void resetPassword(String[] usernames) throws Exception {
        for (String username : usernames) {

            User user = new User();
            user.setPassword(MD5Util.encrypt(username, User.DEFAULT_PASSWORD));

            this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));
            // 重新将用户信息加载到 redis中
            cacheService.saveUser(username);
        }

    }

    @Override
    public IPage<User> listGuardianDetail(User user, QueryRequest queryRequest) {
        Page<User> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.baseMapper.listGuardianDetail(page, user);
    }

    @Override
    public List<User> listGuardianDetail(User user) {
        Page<User> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        this.baseMapper.listGuardianDetail(page, user);
        return page.getRecords();
    }

    @Override
    public IPage<User> listVisitorDetail(User user, QueryRequest queryRequest) {
        Page<User> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
//        SortUtil.handlePageSort(queryRequest, page, "userId", BusApiConstant.ORDER_ASC, false);
        return this.baseMapper.listVisitorDetail(page, user);
    }

    @Override
    public List<User> listVisitorDetail(User user) {
        Page<User> page = new Page<>();
        page.setSize(-1);
        page.setSearchCount(false);
        this.baseMapper.listVisitorDetail(page, user);
        return page.getRecords();
    }

    @Override
    public List<User> listUserDetail(User user) {
        Page<User> page = new Page<>();
        page.setSearchCount(false);
        page.setSize(-1);
        this.baseMapper.findUserDetail(page, user);
        return page.getRecords();
    }

    @Override
    public User getDriverByBusCode(String busCode) {
        return baseMapper.getDriverByBusCode(busCode);
    }

    @Override
    public List<User> listGuardianByStudent(Long studentId) {
        return baseMapper.listGuardianByStudent(studentId);
    }

    @Override
    public void updateRegistrationId(String registrationId, Long userId) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(User::getUserId, userId);
        wrapper.lambda().set(User::getRegistrationId, registrationId);
        this.update(wrapper);
    }

    @Override
    public void clearRegistrationId(String username) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(User::getUsername, username);
        wrapper.lambda().set(User::getRegistrationId, null);
        this.update(wrapper);
    }

    @Override
    public void batchImport(List<User> list) {
        for(User importUser : list) {
            // 0=男,1=女,2=保密
            String ssex = importUser.getSsex();
            if("男".equals(ssex)) {
                importUser.setSsex(User.SEX_MALE);
            }else if("女".equals(ssex)) {
                importUser.setSsex(User.SEX_FEMALE);
            }else{
                importUser.setSsex(User.SEX_UNKNOW);
            }

            Dept dept = deptService.getByDeptName(importUser.getDeptName());
            importUser.setDeptId(dept.getDeptId());

            QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
            roleQueryWrapper.lambda().eq(Role::getRoleName, importUser.getRoleName());
            Role role = roleService.getOne(roleQueryWrapper);
            importUser.setRoleId(role.getRoleId().toString());
            importUser.setStatus(User.STATUS_VALID);

            // 检查用户名有没有重复
            User user = findByName(importUser.getUsername());
            if(user == null) {
                try {
                    //设置默认密码
                    importUser.setPassword(User.DEFAULT_PASSWORD);
                    createUser(importUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                importUser.setUserId(user.getUserId());
                try {
                    updateUser(importUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<User> listBusLeaderByBus(String busCode) {
        return this.baseMapper.listBusLeaderByBus(busCode);
    }

    @Override
    public List<User> listByDept(Long deptId) {
        List<Dept> deptList = deptService.listChildrenDepts(deptId);
        if (deptList.isEmpty()) {
            return new ArrayList<>();
        }
        Long[] deptIds = deptList.stream().map(Dept::getDeptId).collect(Collectors.toList()).toArray(new Long[]{});
        return this.baseMapper.listByDeptList(deptIds);
    }

    @Override
    public void updateMobile(String mobile) throws Exception {
        User checkUser = findByMobile(mobile);
        if (checkUser != null) {
            throw new BusApiException("手机号重复");
        }
        Long userId = BusApiUtil.getCurrentUser().getUserId();
        User user = baseMapper.selectById(userId);
        user.setMobile(mobile);
        this.updateProfile(user);
    }


    @Override
    public User createGuardianUser(String mobile, String realname, String idcard) throws Exception {
        User user;
        user = new User();
        user.setUsername(IdWorker.getIdStr());
        user.setMobile(mobile);
        user.setCreateTime(new Date());
        user.setRealname(realname);
        user.setStatus(GuardianStatusEnum.NORMAL.getValue());
        user.setUserType(UserTypeEnum.MEMBER);
        user.setIdcard(idcard);
        user.setRoleId(String.valueOf(BusApiConstant.ROLE_MAINGUARDIAN));
        createUser(user);
        return user;
    }

    @Override
    public void updateJiguangId(String username, String registrationID) {
        User user = new User();
        user.setRegistrationId(registrationID);
        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public List<User> listOnRouteByWorkerId(Long busTeacherId) {
        return baseMapper.listOnRouteByWorkerId(busTeacherId);
    }

    @Override
    public List<User> listOnRouteByBusId(Long busId) {
        return baseMapper.listOnRouteByBusId(busId);
    }

    private void setUserRoles(User user, String[] roles) {
        Arrays.stream(roles).forEach(roleId -> {
            UserRole ur = new UserRole();
            ur.setUserId(user.getUserId());
            ur.setRoleId(Long.valueOf(roleId));
            this.userRoleMapper.insert(ur);
        });
    }
}
