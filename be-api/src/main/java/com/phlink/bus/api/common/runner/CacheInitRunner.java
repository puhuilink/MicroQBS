package com.phlink.bus.api.common.runner;

import com.phlink.bus.api.alarm.domain.CodeFence;
import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.service.IBusService;
import com.phlink.bus.api.common.exception.RedisConnectException;
import com.phlink.bus.api.common.service.CacheService;
import com.phlink.bus.api.common.utils.SpringContextUtil;
import com.phlink.bus.api.device.domain.Device;
import com.phlink.bus.api.device.service.IDeviceService;
import com.phlink.bus.api.fence.domain.Fence;
import com.phlink.bus.api.fence.service.IFenceService;
import com.phlink.bus.api.route.domain.vo.RouteBusVO;
import com.phlink.bus.api.route.service.IRouteService;
import com.phlink.bus.api.system.domain.SystemConfig;
import com.phlink.bus.api.system.domain.User;
import com.phlink.bus.api.system.manager.EntityManager;
import com.phlink.bus.api.system.manager.FenceManager;
import com.phlink.bus.api.system.manager.SystemConfigManager;
import com.phlink.bus.api.system.manager.UserManager;
import com.phlink.bus.api.system.service.ISystemConfigService;
import com.phlink.bus.api.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 缓存初始化
 */
@Slf4j
@Component
public class CacheInitRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private ISystemConfigService systemConfigService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SystemConfigManager systemConfigManager;

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private IBusService busService;
    @Autowired
    private IRouteService routeService;
    @Lazy
    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private IFenceService fenceService;
    @Autowired
    private FenceManager fenceManager;

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("Redis连接中 ······");
            cacheService.testConnect();

            if(SpringContextUtil.isPro()) {
                log.info("缓存初始化 ······");
                log.info("缓存用户数据 ······");
                List<User> list = this.userService.list();
                for (User user : list) {
                    userManager.loadUserRedisCache(user);
                }
                log.info("缓存系统配置数据 ······");
                List<SystemConfig> systemConfigList = this.systemConfigService.list();
                for (SystemConfig config : systemConfigList) {
                    systemConfigManager.loadSystemRedisCache(config);
                }
                log.info("缓存车辆数据 ······");
                List<Bus> busList = this.busService.list();
                this.entityManager.loadBus(busList);
                log.info("缓存车辆路线关系数据 ······");
                List<RouteBusVO> routeBusVOList = this.routeService.listRouteBus();
                this.entityManager.loadRouteBus(routeBusVOList);
                log.info("缓存车辆与百度路线电子围栏关系数据 ······");
                List<CodeFence> busFenceList = this.fenceService.getBusRouteFence();
                this.entityManager.loadBusRouteFence(busFenceList);
                log.info("缓存车辆与站点电子围栏关系数据 ······");
                List<CodeFence> stopFenceList = this.fenceService.getBusStopFence();
                this.entityManager.loadBusStopFence(stopFenceList);
                log.info("缓存手环数据 ······");
                List<Device> deviceList = this.deviceService.list();
                this.entityManager.loadDevice(deviceList);
                log.info("缓存手环与学校电子围栏关系数据 ······");
//            List<CodeFence> schoolFenceList = this.fenceService.getSchoolFence();
//            this.entityManager.loadSchoolFence(schoolFenceList);
//            log.info("缓存路线告警规则配置数据 ······");
//            List<AlarmRouteRules> alarmRouteRulesList = this.alarmRouteRulesService.list();
//            alarmRulesManager.loadAlarmRouteRules(alarmRouteRulesList);
//            log.info("缓存学校告警规则配置数据 ······");
//            List<AlarmSchoolRules> alarmSchoolRulesList = this.alarmSchoolRulesService.list();
//            alarmRulesManager.loadAlarmSchoolRules(alarmSchoolRulesList);
//            log.info("缓存车辆和设备告警规则配置数据 ······");
//            List<CodeRule> codeRuleList = new ArrayList<>();
//            codeRuleList.addAll(alarmRouteRulesService.listBusCode());
//            codeRuleList.addAll(alarmSchoolRulesService.listDeviceCode());
//            alarmRulesManager.loadBusAndDeviceAlarmRules(codeRuleList);
//            log.info("缓存电子围栏配置数据 ······");
                List<Fence> fenceList = fenceService.getRouteFence();
                fenceList.forEach(fence -> {
                    try {
                        fenceManager.loadFence(fence);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            log.error("缓存初始化失败", e);
            log.error(" ____   __    _   _ ");
            log.error("| |_   / /\\  | | | |");
            log.error("|_|   /_/--\\ |_| |_|__");
            log.error("                        ");
            log.error("BusApi 启动失败              ");
            if (e instanceof RedisConnectException) {
                log.error("Redis连接异常，请检查Redis连接配置并确保Redis服务已启动");
            }
            e.printStackTrace();
            // 关闭 BusApi
            context.close();
        }
    }
}
