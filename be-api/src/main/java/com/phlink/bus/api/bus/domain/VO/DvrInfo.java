package com.phlink.bus.api.bus.domain.VO;

import com.phlink.bus.api.bus.domain.DvrCameraConfig;
import com.phlink.bus.api.bus.domain.DvrServer;
import com.phlink.bus.api.bus.response.DvrRtmpResponse;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DvrInfo {
    private Integer channelNumber;
    private String dvrCode;
    private DvrServer dvrServer;
    private List<DvrCameraConfig> channelConfig;
    private String token;
}
