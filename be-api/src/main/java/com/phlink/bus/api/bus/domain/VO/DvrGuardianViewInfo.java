package com.phlink.bus.api.bus.domain.VO;

import com.phlink.bus.api.bus.domain.DvrCameraConfig;
import com.phlink.bus.api.bus.domain.DvrServer;
import lombok.Data;

import java.util.List;

@Data
public class DvrGuardianViewInfo {
    private String dvrCode;
    private DvrServer dvrServer;
    private DvrCameraConfig dvrCamera;
    private String token;
}
