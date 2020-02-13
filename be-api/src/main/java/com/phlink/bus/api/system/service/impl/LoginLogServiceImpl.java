package com.phlink.bus.api.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.phlink.bus.api.common.utils.AddressUtil;
import com.phlink.bus.api.common.utils.HttpContextUtil;
import com.phlink.bus.api.common.utils.IPUtil;
import com.phlink.bus.api.system.dao.LoginLogMapper;
import com.phlink.bus.api.system.domain.LoginLog;
import com.phlink.bus.api.system.service.LoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service("loginLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    @Transactional
    public void saveLoginLog(LoginLog loginLog) {
        loginLog.setLoginTime(new Date());
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        loginLog.setIp(ip);
        loginLog.setLocation(AddressUtil.getCityInfo(ip));
        this.save(loginLog);
    }

    @Override
    public int getCountByUser(String username) {
        LambdaQueryWrapper<LoginLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LoginLog::getUsername, username);
        return this.count(queryWrapper);
    }
}
