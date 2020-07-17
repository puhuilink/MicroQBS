/*
 * @Author: sevncz.wen
 * @Date: 2020-05-18 18:05:19
 * @Last Modified by: sevncz.wen
 * @Last Modified time: 2020-05-18 18:08:04
 */
package com.puhuilink.qbs.core.web.controller.manage;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.gson.Gson;
import com.puhuilink.qbs.core.web.controller.vo.UserData;
import com.puhuilink.qbs.core.web.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDataListener extends AnalysisEventListener<UserData> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    List<UserData> list = new ArrayList<UserData>();
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private UserService userService;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param userService
     */
    public UserDataListener(UserService userService) {
        this.userService = userService;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as
     *                {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(UserData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", new Gson().toJson(data));
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        userService.saveBatch(list);
        log.info("存储数据库成功！");
    }
}
