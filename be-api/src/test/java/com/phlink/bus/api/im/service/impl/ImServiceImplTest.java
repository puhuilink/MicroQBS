package com.phlink.bus.api.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.phlink.bus.api.ApiApplication;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.properties.BusApiProperties;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.domain.enums.GroupTypeEnum;
import com.phlink.bus.api.im.service.IImGroupsService;
import com.phlink.bus.api.im.service.IImService;
import com.phlink.bus.api.route.domain.Route;
import com.phlink.bus.api.route.domain.RouteOperation;
import com.phlink.bus.api.route.service.IRouteOperationService;
import com.phlink.bus.api.route.service.IRouteService;
import com.phlink.bus.api.serviceorg.domain.Guardian;
import com.phlink.bus.api.serviceorg.domain.Student;
import com.phlink.bus.api.serviceorg.service.IGuardianService;
import com.phlink.bus.api.serviceorg.service.IStudentService;
import com.phlink.bus.api.system.domain.Dept;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.DeptService;
import com.phlink.bus.api.system.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
public class ImServiceImplTest {

    @Autowired
    private IImService imService;
    @Autowired
    private IGuardianService guardianService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private IImGroupsService imGroupsService;
    @Autowired
    private BusApiProperties busApiProperties;
    @Autowired
    private IRouteOperationService routeOperationService;
    @Autowired
    private IRouteService routeService;

    @Test
    public void testLogin() throws RedisConnectException {
        imService.loginToImServer();
    }

    @Test
    public void testCreateDept() throws RedisConnectException {
        Dept dept = new Dept();
        dept.setDeptId(1156517380235587585L);
        dept.setDeptName("随车老师一队");
        imService.createDept(dept, true, busApiProperties.getIm().getBusOrgId());
    }

    @Test
    public void testUpdateDept() throws RedisConnectException {
        Dept dept = new Dept();
        dept.setDeptId(1156517143345491969L);
        dept.setDeptName("校车一队");
        imService.updateDept(dept, true);
    }

    @Test
    public void testDelDept() throws RedisConnectException {
        imService.deleteDept("1156517380235587585L");
    }

    @Test
    public void testAddStaff() throws RedisConnectException {
        User user = userService.findByMobile("18310161102");

        imService.addStaff(user);
//        imService.updateGuardian(user);
    }

    @Test
    public void testRegisterToImServer() {
        User user = userService.findByMobile("18310161102");
        userService.registerToImServer(user);
    }

    @Test
    public void testUpdateStaff() throws RedisConnectException {
        User user = new User();
        user.setUserId(1169128044572778497L);
        user.setMobile("13269323860");
        user.setPassword("12345678");
        user.setRealname("李凤英");
        user.setDeptId(1L);
        user.setStatus(User.STATUS_VALID);
        imService.updateStaff(user);
    }

    @Test
    public void testDelStaff() throws RedisConnectException {
        imService.deleteStaff("1177063420642463746");
    }

    @Test
    public void testDelGuardian() throws RedisConnectException {
        List<Guardian> list = guardianService.list();
        list.forEach( l -> {
            try {
                imService.deleteStaff(String.valueOf(l.getUserId()));
            } catch (RedisConnectException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testAddGroup() throws RedisConnectException {
        ImGroups groups = new ImGroups();
        groups.setId(1L);
        groups.setType(GroupTypeEnum.CUSTOMIZE);
        groups.setName("群1");
        groups.setManagerId(23L);
        imService.addGroup(groups);
    }

    @Test
    public void testTransferGroupManager() throws RedisConnectException {
        String groupId = "1";
        String newManagerId = "23";
        imService.transferGroupManager(groupId, newManagerId);
    }

    @Test
    public void testDelGroup() throws RedisConnectException {
        imService.deleteGroup("1");
    }

    @Test
    public void testInitStaff() {
        List<User> userList=this.userService.list();
//        userList.stream().forEach(user-> {
//            try {
//                this.imService.addStaff(user);
//            } catch (RedisConnectException e) {
//                e.printStackTrace();
//            }
//        });

   /*     List<Dept> deptList=this.deptService.list();
        for (Dept dept:deptList) {
            ImGroups groups = new ImGroups();
            groups.setId(dept.getDeptId());
            groups.setType(GroupTypeEnum.DEPT);
            groups.setName("群1");
            groups.setManagerId(23L);
        }*/
    }

    @Test
    public void testInitDept() {
        imService.initImDept();
    }

    @Test
    public void testInitUser() {
        imService.initImUser();
    }

    @Test
    public void testInitGuardian() {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getIm, false);
        List<Student> students = studentService.list(queryWrapper);

        for (Student s : students) {
            try {
                Long userId = s.getMainGuardianId();
//                Long routeOperationId = s.getRouteOperationId();

                User user = userService.getById(userId);
                if (user != null) {
//                    user.setDeptId(routeOperationId);
                    userService.registerToImServer(user);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testInitRouteGroup() {
        LambdaQueryWrapper<RouteOperation> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getIm, false);
        List<RouteOperation> routeOperations = routeOperationService.list(queryWrapper);

        routeOperations.forEach(r -> {
            try {
                Route route = routeService.getById(r.getRouteId());

                ImGroups imGroups = new ImGroups();
                imGroups.setCompanyId(busApiProperties.getIm().getRouteOrgId());
                imGroups.setType(GroupTypeEnum.CUSTOMIZE);
                imGroups.setManagerId(r.getBindBusTeacherId());
                imGroups.setName(route.getRouteName());
                imGroups.setDepartId(String.valueOf(r.getId()));

                imGroupsService.createImGroups(imGroups);


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testInitRouteGroupMember() {
        LambdaQueryWrapper<RouteOperation> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getIm, false);
        List<RouteOperation> routeOperations = routeOperationService.list(queryWrapper);

        routeOperations.forEach(r -> {
            try {
                LambdaQueryWrapper<Student> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(Student::getRouteOperationId, r.getId());
                List<Student> students = studentService.list(queryWrapper1);

                LambdaQueryWrapper<ImGroups> queryWrapper2 = new LambdaQueryWrapper<>();
                queryWrapper2.eq(ImGroups::getDepartId, String.valueOf(r.getId()));
                ImGroups imGroups = imGroupsService.getByDepartId(r.getId());

                imGroupsService.invite(imGroups.getGroupId(), students.stream().map(Student::getMainGuardianId).toArray(Long[]::new));


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}