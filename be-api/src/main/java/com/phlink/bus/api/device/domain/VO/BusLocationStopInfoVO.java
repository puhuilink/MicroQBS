package com.phlink.bus.api.device.domain.VO;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import com.phlink.bus.api.route.domain.Stop;
import com.phlink.bus.api.trajectory.domain.Trajectory;
import lombok.Data;

import java.util.List;

@Data
public class BusLocationStopInfoVO{

    private Bus bus;
    private DvrLocation location;
    private Long lastStopId;
    private String lastStopName;
    private Integer lastStopDistance;
    private Long nextStopId;
    private String nextStopName;
    private Integer nextStopDistance;
    private Long currentStopId;
    private String currentStopName;

}
