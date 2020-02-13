package com.phlink.bus.api.common.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.properties.BusApiProperties;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.domain.enums.GroupTypeEnum;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.system.domain.Dept;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.DeptService;
import com.phlink.bus.api.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order
@Slf4j
@Component
public class ImInitRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private IImGroupsService imGroupsService;
    @Autowired
    private BusApiProperties busApiProperties;

    @Override
    public void run(ApplicationArguments args) {
//        initDeptToImServer();
//        initUserToImServer();
    }

    private void initDeptToImServer() {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dept::getIm, false);
        List<Dept> deptList = deptService.list(queryWrapper);
        List<ImGroups> imGroups = imGroupsService.list();
        Map<String, List<ImGroups>> groupsMap = imGroups.stream().filter(g -> StringUtils.isNotBlank(g.getDepartId())).collect(Collectors.groupingBy(ImGroups::getDepartId));

        deptList.forEach(d -> {
            deptService.registerToImServer(d, d.getBuildGroup());
            List<ImGroups> groupsList = groupsMap.get(String.valueOf(d.getDeptId()));
            if(groupsList == null || groupsList.isEmpty()) {
                ImGroups groups = new ImGroups();
                groups.setDepartId(String.valueOf(d.getDeptId()));
                groups.setType(GroupTypeEnum.DEPT);
                groups.setName(d.getDeptName());
                groups.setCompanyId(busApiProperties.getIm().getBusOrgId());
                try {
                    imGroupsService.createImGroups(groups);
                } catch (RedisConnectException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initUserToImServer() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getIm, false);
        List<User> userList = userService.list(queryWrapper);

        userList.forEach(u -> {
            userService.registerToImServer(u);
        });
    }
}
