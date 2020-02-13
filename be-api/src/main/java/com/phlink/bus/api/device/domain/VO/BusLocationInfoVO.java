package com.phlink.bus.api.device.domain.VO;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import lombok.Data;

import java.util.List;

@Data
public class BusLocationInfoVO {
    private Bus bus;
    private DvrLocation location;
    private List<Stop> stopList;
    private Trajectory trajectory;
}
