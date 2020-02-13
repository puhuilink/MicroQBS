package com.phlink.bus.core.iotdata.service;

import com.phlink.bus.common.Constants;
import io.rpc.core.device.BusInfo;
import io.rpc.core.device.EWatchInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBoundedBlockingQueue;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceInfoService {

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
    public void saveBusInfo(BusInfo busInfo) {
        String key = Constants.QUEUE_DVR_INFO + busInfo.getBusId();
        RScoredSortedSet<BusInfo> set = redissonClient.getScoredSortedSet(key);
        set.add(busInfo.getTimestamp(), busInfo);
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
    public Collection<BusInfo> listBusInfo(Long startTimestamp, Long endTimestamp, String busId) {
        String key = Constants.QUEUE_DVR_INFO + busId;
        RScoredSortedSet<BusInfo> set = redissonClient.getScoredSortedSet(key);
        return set.valueRange(startTimestamp, true, endTimestamp, true);
    }

    /**
     * @Description: 获得最新的设备信息
     * @Param: [deviceId]
     * @Return: io.rpc.core.device.BusInfo
     * @Author wen
     * @Date 2019-08-29 12:35
     */
    public BusInfo getLastBus(String busId) {
        String key = Constants.QUEUE_DVR_INFO + busId;
        RScoredSortedSet<BusInfo> set = redissonClient.getScoredSortedSet(key);
        return set.last();
    }
}
