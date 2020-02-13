package com.phlink.bus.api.device.domain.VO;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import lombok.Data;

import java.util.List;

@Data
public class BusLocationListVO {
    private Bus bus;
    private List<DvrLocation> locations;
}
