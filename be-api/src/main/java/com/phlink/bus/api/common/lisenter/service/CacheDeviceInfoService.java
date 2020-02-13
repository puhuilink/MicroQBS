package com.phlink.bus.api.common.lisenter.service;

import com.alibaba.fastjson.JSON;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.common.Constants;
import io.rpc.core.device.EWatchInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CacheDeviceInfoService {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * @Description: 保存设备信息到队列中
     * @Param: [eWatchInfo]
     * @Return: void
     * @Author wen
     * @Date 2019-08-29 12:25
     */
    public void saveDeviceInfo(EWatchInfo eWatchInfo) {
        log.info("save to redis");
        String key = Constants.QUEUE_DEVICE_INFO + eWatchInfo.getDeviceId();
        RScoredSortedSet<EWatchInfo> set = redissonClient.getScoredSortedSet(key);
        set.add(eWatchInfo.getTimestamp(), eWatchInfo);
        if(set.size() > Constants.QUEUE_CAPACITY) {
            set.pollFirst();
        }
    }

    /**
     * @Description: 获得一个时间段内的设备信息
     * @Param: [startTimestamp, endTimestamp, deviceId]
     * @Return: java.util.List<io.rpc.core.device.EWatchInfo>
     * @Author wen
     * @Date 2019-08-29 12:35
     */
    public Collection<EWatchInfo> listDeviceInfo(Long startTimestamp, Long endTimestamp, String deviceId) {
        String key = Constants.QUEUE_DEVICE_INFO + deviceId;
        RScoredSortedSet<EWatchInfo> set = redissonClient.getScoredSortedSet(key);
        return set.valueRange(startTimestamp, true, endTimestamp, true);
    }

    /**
     * @Description: 获得最新的设备信息
     * @Param: [deviceId]
     * @Return: io.rpc.core.device.EWatchInfo
     * @Author wen
     * @Date 2019-08-29 12:35
     */
    public EWatchInfo getLastEWatch(String deviceId) {
        String key = Constants.QUEUE_DEVICE_INFO + deviceId;
        RScoredSortedSet<EWatchInfo> set = redissonClient.getScoredSortedSet(key);
        return set.last();
    }

    /**
     * @Description: 保存设备信息到队列中
     * @Param: [BusInfo]
     * @Return: void
     * @Author wen
     * @Date 2019-08-29 12:25
     */
    public void saveDvrInfo(DvrLocation dvrLocation) {
        String key = Constants.QUEUE_DVR_INFO + dvrLocation.getDvrno();
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet(key);
        set.add(dvrLocation.getGpstime(), JSON.toJSONString(dvrLocation));
        if(set.size() > Constants.QUEUE_CAPACITY) {
            set.pollFirst();
        }
    }

    /**
     * @Description: 获得一个时间段内的设备信息
     * @Param: [startTimestamp, endTimestamp, deviceId]
     * @Return: java.util.List<io.rpc.core.device.BusInfo>
     * @Author wen
     * @Date 2019-08-29 12:35
     */
    public Collection<DvrLocation> listDvrInfo(Long startTimestamp, Long endTimestamp, String dvrno) {
        String key = Constants.QUEUE_DVR_INFO + dvrno;
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet(key);
        Collection<String> result = set.valueRange(startTimestamp, true, endTimestamp, true);

        return result.stream().map( s -> {
            if(StringUtils.isBlank(s)) {
                return new DvrLocation();
            }
            return JSON.parseObject(s, DvrLocation.class);
        }).collect(Collectors.toList());
    }

    /**
     * @Description: 获得最新的设备信息
     * @Param: [deviceId]
     * @Return: io.rpc.core.device.BusInfo
     * @Author wen
     * @Date 2019-08-29 12:35
     */
    public DvrLocation getLastDvr(String dvrno) {
        String key = Constants.QUEUE_DVR_INFO + dvrno;
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet(key);
        String result = set.last();
        if(StringUtils.isBlank(result)) {
            return new DvrLocation();
        }
        return JSON.parseObject(result, DvrLocation.class);
    }

}
