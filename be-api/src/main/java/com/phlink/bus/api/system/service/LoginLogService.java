package com.phlink.bus.api.system.service;

import com.phlink.bus.api.system.domain.LoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog(LoginLog loginLog);

    int getCountByUser(String username);
}
