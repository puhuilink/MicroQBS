package com.phlink.bus.api.device.domain.VO;

import com.phlink.bus.api.bus.domain.Bus;
import com.phlink.bus.api.bus.domain.DvrLocation;
import lombok.Data;

@Data
public class BusDetailLocationVO extends Bus {
    private Long userId;
    private String driverName;
    private String driverMobile;
    private String busTeacherName;
    private String busTeacherMobile;
    private String tirpId;
    private DvrLocation location;

}
