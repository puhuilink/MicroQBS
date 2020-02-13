package com.phlink.bus.api.system.manager;

import com.phlink.bus.api.common.service.CacheService;
import com.phlink.bus.api.fence.domain.Fence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FenceManager {
    @Autowired
    private CacheService cacheService;

    public void loadFence(Fence fence) throws Exception {
        cacheService.saveFence(fence);
    }
}
