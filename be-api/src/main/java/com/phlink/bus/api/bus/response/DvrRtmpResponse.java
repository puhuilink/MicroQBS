package com.phlink.bus.api.bus.response;

import lombok.Data;

@Data
public class DvrRtmpResponse {

    private Integer result;
    private String ip;
    private Integer port;
    private Integer channelCode;

}
