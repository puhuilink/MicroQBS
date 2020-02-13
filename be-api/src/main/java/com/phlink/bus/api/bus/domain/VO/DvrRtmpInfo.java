package com.phlink.bus.api.bus.domain.VO;

import com.phlink.bus.api.bus.response.DvrRtmpResponse;
import lombok.Data;

import java.util.Map;

@Data
public class DvrRtmpInfo {
    private Integer channelNumber;
    private String dvrCode;
    private Map<Integer, DvrRtmpResponse> data;
}
