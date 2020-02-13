package com.phlink.bus.api.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.phlink.bus.api.common.domain.BusApiConstant;
import com.phlink.bus.api.common.domain.QueryRequest;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.properties.BusApiProperties;
import com.phlink.bus.api.common.service.RedisService;
import com.phlink.bus.api.common.utils.SpringContextUtil;
import com.phlink.bus.api.im.domain.ImGroups;
import com.phlink.bus.api.im.response.CommonResultEntity;
import com.phlink.bus.api.im.response.LoginResultEntity;
import com.phlink.bus.api.im.service.IImService;
import com.phlink.bus.api.system.domain.Dept;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.service.DeptService;
import com.phlink.bus.api.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ImServiceImpl implements IImService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BusApiProperties busApiProperties;
    @Autowired
    private RedisService redisService;
    @Lazy
    @Autowired
    private UserService userService;
    @Lazy
    @Autowired
    private DeptService deptService;


    private static final String LOGIN_URL = "/admin/login";
    private static final String DEPT_URL = "/departments";
    private static final String STAFF_URL = "/staffs";
    private static final String GROUP_URL = "/groups";

    @Async
    @Scheduled(cron = "${bus-api.im.cron}")
    @Override
    public LoginResultEntity loginToImServer() throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String loginUrl = imurl + LOGIN_URL;

        HttpHeaders headers = new HttpHeaders();
        Map<String, String> params = new HashedMap();
        params.put("username", busApiProperties.getIm().getUsername());
        params.put("password", busApiProperties.getIm().getPassword());
        HttpEntity<Map<String, String>> formEntity = new HttpEntity<>(params, headers);

        ResponseEntity<LoginResultEntity> responseEntity1 = this.restTemplate.postForEntity(loginUrl, formEntity, LoginResultEntity.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        HttpHeaders header = responseEntity1.getHeaders();
        LoginResultEntity testEntity2 = responseEntity1.getBody();
        log.info("post testEntity2: {}", testEntity2);
        log.info("post statusCode: {}", statusCode);
        List<String> cookie = header.get("Set-Cookie");
        log.info("post cookie: {}", cookie);
        if(cookie != null && !cookie.isEmpty()) {
            redisService.set(BusApiConstant.IM_COOKIE, cookie.get(0));
        }else{
            log.error("login return cookie is null");
        }
        return testEntity2;
    }

    @Override
    public CommonResultEntity createDept(Dept dept, Boolean withGroup, String companyId) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + DEPT_URL;

        Map<String, Object> params = new HashedMap();
        if(dept.getDeptId() != null) {
            params.put("id", dept.getDeptId());
        }
        if(dept.getParentId() != null) {
            if(0L != dept.getParentId()) {
                params.put("parent_id", dept.getParentId());
            }
        }
        params.put("name", dept.getDeptName());
        params.put("full_name", dept.getDeptName());
        params.put("company_id", companyId);
        params.put("with_group", withGroup);

        return post(url, params);

    }

    @Override
    public CommonResultEntity updateDept(Dept dept, Boolean withGroup) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + DEPT_URL + "/" + dept.getDeptId();

        Map<String, Object> params = new HashedMap();
        params.put("name", dept.getDeptName());
        params.put("full_name", dept.getDeptName());
        params.put("company_id", busApiProperties.getIm().getBusOrgId());
        params.put("with_group", withGroup);
        return put(url, params);

    }

    @Override
    public CommonResultEntity deleteDept(String deptId) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + DEPT_URL + "/" + deptId;

        return delete(url);

    }

    @Override
    public CommonResultEntity addStaff(User user) throws RedisConnectException {

        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + STAFF_URL;

        Map<String, Object> params = new HashedMap();
        if(user.getUserId() != null) {
            params.put("id", user.getUserId());
        }
        params.put("mobile", user.getMobile());
        params.put("name", user.getRealname());
        params.put("password", User.IM_DEFAULT_PASSWORD);
        if(user.getDeptId() != null) {
            params.put("depart_id", user.getDeptId());
        }else{
            params.put("depart_id", busApiProperties.getIm().getBusOrgId());
        }
        params.put("state", 0);

        return post(url, params);
    }

    @Override
    public CommonResultEntity updateStaff(User user) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + STAFF_URL + "/" + user.getUserId();

        Map<String, Object> params = new HashedMap();
        params.put("mobile", user.getMobile());
        params.put("name", user.getRealname());
        params.put("password", User.IM_DEFAULT_PASSWORD);
        params.put("depart_id", user.getDeptId());
        params.put("state", 0);

        return put(url, params);
    }

    @Override
    public CommonResultEntity updateCustomer(User user) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + STAFF_URL + "/" + user.getUserId();

        Map<String, Object> params = new HashedMap();
        params.put("mobile", user.getMobile());
        params.put("name", user.getRealname());
        params.put("password", User.IM_DEFAULT_PASSWORD);
        params.put("state", 0);

        return put(url, params);
    }

    @Override
    public CommonResultEntity addToDept(User user, Long deptId) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + STAFF_URL + "/" + user.getUserId();

        Map<String, Object> params = new HashedMap();
        params.put("mobile", user.getMobile());
        params.put("name", user.getRealname());
        params.put("password", User.IM_DEFAULT_PASSWORD);
        params.put("org_ids", Arrays.asList(busApiProperties.getIm().getBusOrgId(), user.getDeptId(), deptId));
        params.put("state", 0);

        return put(url, params);
    }

    @Override
    public CommonResultEntity addGuardian(User user) throws RedisConnectException {

        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + STAFF_URL;

        Map<String, Object> params = new HashedMap();
        params.put("id", user.getUserId());
        params.put("mobile", user.getMobile());
        params.put("name", user.getRealname());
        params.put("password", User.IM_DEFAULT_PASSWORD);
        params.put("org_ids", Collections.singletonList(busApiProperties.getIm().getGuardianOrgId()));
        params.put("state", 0);

        return post(url, params);
    }

    @Override
    public CommonResultEntity updateGuardian(User user) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + STAFF_URL + "/" + user.getUserId();

        Map<String, Object> params = new HashedMap();
        params.put("mobile", user.getMobile());
        params.put("name", user.getRealname());
        params.put("password", User.IM_DEFAULT_PASSWORD);
        params.put("org_ids", Collections.singletonList(busApiProperties.getIm().getGuardianOrgId()));
        params.put("state", 0);

        return put(url, params);
    }

    @Override
    public CommonResultEntity deleteStaff(String userId) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + STAFF_URL + "/" + userId;

        return delete(url);

    }

    @Override
    public CommonResultEntity addGroup(ImGroups groups) throws RedisConnectException {

        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + GROUP_URL;

        Map<String, Object> params = new HashedMap();
        params.put("id", groups.getGroupId());
//        params.put("company_id", busApiProperties.getIm().getCompanyId());
        params.put("type", groups.getType().getValue());
        params.put("name", groups.getName());
        if(groups.getManagerId() != null) {
            params.put("manager_id", groups.getManagerId());
            params.put("member_ids", groups.getManagerId());
        }
//        if(StringUtils.isNotBlank(groups.getDepartId())) {
//            params.put("depart_id", groups.getDepartId());
//        }
        if(StringUtils.isNotBlank(groups.getCompanyId())) {
            params.put("company_id", groups.getCompanyId());
        }

        return post(url, params);

    }

    @Override
    public CommonResultEntity transferGroupManager(String groupId, String newManagerId) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + GROUP_URL + "/" + groupId + "/transfer_manager";

        Map<String, Object> params = new HashedMap();
        params.put("id", newManagerId);

        return put(url, params);

    }

    @Override
    public CommonResultEntity deleteGroup(String groupId) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + GROUP_URL + "/" + groupId;

        return delete(url);
    }

    @Override
    public CommonResultEntity updateGroupName(String groupId, String name) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + GROUP_URL + "/" + groupId + "/name";
        Map<String, Object> params = new HashedMap();
        params.put("name", name);
        return put(url, params);
    }

    @Override
    public CommonResultEntity invite(String groupId, Long[] ids) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + GROUP_URL + "/" + groupId + "/invite";
        Map<String, Object> params = new HashedMap();
        params.put("ids", ids);
        return post(url, params);
    }

    @Override
    public CommonResultEntity remove(String groupId, Long[] ids) throws RedisConnectException {
        String imurl = busApiProperties.getIm().getUrl();
        String url = imurl + GROUP_URL + "/" + groupId + "/remove";
        Map<String, Object> params = new HashedMap();
        params.put("ids", ids);
        return post(url, params);
    }

    private CommonResultEntity post(String url, Map<String, Object> params) throws RedisConnectException {
        String cookie = redisService.get(BusApiConstant.IM_COOKIE);

        HttpHeaders headers = new HttpHeaders();
        headers.add("cookie", cookie);
        HttpEntity<Map<String, Object>> formEntity = new HttpEntity<>(params, headers);
        ResponseEntity<CommonResultEntity> responseEntity1 = this.restTemplate.postForEntity(url, formEntity, CommonResultEntity.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        CommonResultEntity entity = responseEntity1.getBody();
        log.info("post params: {}", JSON.toJSONString(params));
        log.info("post responseEntity1: {}", JSON.toJSONString(entity));
        log.info("post statusCode: {}", statusCode);
        return entity;
    }

    private CommonResultEntity put(String url, Map<String, Object> params) throws RedisConnectException {
        String cookie = redisService.get(BusApiConstant.IM_COOKIE);

        HttpHeaders headers = new HttpHeaders();
        headers.add("cookie", cookie);
        HttpEntity<Map<String, Object>> formEntity = new HttpEntity<>(params, headers);
        ResponseEntity<CommonResultEntity> responseEntity1 = this.restTemplate.exchange(url, HttpMethod.PUT, formEntity, CommonResultEntity.class, params);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        CommonResultEntity entity = responseEntity1.getBody();
        log.info("put params: {}", params);
        log.info("put responseEntity1: {}", entity);
        log.info("put statusCode: {}", statusCode);
        return entity;
    }

    private CommonResultEntity delete(String url) throws RedisConnectException {
        String cookie = redisService.get(BusApiConstant.IM_COOKIE);

        HttpHeaders headers = new HttpHeaders();
        headers.add("cookie", cookie);

        HttpEntity<Map<String, Object>> formEntity = new HttpEntity<>(headers);
        ResponseEntity<CommonResultEntity> responseEntity1 = this.restTemplate.exchange(url, HttpMethod.DELETE, formEntity, CommonResultEntity.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        CommonResultEntity entity = responseEntity1.getBody();
        log.info("delete responseEntity1: {}", entity);
        log.info("delete statusCode: {}", statusCode);
        return entity;
    }

    @Override
    @Async
    @Scheduled(cron = "30 */1 * * * ?")
    public void initImUser() {
        if(!SpringContextUtil.isPro()) {
            return;
        }
        log.info("同步IM用户");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getIm, false);
        List<User> userList = userService.list(queryWrapper);

        userList.forEach(u -> {
            try {
                userService.registerToImServer(u);
            } catch (Exception e) {
                log.error("IM同步失败 {}", e.getMessage());
            }
        });
    }

    @Async
    @Override
    @Scheduled(cron = "0 */1 * * * ?")
    public void initImDept() {
        if(!SpringContextUtil.isPro()) {
            return;
        }
        log.info("同步IM部门");
        Dept dept = new Dept();
        QueryRequest queryRequest = new QueryRequest();
        List<Dept> deptList = deptService.findDepts(dept, queryRequest);
        for (Dept d : deptList) {
            deptService.registerToImServer(d, d.getBuildGroup());
        }
    }

}
